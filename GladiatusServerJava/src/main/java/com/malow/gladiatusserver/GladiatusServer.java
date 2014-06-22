package com.malow.gladiatusserver;

import java.util.Scanner;

import malow.malowlib.RandomNumberGenerator;

public class GladiatusServer 
{
	public static void main(String [] args)
	{
		System.out.println("Gladiatus Server started");
		System.out.println("Type 'Exit' to close");

		//cout << "Port to listen to: ";
		int port = 7000;
		//cin >> port;
		//cin.ignore();

		Server serv = new Server();
		serv.Start();
		ConnectionListener cl = new ConnectionListener(port, serv);
		cl.Start();

		String input = "";
		Scanner in = new Scanner(System.in);
		while(input != "Exit")
		{
			System.out.print("> ");
			input = in.next();
		}
		
		in.close();
		cl.Close();
		serv.Close();
		cl.WaitUntillDone();
		serv.WaitUntillDone();
		cl = null;
		serv = null;
	}
}
