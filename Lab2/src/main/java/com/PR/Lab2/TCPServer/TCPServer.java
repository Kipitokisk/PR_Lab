package com.PR.Lab2.TCPServer;

import java.io.*;
import java.net.*;
import java.util.Random;

public class TCPServer {
    private static final int PORT = 12345;
    private static final String FILE_PATH = "sharedfile.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                // Accept new client connection and start a new thread for each client
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        private Random random = new Random();

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String command;
                System.out.println("Client " + socket.getInetAddress() + " is now in the handler thread.");
                while ((command = input.readLine()) != null) {
                    System.out.println("Received command: " + command);
                    if (command.startsWith("write ")) {
                        handleWriteCommand(command.substring(6));
                    } else if (command.equals("read")) {
                        handleReadCommand();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("Client disconnected: " + socket.getInetAddress());
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private synchronized void handleWriteCommand(String message) {
            try {
                // Simulate random sleep delay between 1 to 7 seconds
                Thread.sleep(1000 + random.nextInt(7000));  // Random sleep between 1 and 7 seconds

                // Write message to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                    System.out.println("Writing message: " + message); // Log the message being written
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                    output.println("Message written to file.");
                } catch (IOException e) {
                    e.printStackTrace();
                    output.println("Error writing to file.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                output.println("Error processing request (sleep interrupted).");
            }
        }

        private synchronized void handleReadCommand() {
            try {
                // Simulate random sleep delay between 1 to 7 seconds
                Thread.sleep(1000 + random.nextInt(7000));  // Random sleep between 1 and 7 seconds

                // Read content from the file
                try (BufferedReader fileReader = new BufferedReader(new FileReader(FILE_PATH))) {
                    String line;
                    output.println("File content:");
                    while ((line = fileReader.readLine()) != null) {
                        output.println(line);
                    }
                    output.println("End of file.");
                } catch (IOException e) {
                    e.printStackTrace();
                    output.println("Error reading from file.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                output.println("Error processing request (sleep interrupted).");
            }
        }
    }
}
