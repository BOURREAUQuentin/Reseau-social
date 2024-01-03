import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    public static void main(String[] args) {
        try {
            ServerSocket serveurSocket = new ServerSocket(12345);
            System.out.println("Le serveur est à l'écoute sur le port 12345");
            Socket clientSocket = serveurSocket.accept();
            System.out.println("Client connecté au serveur !");
            PrintWriter reponseServeur = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader lecteurClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while ((message = lecteurClient.readLine()) != null) {
                System.out.println("Message reçu du client : " + message);
                reponseServeur.println(message);
            }
            lecteurClient.close();
            reponseServeur.close();
            clientSocket.close();
            serveurSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
