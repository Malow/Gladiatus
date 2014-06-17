#include "NetworkServer.h"

using namespace MaloW;

NetworkServer::NetworkServer(int port)
{
	this->port = port;
	this->stayAlive = true;

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

	// bind socket's name
	sockaddr_in saListen;
	saListen.sin_port = htons(port);
	saListen.sin_addr.s_addr = htonl(INADDR_ANY);
	saListen.sin_family = AF_INET;
	retCode = bind(this->sock, (sockaddr*)&saListen, sizeof(sockaddr));
	if(retCode == SOCKET_ERROR) 
	{
		this->stayAlive = false;
		MaloW::Debug("Failed to bind socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		WSACleanup();
	}
}

NetworkServer::~NetworkServer()
{
	this->stayAlive = false;
	this->Close();
	this->WaitUntillDone();
	// close server socket
	int retCode = 0;
	/*
	int retCode = closesocket(this->sock);
	if(retCode == SOCKET_ERROR) 
		MaloW::Debug("Failed to close socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
	// Release WinSock DLL*/

	retCode = WSACleanup();
	if(retCode == SOCKET_ERROR) 
		MaloW::Debug("Error cleaning up Winsock Library. Error: " + MaloW::convertNrToString(WSAGetLastError()));
}

ClientChannel* NetworkServer::ListenForNewClients()
{
	ClientChannel* cc = NULL;

	// listen
	int retCode = listen(this->sock, SOMAXCONN);
	if(retCode == SOCKET_ERROR)
	{
		this->stayAlive = false;
		MaloW::Debug("Failed to Listen for new connections. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		WSACleanup();
	}

	if(!this->stayAlive)
		return NULL;
	
	// connect
	sockaddr_in saClient = {0};
	int nSALen = sizeof( sockaddr );
	SOCKET hClient = accept(this->sock, (sockaddr*)&saClient, &nSALen);
	if(hClient == INVALID_SOCKET) 
	{
		this->stayAlive = false;
		MaloW::Debug("Invalid client socket, connection failed. Error: " + MaloW::convertNrToString(WSAGetLastError()));
		WSACleanup();
	}
	else
	{
		cc = new ClientChannel(hClient);
	}
	return cc;
}

void NetworkServer::Life()
{
	while(this->stayAlive)
	{
		ClientChannel* cc = this->ListenForNewClients();
		if(cc && this->stayAlive)
			this->ClientConnected(cc);
	}
}

void NetworkServer::CloseSpecific()
{
	int retCode = shutdown(this->sock, SD_BOTH);
	if(retCode == SOCKET_ERROR) 
		MaloW::Debug("Error trying to perform shutdown on socket from a ->Close() call from another thread.");

	retCode = closesocket(this->sock);
	if(retCode == SOCKET_ERROR) 
		MaloW::Debug("Failed to close socket. Error: " + MaloW::convertNrToString(WSAGetLastError()));
}