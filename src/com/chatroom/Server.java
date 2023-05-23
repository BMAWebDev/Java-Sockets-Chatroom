package com.chatroom;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private ServerSocket serverSocket;
	private List<ClientHandler> clients;
	private static Server server; // Singleton

	private Server(){}

	public static Server initialize(){
		if(server == null)
			server = new Server();

		return server;
	}

	public void startServer() {
		clients = new ArrayList<>();
		int port = 12345;

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Chatroom server started on port " + port);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

				ClientHandler clientHandler = new ClientHandler(clientSocket);
				clients.add(clientHandler);
				clientHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeServer();
		}
	}

	private void closeServer() {
		try {
			// close all clients
			for (ClientHandler client : clients) {
				client.close();
			}

			// and then close the server
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void removeClient(ClientHandler client) {
		clients.remove(client);
	}

	protected void broadcastMessage(String message, ClientHandler sender) {
		for (ClientHandler client : clients) {
			client.sendMessage(message);
		}
	}
}