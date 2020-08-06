package ru.swords.sql.query;

import java.sql.SQLException;
import java.util.ArrayList;

import ru.swords.sql.Connect;

public class Select {
	
	private static final String TEMPLATE_POSITION_ROTATION = "SELECT `X`, `Y`, `Z` FROM `%s` WHERE `Username` = '%s'";
	
	/**
	 * Возвращаяет список всех игроков, которые в данный момент Online
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<String> AllOnline() throws SQLException {
		
		Connect connect = new Connect();
		connect.setSchema(Connect.USERS);
		
		String query = "SELECT `Username` FROM `login` WHERE `Online` = 1";
		String template = "Online: %s";
		
		connect.setResult(query);
		
		ArrayList<String> online = new ArrayList<String>();
		
		while (connect.getResultSet().next()) {
			online.add(connect.getResultSet().getString(1));
			System.out.println(String.format(template, connect.getResultSet().getString(1)));
		}
		
		connect.disconnect();
		
		if (online.isEmpty()) {
			System.out.println("All users is Offline...");
			return null;
		}
		
		return online;
		
	}
	
	/**
	 * Возвращает ID игрока
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public static int getUserID(String username) throws SQLException {
		
		Connect connect = new Connect();
		connect.setSchema(Connect.USERS);
		
		String query = "SELECT `idLogin` FROM `login` WHERE `Username` = '" + username + "'";
		
		connect.setResult(query);
		
		int id = 0;
		
		while (connect.getResultSet().next()) {
			id = connect.getResultSet().getInt(1);
		}
		
		connect.disconnect();
		
		return id;
		
	}
	
	/**
	 * Возвращает позицию игрока в массиве float[x, y, z]
	 * @param username
	 * @return [x, y, z]
	 * @throws SQLException
	 */
	public static float[] getPosition(String username) throws SQLException {
		if (getUserID(username) != 0) {
			float[] position = new float[3];
			
			Connect connect = new Connect();
			connect.setSchema(Connect.USERS);
			
			String query = String.format(TEMPLATE_POSITION_ROTATION,"position", username);
			
			connect.setResult(query);
			
			while (connect.getResultSet().next()) {
				for (int i = 0; i < 3; i++)
					position[i] = connect.getResultSet().getFloat( (i + 1) );
			}
			
			return position;
		} else
			return null;
	}
	
	/**
	 * Возвращает поворот игрока в массиве float[x, y, z]
	 * @param username
	 * @return [x, y, z]
	 * @throws SQLException
	 */
	public static float[] getRotation(String username) throws SQLException {
		if (getUserID(username) != 0) {
			float[] rotation = new float[3];
			
			Connect connect = new Connect();
			connect.setSchema(Connect.USERS);
			
			String query = String.format(TEMPLATE_POSITION_ROTATION,"rotation", username);
			
			connect.setResult(query);
			
			while (connect.getResultSet().next()) {
				for (int i = 0; i < 3; i++)
					rotation[i] = connect.getResultSet().getFloat( (i + 1) );
			}
			
			return rotation;
		} else
			return null;
	}
	
	public static boolean getPlayer(String username, String password) throws SQLException {
		boolean output = false;
		
		Connect connect = new Connect();
		connect.setSchema(Connect.USERS);
		
		String query = String.format("SELECT `Username` FROM `login` WHERE `Username`='%s' AND `Password`=SHA1('%s')", username, password);
		
		connect.setResult(query);
		
		while (connect.getResultSet().next()) {
			if (connect.getResultSet().getString(1).equals(username)) {
				output = true;
			}
		}
		
		connect.disconnect();
		
		return output;
	}
	
}
