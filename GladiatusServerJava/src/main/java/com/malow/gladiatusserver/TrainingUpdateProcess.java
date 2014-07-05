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
				
		while(characterResult.next())
		{
			int characterId = characterResult.getInt("id");
			String currentlyTraining = characterResult.getString("currentlyTraining");
			float currentSkill = characterResult.getFloat(currentlyTraining);
			float increase = diff / Training.GetTimeToSkill(currentSkill);
			currentSkill += increase;
			
			PreparedStatement newCharacterStatement; 
			if(currentlyTraining.equals("health"))
				newCharacterStatement = connect.prepareStatement("UPDATE characters SET health = ? WHERE id = ?;");
			else if(currentlyTraining.equals("strength"))
				newCharacterStatement = connect.prepareStatement("UPDATE characters SET strength = ? WHERE id = ?;");
			else if(currentlyTraining.equals("dexterity"))
				newCharacterStatement = connect.prepareStatement("UPDATE characters SET dexterity = ? WHERE id = ?;");
			else if(currentlyTraining.equals("intelligence"))
				newCharacterStatement = connect.prepareStatement("UPDATE characters SET intelligence = ? WHERE id = ?;");
			else if(currentlyTraining.equals("willpower"))
				newCharacterStatement = connect.prepareStatement("UPDATE characters SET willpower = ? WHERE id = ?;");
			else
				throw new Exception("Wierd fucking error");
			
			newCharacterStatement.setFloat(1, currentSkill);
			newCharacterStatement.setInt(2, characterId);
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
		return diff;
	}
	
	private float GetTimeSinceLastDiff()
	{
		long currentTime = System.nanoTime();
		float diff = (currentTime - this.lastTime) / 1000000000.0f;
		return diff;
	}
}
