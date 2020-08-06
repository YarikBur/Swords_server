package ru.swords.server.client;

import java.sql.SQLException;

import ru.swords.sql.query.Select;
import ru.swords.sql.query.Update;

public class QueryToDB {
	public static void setPosition (String username, String input) {
		float[] postition = new float[3];
		
		for (int i = 0; i < 3; i++) {
			postition[i] = Float.parseFloat(input.replace(',', '.').split(";")[i]);
		}
		
		try {
			Update.setPosition(username, postition[0], postition[1], postition[2]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setRotation (String username, String input) {
		float[] rotation = new float[3];
		
		for (int i = 0; i < 3; i++) {
			rotation[i] = Float.parseFloat(input.replace(',', '.').split(";")[i]);
		}
		
		try {
			Update.setRotation(username, rotation[0], rotation[1], rotation[2]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendInfoPlayer(Client client) {
		{	//Write position for user
			client.write("POSITION");
			
			float[] position = new float[3];
			try {
				position = Select.getPosition(client.username);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			float x = position[0], y = position[1], z = position[2];
			
			client.write(String.format("%f;%f;%f", x, y, z));
		}
		{	//Write rotation for user
			client.write("ROTATION");
			
			float[] rotation = {0, 0, 0};
			try {
				rotation = Select.getPosition(client.username);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			float x = rotation[0], y = rotation[1], z = rotation[2];
			
			client.write(String.format("%f;%f;%f", x, y, z));
		}
	}
	
	public static boolean authorization (String username, String password) {
		boolean auth = false;
		
		try {
			auth = Select.getPlayer(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(username + " auth: " + auth);
		
		return auth;
	}
	
	public static void setOnline(String username) {
		try {
			Update.setOnline(username, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setOffline(String username) {
		try {
			Update.setOnline(username, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
