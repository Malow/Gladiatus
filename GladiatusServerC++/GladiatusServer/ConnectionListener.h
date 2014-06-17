#pragma once

#include "MaloWLib/NetworkServer.h"
#include "Server.h"

using namespace MaloW;

class ConnectionListener : public NetworkServer
{
private:
	Server* server;

public:
	ConnectionListener(int port, Server* server) : NetworkServer(port)
	{
		this->server = server;
	}

	virtual ~ConnectionListener()
	{

	}

	virtual void ClientConnected(ClientChannel* cc)
	{
		this->server->ClientConnected(cc);
	}

};
