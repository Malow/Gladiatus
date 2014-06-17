#include "ServerChannel.h"
#include "NetworkPacket.h"
#include "WindowsWrapper.h"

using namespace MaloW;

ServerChannel::ServerChannel(string IP, int port)
{
	this->stayAlive = true;
	this->notifier = NULL;

	WSADATA wsaData;
	int retCode = WSAStartup(MAKEWORD(2,2), &wsaData);
	if(retCode != 0) 
	{
		this->stayAlive = false;
		MaloW::Debug("Failed to init Winsock library. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		WSACleanup();
	}

	// open a socket
	this->sock = socket(AF_INET, SOCK_STREAM, IPPROTO_IP);
	if(this->sock == INVALID_SOCKET) 
	{
		this->stayAlive = false;
		MaloW::Debug("Invalid socket, failed to create socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		WSACleanup();
	}

	// connect
	sockaddr_in saServer;
	saServer.sin_port = htons(port);
	saServer.sin_addr.s_addr = inet_addr(IP.c_str());
	saServer.sin_family = AF_INET;
	retCode = connect(this->sock, (sockaddr*)&saServer, sizeof(sockaddr));
	if(retCode == SOCKET_ERROR)
	{
		this->stayAlive = false;
		MaloW::Debug("Connection to server failed. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		WSACleanup();
	}

	this->buffer = "";
	this->unImportantFilter = "";
}

ServerChannel::~ServerChannel()
{
	this->stayAlive = false;
	if(this->sock != 0)
	{
		// shutdown socket
		int retCode = shutdown(this->sock, SD_BOTH);
		if(retCode == SOCKET_ERROR) 
		{
			MaloW::Debug("Error trying to perform shutdown on socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		}
		// close server socket
		retCode = closesocket(this->sock);
		if(retCode == SOCKET_ERROR) 
		{
			MaloW::Debug("Error failed to close socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		}
	}
	// Release WinSock DLL
	int retCode = WSACleanup();
	if(retCode == SOCKET_ERROR) 
	{
		MaloW::Debug("Error cleaning up Winsock Library. Error: " + MaloW::convertNrToString(WSAGetLastError()));;
	}
}

string ServerChannel::receiveData()
{
	std::string msg = "";
	bool getNewData = true;
	if(!this->buffer.empty())
	{
		if(int pos = this->buffer.find(10))
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
			char bufs[1024] = {0};
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
				MaloW::Debug("Server dissconnected, closing.");
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

void ServerChannel::sendData(string msg)
{
	msg += 10;
	char bufs[1024] = {0};
	for(int i = 0; i < msg.length(); i++)
	{
		bufs[i] = msg[i];
	}
	int retCode = send(this->sock, bufs, sizeof(bufs), 0);
	if(retCode == SOCKET_ERROR)
	{
		MaloW::Debug("Error sending data. Error: " + MaloW::convertNrToString(WSAGetLastError()));
	}
}

void ServerChannel::Life()
{
	while(this->stayAlive)
	{
		string msg = this->receiveData();
		if(msg != "")
		{
			if(this->notifier && this->stayAlive)
			{
				NetworkPacket* np = new NetworkPacket(msg, 0);

				if(this->unImportantFilter != "")
				{
					// Filter traffic that isnt important
					int res = msg.find(this->unImportantFilter);
					bool important = true;
					if(res != -1)
						important = false;

					this->notifier->PutEvent(np, important);
				}
				else
					this->notifier->PutEvent(np);
			}
		}
	}
}

void ServerChannel::CloseSpecific()
{
	if(this->sock != 0)
	{
		int retCode = shutdown(this->sock, SD_BOTH);
		if(retCode == SOCKET_ERROR) 
			MaloW::Debug("Error trying to perform shutdown on socket from a ->Close() call. Error: " + MaloW::convertNrToString(WSAGetLastError()));
	
		retCode = closesocket(this->sock);
		if(retCode == SOCKET_ERROR) 
		{
			MaloW::Debug("Error failed to close socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		}
	}
	this->sock = 0;
}