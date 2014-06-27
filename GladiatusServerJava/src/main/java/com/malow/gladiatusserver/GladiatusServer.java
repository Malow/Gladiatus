package com.malow.gladiatusserver;

import java.util.Scanner;

public class GladiatusServer 
{
	public static void main(String [] args)
	{
		System.out.println("Gladiatus Server started");
		System.out.println("Type 'Exit' to close");

		//cout << "Port to listen to: ";
		int port = 7000;
		//cin >> port;	// nope, config file
		//cin.ignore();

		Server serv = new Server();
		serv.Start();
		ConnectionListener cl = new ConnectionListener(port, serv);
		cl.Start();
		
		LoadFromDatabase();

		String input = "";
		Scanner in = new Scanner(System.in);
		while(input != "Exit")
		{
			System.out.print("> ");
			input = in.next();
			
			if(input.equals("help"))
				PrintHelp();
			
			if(input.equals("reload:db"))
				LoadFromDatabase();
		}
		
		in.close();
		cl.Close();
		serv.Close();
		cl.WaitUntillDone();
		serv.WaitUntillDone();
		cl = null;
		serv = null;
	}
	
	public static void LoadFromDatabase()
	{
		try {
			Abilities.LoadFromDatabase();
		} catch (Exception e) {
			System.out.println("Error reading Abilities from database.");
			e.printStackTrace();
		}
	}
	
	public static void PrintHelp()
	{
		System.out.println("Commands:");
		System.out.println("help - Displays this help.");
		System.out.println("reload db - Reloads static information from database.");
	}
}
