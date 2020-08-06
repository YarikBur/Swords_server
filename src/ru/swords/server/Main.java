package ru.swords.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ru.swords.server.client.Client;

public class Main extends Thread {
	
	private static final int PORT = 5586;
	private static ArrayList<Client> clients = new ArrayList<Client>();
	
	@Override
	public void run() {
		try {
			loop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		exit();
	}
	
	private void loop() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(PORT);
		
		System.out.println("Server started...");
		
		while(true) {
		//while(!exitLastClient) {
			
			Socket socket = null;
			
			try {
				
				socket = serverSocket.accept();
				
				System.out.println("New client: " + socket);
				
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				
				System.out.println("Create new thread for client: " + socket);
				
				Client client = new Client(socket, dataInputStream, dataOutputStream);
				clients.add(client);
				client.start();
				
			} catch (Exception e) {
				socket.close();
				System.err.println("Error: " + e.getMessage());
				break;
			}
			
		}
	}
	
	public static void removeClient(Client client) {
		clients.remove(client);
	}
	
	public static void writeAllClients(String sender, String text) {
		for (Client client : clients) {
			if (!client.username.equals(sender)) {
				client.write(text);
				System.out.println("Writed to " + client.username + ": " + text);
			}
		}
	}
	
	public static void sendInfoAllUsers(String username) {
		for (Client client : clients) {
			if (!client.username.equals(username)) {
				client.write("");
			}
		}
	}
	
	private void exit() {
		System.out.println("Server stoped...");
	}
	
	public static void main(String[] args) {
		new Thread(new Main()).start();
	}
}
