package com.malow.gladiatusserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import malow.gladiatus.common.models.responses.CharacterInfoResponse;

public class SQLConnector 
{	
	public static class WrongPasswordException extends Exception
	{
		private static final long serialVersionUID = 1L;
	}
	
	public static class SessionExpiredException extends Exception
	{
		private static final long serialVersionUID = 2L;
	}
	
	public static class NoCharacterFoundException extends Exception
	{
		private static final long serialVersionUID = 3L;
	}
	  
	public static String authenticateAccount(String username, String password) throws Exception
	{
		String sessionId = "";
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");
		  
		PreparedStatement accountStatement = connect.prepareStatement("SELECT * FROM Accounts WHERE username = ? ; ");
		accountStatement.setString(1, username);
		ResultSet accountResult = accountStatement.executeQuery();
		
		if (accountResult.next()) 
		{
			String dbpw = accountResult.getString("password");
			if(dbpw.equals(password))
			{
				sessionId = UUID.randomUUID().toString();
				PreparedStatement setSessionStatement = connect.prepareStatement("UPDATE Accounts SET session_id = ? WHERE username = ? ; ");
				setSessionStatement.setString(1, sessionId);
				setSessionStatement.setString(2, username);
				setSessionStatement.executeUpdate();
				setSessionStatement.close();
			}
			else
			{
				throw new WrongPasswordException();
			}
		}
		else
		{
			PreparedStatement newAccountStatement = connect.prepareStatement("insert into Gladiatus.Accounts values (default, ?, ?, NULL);");
			newAccountStatement.setString(1, username);
			newAccountStatement.setString(2, password);
			newAccountStatement.executeUpdate();
			newAccountStatement.close();
			
			sessionId = UUID.randomUUID().toString();
			PreparedStatement setSessionStatement = connect.prepareStatement("UPDATE Accounts SET session_id = ? WHERE username = ? ; ");
			setSessionStatement.setString(1, sessionId);
			setSessionStatement.setString(2, username);
			setSessionStatement.executeUpdate();
			setSessionStatement.close();
		}
		 
		accountStatement.close();
		connect.close();
		accountResult.close();
		  
		return sessionId;
	}
	
	public static CharacterInfoResponse getCharacterInfo(String sessionId) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");
		CharacterInfoResponse response = null;
		
		PreparedStatement accountStatement = connect.prepareStatement("SELECT * FROM Accounts WHERE session_id = ? ; ");
		accountStatement.setString(1, sessionId);
		ResultSet accountResult = accountStatement.executeQuery();
		
		
		int accountId = -1;
		
		if (accountResult.next()) 
		{
			accountId = accountResult.getInt("id");
			PreparedStatement characterStatement = connect.prepareStatement("SELECT * FROM Characters WHERE account_id = ? ; ");
			characterStatement.setInt(1, accountId);
			ResultSet characterResult = characterStatement.executeQuery();
			
			
			if(characterResult.next())
			{
				String characterName = characterResult.getString("character_name");
				String characterImage = characterResult.getString("character_image");
				String health = Float.toString(characterResult.getFloat("health"));
				String armor = Float.toString(characterResult.getFloat("armor"));
				String strength = Float.toString(characterResult.getFloat("strength"));
				String dexterity = Float.toString(characterResult.getFloat("dexterity"));
				String initiative = Float.toString(characterResult.getFloat("initiative"));
				
				response = new CharacterInfoResponse(characterName, characterImage, health, armor, strength, dexterity, initiative);
			}
			else
			{
				throw new NoCharacterFoundException();
			}
			
			accountStatement.close();
			characterResult.close();
		}
		else
		{ 
			throw new SessionExpiredException();
		}		
		
		accountStatement.close();
		connect.close();
		accountResult.close();
		
		return response;
	}
} 