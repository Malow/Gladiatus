#pragma once

#include "MaloWLib/ClientChannel.h"
#include "MaloWLib/Array.h"
#include "LoginRequest.h"
#include "LoginResponse.h"
#include "ModelInterface.h"
#include "ConvertStringToModel.h"

#include <iostream>

using namespace MaloW;

class Server : public Process
{
private:
	Array<ClientChannel*> ccs;

public:
	Server();
	virtual ~Server();

	void ClientConnected(ClientChannel* cc);

	virtual void Life();
};

