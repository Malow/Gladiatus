package com.malow.gladiatusserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import malow.gladiatus.common.models.Ability;

public class Abilities 
{
	public static Map<Integer, Ability> abilities;
	public static List<Ability> basicAbilities;

	public static void LoadFromDatabase() throws Exception
	{
		abilities = new HashMap<Integer, Ability>();
		basicAbilities = new ArrayList<Ability>();
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");
		  
		PreparedStatement accountStatement = connect.prepareStatement("SELECT * FROM Abilities; ");
		ResultSet accountResult = accountStatement.executeQuery();
		
		while (accountResult.next()) 
		{
			int abilityId = accountResult.getInt("id");
			String name = accountResult.getString("name");
			String description = accountResult.getString("description");
			String tags = accountResult.getString("tags");
			int tier = accountResult.getInt("tier");
			
			Ability a = new Ability(abilityId, name, description, tags, tier);
			
			abilities.put(abilityId, a);
			
			if(abilityId == 1)
			{
				basicAbilities.add(a);
			}
		}
	}
	
	public static List<Ability> GetBasicAbilities()
	{
		return basicAbilities;
	}
	
	public static Ability GetAbilityById(int id)
	{
		return abilities.get(id);
	}

}
