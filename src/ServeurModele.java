import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServeurModele{ 

    private ServeurFX vueServeur;
    private PrintWriter writer;
    private Map<String, PrintWriter> connectedClients = new HashMap<>();
    private List<Client> allClients = new ArrayList<>();
    private ServerSocket serveurSocket;

    public ServeurModele(ServeurFX vueServeur) throws IOException{
        this.vueServeur = vueServeur;
        this.serveurSocket = new ServerSocket(12345);
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                this.vueServeur.runLater("Client connecté au serveur !\n");

                this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = reader.readLine()) != null) {
                    final String finalMessage = message;
                    String fMessage = "Client: " + finalMessage + "\n";
                    this.vueServeur.runLater(fMessage);
                    this.writer.println(message);
                }

                this.vueServeur.runLater("Client déconnecté.\n");
                writer.close();
                reader.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        System.exit(0);
    }


    public void handleClient(Socket clientSocket) {
        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter serverResponse = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientId = clientReader.readLine();
            Client client = new Client(clientId);
            connectedClients.put(clientId, serverResponse);
            allClients.add(client);
            updateClientListView();

            String message;
            while ((message = clientReader.readLine()) != null) {
                final String finalClientId = clientId;
                final String finalMessage = message;

                if (message.startsWith("@")) {
                    String[] parts = message.split(" ", 2);
                    if (parts.length == 2) {
                        String[] recipients = parts[0].substring(1).split(",");
                        sendClientListToOne(clientId, recipients);
                    }
                } else if (message.startsWith("SUBSCRIBE")) {
                    String[] parts = message.split(" ");
                    if (parts.length == 2) {
                        String subscribeTo = parts[1];
                        subscribeClient(clientId, subscribeTo);
                    }
                }
                else if (message.startsWith("UNSUBSCRIBE")) {
                    String[] parts = message.split(" ");
                    if (parts.length == 2) {
                        String subscribeTo = parts[1];
                        unsubscribeClient(clientId, subscribeTo);
                    }
                }
                else{
                    for (Map.Entry<String, PrintWriter> entry: connectedClients.entrySet()) {
                        List<Client> listeAbonnesClientConnecte = new ArrayList<Client>();
                        Client clientConnecte = findClientById(entry.getKey());
                        for (Client abonneClientConnecte : clientConnecte.getListeAbonnes()){
                            listeAbonnesClientConnecte.add(abonneClientConnecte);
                        }
                        if (clientConnecte.equals(client)){
                            entry.getValue().println(finalClientId + ": " + finalMessage);
                        }
                        for (Client abonneClientConnecte : listeAbonnesClientConnecte){
                            if (abonneClientConnecte.equals(client)){
                                entry.getValue().println(finalClientId + " (abonné): " + finalMessage);
                            }
                        }
                    }
                }
            }

            try {
                clientReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectedClients.remove(clientId);
            allClients.remove(client);
            updateClientListView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateClientListView() {
        sendClientListToAll();
    }

    private void sendClientListToAll() {
        StringBuilder clientListString = new StringBuilder();
        if (clientListString.length() > 0) {
            clientListString.setLength(clientListString.length() - 1); // Remove the trailing comma
        }

        for (PrintWriter writer : connectedClients.values()) {
            writer.println("CLIENT_LIST " + clientListString.toString());
        }
    }

    private void sendClientListToOne(String clientId, String[] recipients) {
        StringBuilder clientListString = new StringBuilder();
        if (clientListString.length() > 0) {
            clientListString.setLength(clientListString.length() - 1); // Remove the trailing comma
        }

        connectedClients.get(clientId).println("CLIENT_LIST " + clientListString.toString());
    }

    private void subscribeClient(String subscriberId, String subscribeTo) {
        Client subscriber = findClientById(subscriberId);
        Client targetClient = findClientById(subscribeTo);

        if (subscriber != null && targetClient != null) {
            subscriber.ajouterAbonne(targetClient);
            // Nouvelle notification pour le client s'abonnant
            connectedClients.get(subscriberId).println("Vous venez de vous abonner à " + subscribeTo);
            // Nouvelle notification pour le client cible
            connectedClients.get(subscribeTo).println(subscriberId + " s'est abonné à vous!");
        }
    }

    private void unsubscribeClient(String subscriberId, String subscribeTo) {
        Client subscriber = findClientById(subscriberId);
        Client targetClient = findClientById(subscribeTo);

        if (subscriber != null && targetClient != null) {
            subscriber.supprimerAbonne(targetClient);
            // Nouvelle notification pour le client s'abonnant
            connectedClients.get(subscriberId).println("Vous venez de vous désabonner à " + subscribeTo);
            // Nouvelle notification pour le client cible
            connectedClients.get(subscribeTo).println(subscriberId + " s'est désabonné à vous!");
        }
    }

    private boolean containsIgnoreCase(String[] array, String target) {
        for (String s : array) {
            if (s.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

    private Client findClientById(String clientId) {
        for (Client client : allClients) {
            if (client.getNomUtilisateur().equals(clientId)) {
                return client;
            }
        }
        return null;
    }
}