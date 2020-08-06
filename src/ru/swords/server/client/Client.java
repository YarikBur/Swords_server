package ru.swords.server.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import ru.swords.server.Main;

public class Client extends Thread {
	
	private boolean closedConnection = false;
	private Socket socket;
	public String username = null;
	public String password = null;
	public boolean authorized = false;
	private String dublicateSocket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	public Client(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
		this.socket = socket;
		dublicateSocket = socket.toString();
		this.dataInputStream = dataInputStream;
		this.dataOutputStream = dataOutputStream;
	}
	
	private void initUsername() {
		username = read();
		if (username.equals("EXIT"))
			return;
		
		System.out.println(socket + " name: " + username);
		
		password = read();
		if (password.equals("EXIT"))
			return;
		
		System.out.println(username + " password: " + password);
		
		authorized = QueryToDB.authorization(username, password);
		
		if (authorized)
			write("Authorization was successful");
		else
			write("Wrong login or password");
	}
	
	private String read() {
		String input = "";
		
		try {
			byte[] lenBytes = new byte[4];
			dataInputStream.read(lenBytes, 0, 4);
			int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) | ((lenBytes[1] & 0xff << 8) | lenBytes[0] & 0xff));
			byte[] rec = new byte[len];
			dataInputStream.read(rec, 0, len);
			
			input = new String(rec, 0, len);
		} catch (IOException e) {
			System.err.println("Error on client (" + dublicateSocket + ") in read: " + e.getMessage());
		}
		
		
		return input;
	}
	
	public void write(String text) {
		byte[] toSendBytes = text.getBytes();
		int toSendLen = toSendBytes.length;
		byte[] toSendLenBytes = new byte[4];
		
		for (int i = 0; i < 4; i++) {
			toSendLenBytes[i] = (byte)((toSendLen >> (i*8)) & 0xff);
		}
		
		try {
			dataOutputStream.write(toSendLenBytes);
			dataOutputStream.write(toSendBytes);
		} catch (IOException e) {
			System.err.println("Error on client (" + dublicateSocket + ") in write: " + e.getMessage());
		}
		
	}
	
	@Override
	public void run() {
		loop();
		exit();
	}
	
	private void loop() {
		while (!closedConnection) {
			
			if (!authorized) {
				initUsername();
				if (username.equals("EXIT") || password.equals("EXIT"))
					closedConnection = true;
			}else {
				String input = read();
				
				switch (input) {
				case "CONNECTION":
					QueryToDB.sendInfoPlayer(this);
					break;
				case "CONNECTED":
					QueryToDB.setOnline(username);
					Main.sendInfoAllUsers(username);
					break;
				case "EXIT":
					QueryToDB.setOffline(username);
					closedConnection = true;
					break;
				case "POSITION":
					String position = read();
					System.out.println(String.format("%s position: %s", username, position));
					QueryToDB.setPosition(username, position);
					break;
				case "ROTATION":
					String rotation = read();
					System.out.println(String.format("%s rotation: %s", username, rotation));
					QueryToDB.setRotation(username, rotation);
					
					break;
	
				default:
					System.out.println("unknown command: " + input);
					break;
				}
			}
		}
	}
	
	private void exit() {
		try {
			
			socket.close();
			dataInputStream.close();
			dataOutputStream.close();
			Main.removeClient(this);
			System.out.println(socket + " disconnect");
			
		} catch (IOException e) {
			System.err.println("Error on client (" + dublicateSocket + ") for close connection: " + e.getMessage());
		}
		
	}
}
