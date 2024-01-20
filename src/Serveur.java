package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Serveur {
    private static final int PORT = 5556;
    private static Map<String, PrintWriter> lesClientsConnectes = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serveurSocket = new ServerSocket(PORT);
            System.out.println("En attente de clients...");

            while (true) {
                Socket clientSocket = serveurSocket.accept();
                new ClientHandler(clientSocket, lesClientsConnectes).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
