package com.malow.gladiatusserver;

import java.util.ArrayList;

import com.malow.gladiatusserver.SQLConnector.AccountAlreadyHasACharacterException;
import com.malow.gladiatusserver.SQLConnector.CharacternameTakenException;
import com.malow.gladiatusserver.SQLConnector.NoCharacterFoundException;
import com.malow.gladiatusserver.SQLConnector.SessionExpiredException;

import malow.gladiatus.common.models.Ability;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.BasicAbilitiesRequest;
import malow.gladiatus.common.models.requests.CharacterCreateRequest;
import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
import malow.gladiatus.common.models.responses.BasicAbilitiesResponse;
import malow.gladiatus.common.models.responses.CharacterCreationFailedResponse;
import malow.gladiatus.common.models.responses.LoginResponse;
import malow.gladiatus.common.models.responses.NoCharacterFoundResponse;
import malow.gladiatus.common.models.responses.SessionExpiredResponse;
import malow.gladiatus.common.models.responses.SomethingWentHorriblyWrongResponse;
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
				System.out.println("Client " + (sender.GetChannelID() + 1) + " failed login due to wrong password");
			}
			else
			{
				System.out.println("Client " + (sender.GetChannelID() + 1) + " Unexpected login error.");
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
				System.out.println("Client " + (sender.GetChannelID() + 1) + " failed login due to Username is already taken.");
			}
			else
			{
				System.out.println("Client " + (sender.GetChannelID() + 1) + " Unexpected registration error.");
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
				System.out.println("Client " + (sender.GetChannelID() + 1) + " no character found.");
			}
			else if(e instanceof SessionExpiredException)
			{
				response = new SessionExpiredResponse();
				System.out.println("Client " + (sender.GetChannelID() + 1) + " session expired");
			}
			else
			{
				response = new SomethingWentHorriblyWrongResponse();
				System.out.println("Client " + (sender.GetChannelID() + 1) + " Unexpected character info exception: " + e);
				e.printStackTrace();
			}
		}
		sender.SendData(response.toNetworkString());
	}

	public static void handleBasicAbilitiesRequest(BasicAbilitiesRequest request, NetworkChannel sender) 
	{
		//String sessionId = request.sessionId;
		ModelInterface response = null;
		try
		{
			response = SQLConnector.getBasicAbilities();
		}
		catch (Exception e)
		{
			System.out.println("Client " + (sender.GetChannelID() + 1) + " Unexpected basic abilities error: " + e);
			e.printStackTrace();
			response = new BasicAbilitiesResponse(new ArrayList<Ability>());
		}
		sender.SendData(response.toNetworkString());
	}

	public static void handleCharacterCreateRequest(CharacterCreateRequest request, NetworkChannel sender) 
	{
		ModelInterface response = null;
		if(!CharacterCreationValidator.ValidateRequest(request))
		{
			response = new CharacterCreationFailedResponse("Validation of request failed.");
			sender.SendData(response.toNetworkString());
			return;
		}
		
		try
		{
			response = SQLConnector.createCharacter(request);
		}
		catch (Exception e)
		{
			if(e instanceof CharacternameTakenException)
			{
				System.out.println("Client " + (sender.GetChannelID() + 1) + " Character creation failed. charactername taken taken: " + e);
				response = new CharacterCreationFailedResponse("Character name already taken.");
			}
			else if(e instanceof AccountAlreadyHasACharacterException)
			{
				System.out.println("Client " + (sender.GetChannelID() + 1) + " Character creation failed. account already has a character: " + e);
				e.printStackTrace();
				response = new CharacterCreationFailedResponse("Account already has a character.");
			}
			else
			{
				System.out.println("Client " + (sender.GetChannelID() + 1) + " Character creation failed: " + e);
				e.printStackTrace();
				response = new CharacterCreationFailedResponse("Unexpected error.");
			}
		}
		sender.SendData(response.toNetworkString());
		
	}
}
