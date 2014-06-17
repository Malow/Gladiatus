#pragma once

#include <string>
#include "ModelInterface.h"
#include "Json/rapidjson/document.h"
#include "Json/rapidjson/document.h"
#include "Json/rapidjson/prettywriter.h"
#include "Json/rapidjson/filestream.h"
#include <cstdio>
#include <iostream>

using namespace std;

class LoginResponse : public ModelInterface
{
public:
	static const string modelName;

	string hashCode;
	LoginResponse(string hashCode);
	~LoginResponse();

	string toNetworkString();

	static LoginResponse* toModel(string msg);
private:


};

const string LoginResponse::modelName = "LoginResponse";

LoginResponse::LoginResponse(string hashCode)
{
	this->hashCode = hashCode;
}

LoginResponse::~LoginResponse()
{}

string LoginResponse::toNetworkString()
{
	rapidjson::Document document;
	document["modelName"] = LoginRequest::modelName;
	document["hashCode"] = this->hashCode;
	return document.GetString();
}

LoginResponse* LoginResponse::toModel(string msg)
{
	rapidjson::Document document;
	if (document.Parse<0>(msg.c_str()).HasParseError())
	{
		cout << "Json parsing error" << endl;
		return NULL;
	}
	else
	{
		if(document["modelName"].GetString() == LoginResponse::modelName)
		{
			string hashCode = document["hashCode"].GetString();
			return new LoginResponse(hashCode);
		}
		else
		{
			cout << "Unexpected model" << endl;
			return NULL;
		}
	}
}