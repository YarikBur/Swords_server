package ru.swords.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
	
	public static final String USERS = "`swords_users`";
	
	private static final String url = "jdbc:mysql://localhost:5555/?autoReconnect=true&useSSL=false";
	private static final String user = "root";
	private static final String password = "141500qazwsX";
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	public Connect() {
		connect();
	}
	
	public void connect() {
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setSchema(String schema) {
		
		try {
			statement.execute("USE " + schema);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}
	
	public void setResult(String query) throws SQLException {
		resultSet = statement.executeQuery(query);
	}
	
	public void setUpdate(String query) throws SQLException {
		statement.executeUpdate(query);
	}
	
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public void disconnect() {
		
		try { if (connection != null) connection.close(); } catch (SQLException e) {  }
		try { if (statement != null) statement.close(); } catch (SQLException e) {  }
		try { if (resultSet != null) resultSet.close(); } catch (SQLException e) {  }
		
	}

}
