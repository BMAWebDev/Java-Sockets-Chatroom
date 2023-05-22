package com.chatroom;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
	private final Socket clientSocket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private final Server server;

	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
		this.server = Server.initialize();
	}

	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			String message;
			while ((message = reader.readLine()) != null) {
				System.out.println("Client: " + message);
				server.broadcastMessage(message, this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void sendMessage(String message) {
		try {
			writer.write(message);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			reader.close();
			writer.close();
			clientSocket.close();
			server.removeClient(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

