#include "Server.h"
#include "ConnectionListener.h"


using namespace std;


int main()
{
	system("CLS");
	cout << "Gladiatus Server started" << endl;
	cout << "Type 'Exit' to close" << endl;

	//cout << "Port to listen to: ";
	int port = 7000;
	//cin >> port;
	//cin.ignore();

	Server* serv = new Server();
	serv->Start();
	ConnectionListener* cl = new ConnectionListener(port, serv);
	cl->Start();

	string input = "";
	while(input != "Exit")
	{
		cout << "> ";
		getline(cin, input);
	}

	cl->Close();
	serv->Close();
	cl->WaitUntillDone();
	serv->WaitUntillDone();
	delete cl;
	delete serv;

	return 0;
}