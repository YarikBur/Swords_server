package ru.swords.sql.query;

import java.sql.SQLException;
import java.util.Locale;

import ru.swords.sql.Connect;

public class Update {
	public static void setOnline (String username, boolean setOnline) throws SQLException {
		
		Connect connect = new Connect();
		connect.setSchema(Connect.USERS);
		
		int online = setOnline ? 1 : 0;
		String query = "UPDATE `login` SET `Online` = " + online + " WHERE `idLogin` = " + Select.getUserID(username);
		
		connect.setUpdate(query);
		
		connect.disconnect();
		
		System.out.println(String.format("Username '%s' is %s", username, setOnline ? "online" : "offline"));
		
	}
	
	public static void setPosition (String username, float x, float y, float z) throws SQLException {
		
		Connect connect = new Connect();
		connect.setSchema(Connect.USERS);
		
		String query = String.format(Locale.US, "UPDATE `position` SET `X` = %.3f, `Y` = %.3f, `Z` = %.3f WHERE `Username` = '%s'", x, y, z, username);
		
		connect.setUpdate(query);
		
		connect.disconnect();
		
	}
	
	public static void setRotation (String username, float x, float y, float z) throws SQLException {
		
		Connect connect = new Connect();
		connect.setSchema(Connect.USERS);
		
		String query = String.format(Locale.US, "UPDATE `rotation` SET `X` = %.3f, `Y` = %.3f, `Z` = %.3f WHERE `Username` = '%s'", x, y, z, username);
		
		connect.setUpdate(query);
		
		connect.disconnect();
		
	}
}
