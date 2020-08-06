package ru.swords;

import java.sql.SQLException;

import ru.swords.sql.query.Insert;
import ru.swords.sql.query.Select;
import ru.swords.sql.query.Update;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			Insert.newUser("TestUser", "bbq");
			Update.setPosition("Asfick", 0.5f, 0f, 2f);
			Update.setRotation("Asfick", 15f, 0f, 0f);
			
			float[] position = Select.getPosition("Asfick");
			if (position != null)
				System.out.println(position[0] + "  " + position[1] + "  " + position[2]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
