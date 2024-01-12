import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientModele{

    private PrintWriter writer;
    private BufferedReader reader;
    private TextArea chatArea;
    
    public ClientModele(){
        this.reader = new BufferedReader();
        this.chatArea = new JTextArea();
    }

    public void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            appendToChatArea("Connecté au serveur !");
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public void appendToChatArea(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    public void readMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
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
