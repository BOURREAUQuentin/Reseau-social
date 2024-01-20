import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            boolean estConnecte = false;
            Socket socket = null;
            boolean messageAttenteAffiche = false;
            while (!estConnecte) {
                try {
                    socket = new Socket("localhost", 12345);
                    estConnecte = true;
                }
                catch (IOException e) {
                    if (!messageAttenteAffiche){
                        System.out.println("En attente du serveur...");
                        messageAttenteAffiche = true;
                    }
                }
            }
            System.out.println("Connecté au serveur !");
            BufferedReader reponseServeur = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter ecrivainClient = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader messageConsoleClient = new BufferedReader(new InputStreamReader(System.in));
            String clientInput;
            while ((clientInput = messageConsoleClient.readLine()) != null) {
                ecrivainClient.println(clientInput);
                System.out.println("Message reçu du serveur : " + reponseServeur.readLine());
            }
            reponseServeur.close();
            ecrivainClient.close();
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
