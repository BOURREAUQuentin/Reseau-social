package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final int PORT = 5556;
    private static final String ADRESSE_IP = "localhost";
    private Socket socketClient;
    private BufferedReader lecteurClient;
    private PrintWriter ecriteurClient;
    private String pseudoClient;

    public Client(String pseudoClient){
        this.pseudoClient = pseudoClient;
        try{
            this.socketClient = new Socket(ADRESSE_IP, PORT);
            this.lecteurClient = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
            this.ecriteurClient = new PrintWriter(this.socketClient.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
