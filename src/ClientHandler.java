package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import modele.java.Client;
import modele.java.Message;

public class ClientHandler extends Thread {
    private String nomUtilisateurClient;
    private Socket socketClient;
    private BufferedReader lecteurClient;
    private PrintWriter ecriteurClient;
    private Map<String, PrintWriter> lesClientsConnectes;

    public ClientHandler(Socket socketClient, Map<String, PrintWriter> lesClientsConnectes){
        this.socketClient = socketClient;
        this.lesClientsConnectes = lesClientsConnectes;
        try{
            this.lecteurClient = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            this.ecriteurClient = new PrintWriter(socketClient.getOutputStream());
            this.nomUtilisateurClient = this.lecteurClient.readLine();
            this.lesClientsConnectes.put(this.nomUtilisateurClient, this.ecriteurClient);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(this.nomUtilisateurClient + " viens de se connecter");
    }

    @Override
    public void run(){
        try {
            this.lecteurClient = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
            this.ecriteurClient = new PrintWriter(this.socketClient.getOutputStream());
            String message;
            while ((message = this.lecteurClient.readLine()) != null) {
                this.broadcast(message);
            }
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
