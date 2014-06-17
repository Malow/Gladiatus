#include "ClientChannel.h"
#include "WindowsWrapper.h"

using namespace MaloW;

long ClientChannel::NextCID = 0;

ClientChannel::ClientChannel(SOCKET sock)
{
	this->id = this->NextCID;
	this->NextCID++;
	this->sock = sock;
	this->stayAlive = true;
	this->buffer = "";
}

ClientChannel::~ClientChannel()
{
	int retCode = closesocket(this->sock);
	if(retCode == SOCKET_ERROR) 
		MaloW::Debug("Error failed to close socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
}

string ClientChannel::receiveData()
{
	std::string msg = "";
	bool getNewData = true;
	if(!this->buffer.empty())
	{
		int pos = this->buffer.find(10);
		if(pos > 0)
		{
			msg = this->buffer.substr(0, pos);
			this->buffer = this->buffer.substr(pos+1, this->buffer.length());
			getNewData = false;
		}
	}
	if(getNewData)
	{
		bool goAgain = true;
		do
		{
			char bufs[1024] = {255};
			int retCode = 0;
			retCode = recv(this->sock, bufs, sizeof(bufs), 0);
			if(retCode == SOCKET_ERROR)
			{
				this->Close();
				MaloW::Debug("Error recieving data. Error: " + MaloW::convertNrToString(WSAGetLastError()) + ". Probably due to crash/improper dissconnect");
			}
			else if(retCode == 0)
			{
				this->Close();
				MaloW::Debug("Client dissconnected, closing.");
			}
			else
			{
				for(int i = 0; i < 1024; i++)
				{
					if(bufs[i] == 10)
						goAgain = false;
					if(bufs[i] != 0)
						this->buffer += bufs[i];
					else
						i = 1024;
				}
				
				if(!goAgain)
				{
					for(int i = 0; i < 1024; i++)
					{
						if(this->buffer[i] != 10)
							msg += this->buffer[i];
						else
						{
							this->buffer = this->buffer.substr(i+1, this->buffer.length());
							i = 1024;
						}
					}
				}
			}
		}
		while(goAgain && this->stayAlive);
	}

	return msg;
}

void ClientChannel::sendData(string msg)
{
	msg += 10;
	char bufs[1024] = {0};
	for(int i = 0; i < msg.length(); i++)
		bufs[i] = msg[i];

	int retCode = send(this->sock, bufs, sizeof(bufs), 0);
	if(retCode == SOCKET_ERROR)
	{
		MaloW::Debug("Error sending data. Error: " + MaloW::convertNrToString(WSAGetLastError()));
	}
}

void ClientChannel::Life()
{
	while(this->stayAlive)
	{
		string msg = this->receiveData();
		if(msg != "")
		{
			if(this->notifier)
			{
				NetworkPacket* np = new NetworkPacket(msg, this->id);
				this->notifier->PutEvent(np);
			}
		}
	}
}

void ClientChannel::CloseSpecific()
{
	int retCode = shutdown(this->sock, SD_BOTH);
	if(retCode == SOCKET_ERROR) 
		MaloW::Debug("Error trying to perform shutdown on socket from a ->Close() call. Error: " + MaloW::convertNrToString(WSAGetLastError()));
}