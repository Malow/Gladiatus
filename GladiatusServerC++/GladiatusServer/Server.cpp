#include "Server.h"

Server::Server()
{
}

Server::~Server()
{
	for(int i = 0; i < this->ccs.size(); i++)
	{
		this->ccs.get(i)->Close();
	}
	for(int i = 0; i < this->ccs.size(); i++)
	{
		this->ccs.get(i)->WaitUntillDone();
	}
	while(this->ccs.size() > 0)
	{
		delete this->ccs.get(0);
	}
}

void Server::ClientConnected( ClientChannel* cc )
{
	this->ccs.add(cc);
	cc->setNotifier(this);
	cc->Start();
	cout << "Client " << this->ccs.size() << " connected." << endl << "> ";
}

void Server::Life()
{
	while(this->stayAlive)
	{
		ProcessEvent* ev = this->WaitEvent();
		if(NetworkPacket* np = dynamic_cast<NetworkPacket*>(ev))
		{
			cout << "Client " << np->GetSenderID() + 1 << ": " << np->GetMessage() << endl << "> ";
			ModelInterface* request = ConvertStringToModel(np->GetMessage());
			if(LoginRequest* loginRequest = dynamic_cast<LoginRequest*>(request))
			{
				// auth
				string hashCode = "QPQWHE239847JOQWHE";
				LoginResponse response = LoginResponse(hashCode);
				this->ccs.get(np->GetSenderID())->sendData(response.toNetworkString());
			}

			if(request)
			{
				delete request;
				request = NULL;			
			}
		}
		delete ev;
	}
}
