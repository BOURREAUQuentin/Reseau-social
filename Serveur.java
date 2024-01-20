import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                ClientHandler clientHandler = new ClientHandler(clientSocket, lesClientsConnectes);
                clientHandler.start();

                new Thread(()->{
                    BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                    while(true){
                        try {
                            String consoleInput = consoleReader.readLine();
        
                            if (consoleInput.contains("/deleteUser")) {
                                String[] parts = consoleInput.split(" ");
                                String pseudo = parts[1];
                                try {
                                    if (UtilisateurSQL.utilisateurExiste(pseudo))
                                    informClientHandlerDeleteUser(pseudo);
                                    else{
                                        System.out.println("Ce pseudo n'existe pas");
                                    }
                                } 
                                catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (consoleInput.contains("/deleteMessage")) {
                                String[] parts = consoleInput.split(" ");
                                String idMessageString = parts[1];
                                try{
                                    int idMessage = Integer.parseInt(idMessageString);
                                    if (MessageSQL.idMessagePresent(idMessage))
                                    informClientHandlerDeleteMessage(idMessage);
                                    else{
                                        System.out.println("Ce message n'existe pas");
                                    }
                                }
                                catch(NumberFormatException e){
                                    System.out.println("L'identifiant du message doit être un nombre entier.");
                                }
                                catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void informClientHandlerDeleteUser(String pseudo) {
        if (lesClientsConnectes.containsKey(pseudo)) {
            PrintWriter userWriter = lesClientsConnectes.get(pseudo);
            userWriter.println("Votre compte a été supprimé. La connexion sera fermée.");
        }
        else{
            try {
                UtilisateurSQL.supprimerUtilisateur(pseudo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void informClientHandlerDeleteMessage(int idMessage) {
        if (lesClientsConnectes.isEmpty()) {
            try {
                MessageSQL.supprimerMessage(idMessage);
                System.out.println("Message supprimé. acun client");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Message message = null;
            try {
                message = MessageSQL.recupererMessageParId(idMessage);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String envoie = "///SUPPRIMER" + "|||" + message.getDate() + "|||" + message.getNomUtilisateur() + "|||" + message.getContenu();
            // Retirer l'entrée du client associée au pseudo après la suppression du message
            for (String pseudo : lesClientsConnectes.keySet()) {
                PrintWriter writer = lesClientsConnectes.get(pseudo);
                if (writer != null) {
                    writer.println(envoie);
                }
            }
            try {
                MessageSQL.supprimerMessage(idMessage);
                System.out.println("Message supprimé. avec client");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
