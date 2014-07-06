package com.malow.gladiatusserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import malow.gladiatus.common.Training;
import malow.malowlib.Process;

public class TrainingUpdateProcess extends Process
{
	private long lastTime;
	public float updateEveryXSeconds = 1.0f;
	public boolean logUpdates = false;
	public float diffMultiplier = 1.0f;
	
	public TrainingUpdateProcess()
	{
		this.lastTime = System.nanoTime();
	}
	
	@Override
	public void Life() 
	{
		while(this.stayAlive)
		{
			float diff = this.GetDiff();
			
			try 
			{
				UpdateTraining(diff);
			} 
			catch (Exception e1) 
			{
				System.out.println("Error: TrainingUpdate failed: ");
				e1.printStackTrace();
			}
			
			float timeElapsed = GetTimeSinceLastDiff();
			if(this.logUpdates)
				System.out.println("Finished updating training in " + timeElapsed + "ms.");
			
			try 
			{
				float sleepFor = (this.updateEveryXSeconds * 1000.0f) - timeElapsed * 1000.0f;
				if(sleepFor > 1.0f)
				{
					Thread.sleep((long) sleepFor);
				}
				else
				{
					System.out.println("Warning: Updating training took " + Math.abs(sleepFor) + "ms longer than target.");
				}
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
		}
	}	
	
	private void UpdateTraining(float diff) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/Gladiatus?" + "user=GladiatusServer&password=qqiuIUr348EW");
				
		PreparedStatement characterStatement = connect.prepareStatement("SELECT * FROM Characters WHERE currentlyTraining IS NOT NULL; ");
		ResultSet characterResult = characterStatement.executeQuery();

		float decay = diff / (86400.0f * (100.0f * (1.0f / Training.STAT_DECAY_PERCENT_PER_DAY)));
		
		// TODO: If diff for some reason should be really big so that increase or decay is bigger than 1.0 it can skip several levels.
		// This needs to be fixed if we count offline training, IE server being offline, doubt we will tho.
				
		while(characterResult.next())
		{
			int characterId = characterResult.getInt("id");
			
			float health = characterResult.getFloat("health");
			float strength = characterResult.getFloat("strength");
			float dexterity = characterResult.getFloat("dexterity");
			float intelligence = characterResult.getFloat("intelligence");
			float willpower = characterResult.getFloat("willpower");
			String currentlyTraining = characterResult.getString("currentlyTraining");
			
			if(health > 10.0f)
			{
				health -= decay;
				if(health < 10.0f)
					health = 10.0f;
			}
			if(strength > 10.0f)
			{
				strength -= decay;
				if(strength < 10.0f)
					strength = 10.0f;
			}			
			if(dexterity > 10.0f)
			{
				dexterity -= decay;
				if(dexterity < 10.0f)
					dexterity = 10.0f;
			}			
			if(intelligence > 10.0f)
			{
				intelligence -= decay;
				if(intelligence < 10.0f)
					intelligence = 10.0f;
			}			
			if(willpower > 10.0f)
			{
				willpower -= decay;
				if(willpower < 10.0f)
					willpower = 10.0f;
			}
			
			float currentSkill = characterResult.getFloat(currentlyTraining);
			float increase = diff / Training.GetTimeToSkill(currentSkill);
			currentSkill += increase;
						
			PreparedStatement newCharacterStatement = connect.prepareStatement("UPDATE characters SET health = ?, strength = ?, dexterity = ?, intelligence = ?, willpower = ? WHERE id = ?;");
			newCharacterStatement.setFloat(1, health);
			newCharacterStatement.setFloat(2, strength);
			newCharacterStatement.setFloat(3, dexterity);
			newCharacterStatement.setFloat(4, intelligence);
			newCharacterStatement.setFloat(5, willpower);
			newCharacterStatement.setInt(6, characterId);
			
			if(currentlyTraining.equals("health"))
				newCharacterStatement.setFloat(1, currentSkill);
			else if(currentlyTraining.equals("strength"))
				newCharacterStatement.setFloat(2, currentSkill);
			else if(currentlyTraining.equals("dexterity"))
				newCharacterStatement.setFloat(3, currentSkill);
			else if(currentlyTraining.equals("intelligence"))
				newCharacterStatement.setFloat(4, currentSkill);
			else if(currentlyTraining.equals("willpower"))
				newCharacterStatement.setFloat(5, currentSkill);
			else
				throw new Exception("Wierd fucking error");
			
			int rowCount = newCharacterStatement.executeUpdate();
			newCharacterStatement.close();
			if(rowCount != 1)
			{
				throw new Exception();
			}
		}
		
		characterStatement.close();
		characterResult.close();
		connect.close();
	}
	
	private float GetDiff()
	{
		long currentTime = System.nanoTime();
		float diff = (currentTime - this.lastTime) / 1000000000.0f;
		this.lastTime = currentTime;
		return diff * this.diffMultiplier;
	}
	
	private float GetTimeSinceLastDiff()
	{
		long currentTime = System.nanoTime();
		float diff = (currentTime - this.lastTime) / 1000000000.0f;
		return diff * this.diffMultiplier;
	}
}
