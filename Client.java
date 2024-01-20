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
        List<Utilisateur> liste;
        try {
            liste = AbonneSQL.getUtilisateurAbonnes(pseudoClient);
            for (Utilisateur utilisateur : liste) {
                System.out.println("amis : "+utilisateur.getNomUtilisateur());
                ClientsAbonnements.add(utilisateur.getNomUtilisateur());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Utilisateur> liste2;
        try {
            liste2 = AbonneSQL.getUtilisateurAbonnes(pseudoClient);
            for (Utilisateur utilisateur : liste2) {
                ClientsAbonnes.add(utilisateur.getNomUtilisateur());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Utilisateur> liste3;
        try {
            liste3 = AbonneSQL.getUtilisateurNonAbonnes(pseudoClient);
            for (Utilisateur utilisateur : liste3) {
                ClientsNonAbonnes.add(utilisateur.getNomUtilisateur());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public int getIdUser() throws ClassNotFoundException{
        return UtilisateurSQL.getUtilisateurParNomUtilisateur(pseudoClient).getIdUtilisateur();
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
                    if (serveurMessage.equals("Votre compte a été supprimé. La connexion sera fermée.")){
                        PageAccueil.afficherPopUpBannissement();
                    }
                    else if (serveurMessage.contains("///likeDislike")){ // permet de mettre a jour le nombre de like et dislike du message concerné
                        PageAccueil.afficheMessage(serveurMessage);
                    }
                    else if (serveurMessage.contains("///SUPPRIMER")){ // permet de supprimer le message concerné
                        PageAccueil.supprimerMessageEtMettreAjourAffichage(serveurMessage);
                    }
                    else{
                        for (String s : ClientsAbonnements){
                            if (serveurMessage.contains(s)){
                                PageAccueil.afficheMessage(serveurMessage);
                            }
                        }
                        if (serveurMessage.contains(pseudoClient)){
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

    public void likeMessage(String date,String pseudo, String contenu) throws ClassNotFoundException{
        Message m = MessageSQL.recupererMessage(date,pseudo,contenu);
        LikeSQL.ajouterLike(m.getIdMessage(), this.pseudoClient);
        int compteur = LikeSQL.nbLikesMessage(m.getIdMessage());
        envoyerMessage("/likeDislike "+m.getIdMessage()+" "+compteur);
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

    public void supprimerMessage(String date,String pseudo, String contenu) throws ClassNotFoundException{
        System.out.println("supprimer message");
        Message m = MessageSQL.recupererMessage(date, pseudo, contenu);
        System.out.println(" message : " + m.getContenu());
        this.envoyerMessage("/supprimerMessage " + m.getIdMessage());
    }
}
