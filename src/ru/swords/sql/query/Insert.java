package ru.swords.sql.query;

import java.sql.SQLException;

import ru.swords.sql.Connect;

public class Insert {
	
	public static void newUser(String username, String password) throws SQLException {
		
		Connect connect = new Connect();
		connect.setSchema(Connect.USERS);
		
		String query = String.format("INSERT INTO `login` (`Username`, `Password`) VALUES ('%s', SHA1('%s'))", username, password);
		
		if (Select.getUserID(username) == 0) {
			connect.setUpdate(query);
			System.out.println(String.format("User - %s is created", username));
		} else
			System.out.println(String.format("User - %s is not created", username));
		
		connect.disconnect();
		
	}
	
}
