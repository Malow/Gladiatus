#pragma once

#include "ModelInterface.h"
#include "LoginRequest.h"
#include <string>

using namespace std;

static ModelInterface* ConvertStringToModel(string msg)
{
	LoginRequest* loginRequest = LoginRequest::toModel(msg);
	return loginRequest;

}