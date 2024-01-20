import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final int PORT = 5556;
    private static final String ADRESSE_IP = "localhost";
    private Socket socketClient;
    private BufferedReader lecteurClient;
    private PrintWriter ecriteurClient;
    private String pseudoClient;
    private static List<String> ClientsAbonnements = new ArrayList<>(); // clients auxquels l'utilisateur est abonné
    private static List<String> ClientsAbonnes = new ArrayList<>(); // clients abonnés à cet utilisateur
    private static List<String> ClientsNonAbonnes = new ArrayList<>(); // clients auxquels l'utilisateur n'est pas encore abonné

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

    public String getPseudoClient() {
        return pseudoClient;
    }

    public List<String> getClientsAbonnements(){
        return ClientsAbonnements;
    }

    public List<String> getClientsAbonnes(){
        return ClientsAbonnes;
    }

    public List<String> getClientsNonAbonnes(){
        return ClientsNonAbonnes;
    }

    public void connexionServeur() {
        System.out.println("Vous êtes connecté au serveur");
        new Thread(() -> {
            try {
                String serveurMessage;
                while ((serveurMessage = this.lecteurClient.readLine()) != null) {
                    if (serveurMessage.contains(this.pseudoClient)){
                        PageAccueil.afficheMessage(serveurMessage);
                    }
                    for (String clientAbonne : ClientsAbonnements){
                        if (serveurMessage.contains(clientAbonne)){
                            PageAccueil.afficheMessage(serveurMessage);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        this.ecriteurClient.println(this.pseudoClient);
        System.out.println("Connecté en tant que : " + this.pseudoClient);
    }

    public void ajouterAbonnement(String pseudo){
        if (ClientsNonAbonnes.contains(pseudo)){
            ClientsNonAbonnes.remove(pseudo);
        }
        ClientsAbonnements.add(pseudo);
        System.out.println("Vous suivez : " + pseudo);
        envoyerMessage("/follow " + pseudo);
    }

    public void supprimerAbonnement(String pseudo){
        if (!ClientsNonAbonnes.contains(pseudo)){
            ClientsNonAbonnes.add(pseudo);
        }
        ClientsAbonnements.remove(pseudo);
        System.out.println("Vous ne suivez plus : " + pseudo);
        envoyerMessage("/unfollow " + pseudo);
    }

    public void envoyerMessage(String message) {
        this.ecriteurClient.println(message);
    }
}
