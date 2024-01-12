import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextArea;

public class ClientModele{

    private PrintWriter writer;
    private BufferedReader reader;
    private ClientFX vueClient;
    private Set<Client> connectedClients = new HashSet<>();
    
    public ClientModele(ClientFX vueClient){
        Socket socket = new Socket("localhost", 12345);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.vueClient = vueClient;
    }

    public void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            appendToChatArea("Connecté au serveur !");
        } catch (IOException e) {
            e.printStackTrace();
            this.vueClient.exit();
        }
    }

    public void appendToChatArea(String message) {
        String m = message + "\n";
        this.vueClient.runLater(m);
    }

    public void sendMessage() {
        String message = this.vueClient.getMessageField();
        if (!message.isEmpty()) {
            this.writer.println(message);
            this.vueClient.clearFielMessage();
        }
    }

    public void readMessages() {
        try {
            String message;
            while ((message = this.reader.readLine()) != null) {
                appendToChatArea("Serveur: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void closeConnection() {
        try {
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
