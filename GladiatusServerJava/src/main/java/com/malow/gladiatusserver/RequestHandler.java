package com.malow.gladiatusserver;

import com.malow.gladiatusserver.SQLConnector.NoCharacterFoundException;
import com.malow.gladiatusserver.SQLConnector.SessionExpiredException;

import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
import malow.gladiatus.common.models.responses.LoginResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.gladiatus.common.models.responses.SessionExpiredResponse;
import malow.malowlib.NetworkChannel;

public class RequestHandler 
{
	public static void handleLoginRequest(LoginRequest request, NetworkChannel sender)
	{
		String sessionId = "";
		String errorCode = "";
		try 
		{
			sessionId = SQLConnector.authenticateAccount(request.username, request.password);
		} 
		catch (Exception e) 
		{
			if(e instanceof SQLConnector.WrongPasswordException)
			{
				errorCode = "Wrong username/password.";
				System.out.println("Client " + sender.GetChannelID() + " failed login due to wrong password");
			}
			else
			{
				System.out.println("Client " + sender.GetChannelID() + " Unexpected login error.");
				errorCode = "Unexpected login error.";	
				e.printStackTrace();
			}
		}
		LoginResponse response = new LoginResponse(sessionId, errorCode);
		sender.SendData(response.toNetworkString());
	}
	
	public static void handleRegisterRequest(RegisterRequest request, NetworkChannel sender) 
	{
		String sessionId = "";
		String errorCode = "";
		try
		{
			sessionId = SQLConnector.registerAccount(request.username, request.password, request.email);
			
		}
		catch (Exception e)
		{
			if(e instanceof SQLConnector.UsernameTakenException)
			{
				errorCode = "Username is already taken.";
				System.out.println("Client " + sender.GetChannelID() + " failed login due to Username is already taken.");
			}
			else
			{
				System.out.println("Client " + sender.GetChannelID() + " Unexpected registration error.");
				errorCode = "Unexpected registration error.";	
				e.printStackTrace();
			}
		}
		LoginResponse response = new LoginResponse(sessionId, errorCode);
		sender.SendData(response.toNetworkString());
	}
	
	
	public static void handleCharacterInfoRequest(CharacterInfoRequest request, NetworkChannel sender)
	{
		String sessionId = request.sessionId;
		ModelInterface response = null;
		try 
		{
			response = SQLConnector.getCharacterInfo(sessionId);
		} 
		catch (Exception e) 
		{
			if(e instanceof NoCharacterFoundException)
			{
				response = new NoCharacterFoundResponse();
				System.out.println("Client " + sender.GetChannelID() + " no character found.");
			}
			else if(e instanceof SessionExpiredException)
			{
				response = new SessionExpiredResponse();
				System.out.println("Client " + sender.GetChannelID() + " session expired");
			}
			else
			{
				System.out.println("Client " + sender.GetChannelID() + " Unexpected character info exception: " + e);
				e.printStackTrace();
			}
		}
		sender.SendData(response.toNetworkString());
	}
}
