package com.chatroom;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
	private BufferedReader reader;
	private BufferedWriter writer;
	private final JTextArea chatArea;
	private final JTextField messageField;

	public Client() {
		setTitle("Chatroom Client");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Panel for chat history
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		chatPanel.add(scrollPane, BorderLayout.CENTER);

		// Panel for message input
		JPanel messagePanel = new JPanel(new BorderLayout());
		messageField = new JTextField();
		messageField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		messagePanel.add(messageField, BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);

		// Add panels to the frame
		add(chatPanel, BorderLayout.CENTER);
		add(messagePanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	public void connect(String serverAddress, int serverPort) {
		try {
			Socket clientSocket = new Socket(serverAddress, serverPort);
			System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			// Start a separate thread for reading incoming messages from the server
			Thread incomingThread = new Thread(() -> {
				try {
					String message;
					while ((message = reader.readLine()) != null) {
						chatArea.append("Server: " + message + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			incomingThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage() {
		String message = messageField.getText();
		if (!message.isEmpty()) {
			try {
				writer.write(message);
				writer.newLine();
				writer.flush();
				messageField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Client clientGUI = new Client();
			clientGUI.connect("localhost", 12345);
		});
	}
}
