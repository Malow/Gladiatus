package com.malow.gladiatusserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import malow.gladiatus.common.models.Ability;
import malow.gladiatus.common.models.ModelInterface;
import malow.gladiatus.common.models.requests.CharacterCreateRequest;
import malow.gladiatus.common.models.responses.BasicAbilitiesResponse;
import malow.gladiatus.common.models.responses.CharacterCreationSuccessfulResponse;
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
	
	public static class UsernameTakenException extends Exception
	{
		private static final long serialVersionUID = 4L;
	}
	
	public static class CharacternameTakenException extends Exception
	{
		private static final long serialVersionUID = 5L;
	}
	
	public static class AccountAlreadyHasACharacterException extends Exception
	{
		private static final long serialVersionUID = 6L;
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
			System.out.println("No such account: " + username);
			throw new WrongPasswordException();
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
		
		int accountId = getAccountId(sessionId);
		
		PreparedStatement characterStatement = connect.prepareStatement("SELECT * FROM Characters WHERE account_id = ? ; ");
		characterStatement.setInt(1, accountId);
		ResultSet characterResult = characterStatement.executeQuery();
		
		if(characterResult.next())
		{
			String characterName = characterResult.getString("character_name");
			String characterImage = characterResult.getString("character_image");
			String health = Float.toString(characterResult.getFloat("health"));
			String strength = Float.toString(characterResult.getFloat("strength"));
			String dexterity = Float.toString(characterResult.getFloat("dexterity"));
			
			response = new CharacterInfoResponse(characterName, characterImage, health, "0", strength, dexterity, "0");
		}
		else
		{
			throw new NoCharacterFoundException();
		}
		
		characterStatement.close();
		characterResult.close();
		connect.close();
		
		return response;
	}

	public static String registerAccount(String username, String password, String email) throws Exception
	{
		String ret = null;
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");

		PreparedStatement accountStatement = connect.prepareStatement("SELECT * FROM Accounts WHERE username = ? ; ");
		accountStatement.setString(1, username);
		ResultSet accountResult = accountStatement.executeQuery();
		
		if (accountResult.next())
		{
			throw new UsernameTakenException();
		}
		
		accountStatement.close();
		accountResult.close();
		
		
		String sessionId = UUID.randomUUID().toString();
		PreparedStatement newAccountStatement = connect.prepareStatement("insert into Gladiatus.Accounts values (default, ?, ?, ?, ?);");
		newAccountStatement.setString(1, username);
		newAccountStatement.setString(2, password);
		newAccountStatement.setString(3, email);
		newAccountStatement.setString(4, sessionId);
		int rowCount = newAccountStatement.executeUpdate();
		newAccountStatement.close();
		
		if(rowCount == 1)
		{
			ret = sessionId;
		}
		else if(rowCount == 0)
		{
			throw new Exception();
		}
		else
		{
			System.out.println("CRITICAL ERROR, MULTIPLE LINES ADDED ON ACCOUNT CREATE!");
			throw new Exception();
		}
		
		connect.close();
		return ret;
	}

	public static BasicAbilitiesResponse getBasicAbilities() throws Exception
	{
		List<Ability> abilities = new ArrayList<Ability>();
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");
		  
		PreparedStatement accountStatement = connect.prepareStatement("SELECT * FROM Basic_abilities; ");
		ResultSet accountResult = accountStatement.executeQuery();
		
		while (accountResult.next()) 
		{
			int abilityId = accountResult.getInt("id");
			String name = accountResult.getString("name");
			String description = accountResult.getString("description");
			String tags = accountResult.getString("tags");
			
			abilities.add(new Ability(abilityId, name, description, tags));
		}
		
		return new BasicAbilitiesResponse(abilities);
	}

	public static ModelInterface createCharacter(CharacterCreateRequest request) throws Exception 
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");

		if(characterExists(request.characterName))
		{
			throw new CharacternameTakenException();
		}		
		
		int accountId = getAccountId(request.sessionId);
		
		if(accountHasCharacter(accountId))
		{
			throw new AccountAlreadyHasACharacterException();
		}
		
		String abilitiesAsString = createStringFromAbilityList(request.abilities);
				
		PreparedStatement newCharacterStatement = connect.prepareStatement("insert into Gladiatus.Characters values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		newCharacterStatement.setInt(1, accountId);
		newCharacterStatement.setString(2, request.characterName);
		newCharacterStatement.setString(3, request.characterImage);
		newCharacterStatement.setFloat(4, request.stats.health);
		newCharacterStatement.setFloat(5, request.stats.strength);
		newCharacterStatement.setFloat(6, request.stats.dexterity);
		newCharacterStatement.setFloat(7, request.stats.intelligence);
		newCharacterStatement.setFloat(8, request.stats.willpower);
		newCharacterStatement.setInt(9, Constants.STARTING_MONEY);
		newCharacterStatement.setString(10, abilitiesAsString);
		int rowCount = newCharacterStatement.executeUpdate();
		newCharacterStatement.close();
		
		if(rowCount != 1)
		{
			throw new Exception();
		}
		
		connect.close();
		return new CharacterCreationSuccessfulResponse();
	}
	
	
	
	
	private static String createStringFromAbilityList(List<Integer> abilities)
	{
		String ret = "";
		for(int abilityId: abilities)
		{
			ret += abilityId + ",";
		}
		return ret;
	}
	
//	private static List<Integer> createAbilityListFromString(String abilities)
//	{
//		
//	}
	
	
	
	public static boolean characterExists(String characterName) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");

		PreparedStatement accountStatement = connect.prepareStatement("SELECT * FROM Characters WHERE character_name = ? ; ");
		accountStatement.setString(1, characterName);
		ResultSet accountResult = accountStatement.executeQuery();
		
		boolean ret = false;
		
		if (accountResult.next())
		{
			ret = true;
		}
		
		accountStatement.close();
		accountResult.close();
		connect.close();
		
		return ret;
	}
	
	public static int getAccountId(String sessionId) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");
		
		PreparedStatement accountStatement = connect.prepareStatement("SELECT * FROM Accounts WHERE session_id = ? ; ");
		accountStatement.setString(1, sessionId);
		ResultSet accountResult = accountStatement.executeQuery();
		
		int accountId = -1;
		
		if (accountResult.next()) 
		{
			accountId = accountResult.getInt("id");
		}
		else
		{ 
			throw new SessionExpiredException();
		}		
		
		accountStatement.close();
		connect.close();
		accountResult.close();
		
		return accountId;
	}
	
	public static boolean accountHasCharacter(int accountId) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");
		
		PreparedStatement characterStatement = connect.prepareStatement("SELECT * FROM Characters WHERE account_id = ? ; ");
		characterStatement.setInt(1, accountId);
		ResultSet characterResult = characterStatement.executeQuery();
		
		boolean ret = false;
		
		if(characterResult.next())
		{
			ret = true;
		}
		
		characterStatement.close();
		characterResult.close();
		connect.close();
		
		return ret;
	}
} 