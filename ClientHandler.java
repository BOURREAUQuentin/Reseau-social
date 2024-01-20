import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientHandler extends Thread {
    private String nomUtilisateurClient;
    private Socket socketClient;
    private BufferedReader lecteurClient;
    private PrintWriter ecriteurClient;
    private Map<String, PrintWriter> lesClientsConnectes;
    private static List<String> ClientsAbonnements = new ArrayList<>(); // clients auxquels l'utilisateur est abonné
    private static List<String> ClientsAbonnes = new ArrayList<>(); // clients abonnés à cet utilisateur
    private static List<String> ClientsNonAbonnes = new ArrayList<>(); // clients auxquels l'utilisateur n'est pas encore abonné

    public ClientHandler(Socket socketClient, Map<String, PrintWriter> lesClientsConnectes){
        this.socketClient = socketClient;
        this.lesClientsConnectes = lesClientsConnectes;
        try{
            this.lecteurClient = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            this.ecriteurClient = new PrintWriter(socketClient.getOutputStream());
            this.nomUtilisateurClient = this.lecteurClient.readLine();
            this.lesClientsConnectes.put(this.nomUtilisateurClient, this.ecriteurClient);
            try {
                for (Utilisateur u : AbonneSQL.getUtilisateursAbonnements(nomUtilisateurClient)){
                    ClientsAbonnements.add(u.getNomUtilisateur());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                for (Utilisateur u : AbonneSQL.getUtilisateurAbonnes(nomUtilisateurClient)){
                    ClientsAbonnes.add(u.getNomUtilisateur());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                for (Utilisateur u : AbonneSQL.getUtilisateurNonAbonnes(nomUtilisateurClient)){
                    ClientsNonAbonnes.add(u.getNomUtilisateur());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(this.nomUtilisateurClient + " viens de se connecter");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            this.lecteurClient = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
            this.ecriteurClient = new PrintWriter(this.socketClient.getOutputStream());
            String message;
            while ((message = this.lecteurClient.readLine()) != null) {
                System.out.println(message);
                this.broadcast(message);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            lesClientsConnectes.remove(this.nomUtilisateurClient);
            System.out.println(this.nomUtilisateurClient + " est déconnecté.");
            try {
                socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void follow(String[] recipientMessage){
        for (String pseudo : lesClientsConnectes.keySet()) {
            if (pseudo.equals(recipientMessage[1])) {
                ClientsAbonnements.add(recipientMessage[1]);
            }
        }
        return;
    }

    private void unfollow(String[] recipientMessage){
        for (String pseudo : lesClientsConnectes.keySet()) {
            if (pseudo.equals(recipientMessage[1])) {
                ClientsAbonnements.remove(recipientMessage[1]);
            }
        }
        return;
    }

    private void likerDislike(String[] recipientMessage) {
        String idMessage = recipientMessage[1];
        int id = Integer.parseInt(idMessage);
        Message message=null;
        try {
            message = MessageSQL.recupererMessageParId(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("t"+message.getDate());
        String messageWithDate = "///likeDislike"+"|||"+message.getDate() +"|||" + message.getNomUtilisateur() + "|||" + message.getContenu();
        
        envoyerMessagesLesUtilisateurs(messageWithDate);
    }

    public void supprimerMessage(String[] recipientMessage){
        String idMessage = recipientMessage[1];
        int id = Integer.parseInt(idMessage);
        Message message=null;
        System.out.println("id : "+id);
        try {
            message = MessageSQL.recupererMessageParId(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String messageWithDate = "///SUPPRIMER"+"|||"+message.getDate() +"|||" + message.getNomUtilisateur() + "|||" + message.getContenu();
        try {
            MessageSQL.supprimerMessage(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        envoyerMessagesLesUtilisateurs(messageWithDate);
    }

    private void envoyerMessagesLesUtilisateurs(String message) {
        List<String> utilisateurs = new ArrayList<>();
        try {
            for (Utilisateur u : UtilisateurSQL.getLesUtilisateurs()) 
            // tous les client de la bd
            {
                System.out.println("pseudo : "+u.getNomUtilisateur());
                utilisateurs.add(u.getNomUtilisateur());

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (String pseudo : utilisateurs) {
            PrintWriter recipientWriter = lesClientsConnectes.get(pseudo);
            if (recipientWriter != null){  // on regarde si il est connecter
                recipientWriter.println(message);
            }
        }
    }

    private void broadcast(String message) {

        System.out.println("message : " + message);
        if (message.equals("Votre compte a été supprimé. La connexion sera fermée.")){
            // si le message est supprimer le compte alors le message est envoyer au client et le client est deconnecter
            this.ecriteurClient.println(message);
            System.out.println("pseudo : "+nomUtilisateurClient);
        }
        if (message.contains("/")){
            // permet de gerer les evenement de like dislike follow unfollow
            System.out.println("hahahaha : "+message);
            String[] recipientMessage = message.split(" ");
            if (recipientMessage[0].equals("/follow")) {
                follow(recipientMessage);
            }

            else if (recipientMessage[0].equals("/unfollow")){
                unfollow(recipientMessage);
            }
            else if(recipientMessage[0].equals("/likeDislike")){
                likerDislike(recipientMessage);
            }
            else if(recipientMessage[0].equals("/supprimerMessage")){
                supprimerMessage(recipientMessage);
            }
        }
        else{
            // permet d'envoyer les messages
            List<String> utilisateurs = new ArrayList<>();

            try{
                MessageSQL.ajouterMessage(nomUtilisateurClient, message);
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            try {
                for (Utilisateur u : UtilisateurSQL.getLesUtilisateurs())
                {
                    utilisateurs.add(u.getNomUtilisateur());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (String pseudo : utilisateurs) {
                PrintWriter recipientWriter = lesClientsConnectes.get(pseudo);
                if (recipientWriter != null){
                    LocalDateTime now = LocalDateTime.now();
                    // pour le format de la date
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // Formate la date en chaîne de caractères selon le format spécifié
                    String formattedDateTime = now.format(formatter);

                    String messageWithDate = formattedDateTime +"|||" + nomUtilisateurClient + "|||" + message;
                    recipientWriter.println(messageWithDate);
                }
            }
        }
    }
}
