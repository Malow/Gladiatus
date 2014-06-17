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



class LoginRequest : public ModelInterface
{
public:
	static const string modelName;

	string username;
	string password;
	LoginRequest(string username, string password);
	~LoginRequest();

	string toNetworkString();

	static LoginRequest* toModel(string msg);
private:
	

};

const string LoginRequest::modelName = "LoginRequest";

LoginRequest::LoginRequest(string username, string password)
{
	this->username = username;
	this->password = password;
}

LoginRequest::~LoginRequest()
{}

string LoginRequest::toNetworkString()
{
	rapidjson::Document document;
	document["modelName"] = LoginRequest::modelName;
	document["username"] = this->username;
	document["password"] = this->password;
	return document.GetString();
}

LoginRequest* LoginRequest::toModel(string msg)
{
	rapidjson::Document document;
	if (document.Parse<0>(msg.c_str()).HasParseError())
	{
		cout << "Json parsing error" << endl;
		return NULL;
	}
	else
	{
		if(document["modelName"].GetString() == LoginRequest::modelName)
		{
			string username = document["username"].GetString();
			string password = document["password"].GetString();
			return new LoginRequest(username, password);
		}
		else
		{
			cout << "Unexpected model" << endl;
			return NULL;
		}
	}
}