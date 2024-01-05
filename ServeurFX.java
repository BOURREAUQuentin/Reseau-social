import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ListView;

public class ServeurFX extends Application {
    private Map<String, PrintWriter> connectedClients = new HashMap<>();
    private ObservableList<Client> clientList = FXCollections.observableArrayList();
    private TextArea serverLog = new TextArea();
    private ListView<Client> clientsListView = new ListView<>(clientList);
    private List<Client> allClients = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        serverLog.setEditable(false);

        root.setCenter(new ScrollPane(serverLog));
        root.setRight(createClientsListView());

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Serveur de Messagerie");

        primaryStage.setOnCloseRequest(event -> closeServer());

        primaryStage.show();

        new Thread(this::startServer).start();
    }

    private Node createClientsListView() {
        VBox clientsBox = new VBox(5);
        clientsBox.setPadding(new Insets(10));
        clientsBox.getChildren().add(new Label("Clients connectés"));

        clientsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    HBox clientBox = new HBox(5);
                    Label clientLabel = new Label(client.getNomUtilisateur());
                    Button subscribeButton = new Button("S'abonner");
                    subscribeButton.setOnAction(event -> subscribeToClient(client));
                    clientBox.getChildren().addAll(clientLabel, subscribeButton);
                    setGraphic(clientBox);
                }
            }
        });

        clientsBox.getChildren().add(clientsListView);
        return clientsBox;
    }

    private void subscribeToClient(Client targetClient) {
        Client subscriber = selectedClient();
        if (subscriber != null) {
            String message = "SUBSCRIBE " + targetClient.getNomUtilisateur();
            connectedClients.get(subscriber.getNomUtilisateur()).println(message);
        }
    }

    private Client selectedClient() {
        return clientsListView.getSelectionModel().getSelectedItem();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            Platform.runLater(() -> serverLog.appendText("Server listening on port 12345\n"));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter serverResponse = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientId = clientReader.readLine();
            Client client = new Client(clientId);
            connectedClients.put(clientId, serverResponse);
            allClients.add(client);
            updateClientListView();

            Platform.runLater(() -> serverLog.appendText("Client " + clientId + " connected to the server!\n"));

            String message;
            while ((message = clientReader.readLine()) != null) {
                final String finalClientId = clientId;
                final String finalMessage = message;

                Platform.runLater(() -> serverLog.appendText("Client " + finalClientId + ": " + finalMessage + "\n"));
                for (Map.Entry<String, PrintWriter> entry: connectedClients.entrySet()) {
                    List<Client> listeAbonnesClientConnecte = new ArrayList<Client>();
                    Client clientConnecte = findClientById(entry.getKey());
                    for (Client abonneClientConnecte : clientConnecte.getListeAbonnes()){
                        listeAbonnesClientConnecte.add(abonneClientConnecte);
                    }
                    if (clientConnecte.equals(client)){
                        entry.getValue().println("Client " + finalClientId + ": " + finalMessage);
                    }
                    for (Client abonneClientConnecte : listeAbonnesClientConnecte){
                        if (abonneClientConnecte.equals(client)){
                            entry.getValue().println("Client " + finalClientId + ": " + finalMessage);
                        }
                    }
                }

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
            }

            try {
                clientReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectedClients.remove(clientId);
            allClients.remove(client);
            updateClientListView();

            Platform.runLater(() -> {
                serverLog.appendText("Client " + clientId + " disconnected.\n");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeServer() {
        Platform.runLater(() -> {
            System.exit(0);
        });
    }

    private void updateClientListView() {
        Platform.runLater(() -> {
            clientList.setAll(allClients);
            sendClientListToAll();
        });
    }

    private void sendClientListToAll() {
        StringBuilder clientListString = new StringBuilder();
        for (Client client : clientList) {
            clientListString.append(client.getNomUtilisateur()).append(",");
        }
        if (clientListString.length() > 0) {
            clientListString.setLength(clientListString.length() - 1); // Remove the trailing comma
        }

        for (PrintWriter writer : connectedClients.values()) {
            writer.println("CLIENT_LIST " + clientListString.toString());
        }
    }

    private void sendClientListToOne(String clientId, String[] recipients) {
        StringBuilder clientListString = new StringBuilder();
        for (Client client : clientList) {
            if (!client.getNomUtilisateur().equals(clientId) && containsIgnoreCase(recipients, client.getNomUtilisateur())) {
                clientListString.append(client.getNomUtilisateur()).append(",");
            }
        }
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
            Platform.runLater(() -> serverLog.appendText(subscriberId + " subscribed to " + subscribeTo + "\n"));
        }
    }

    private void unsubscribeClient(String subscriberId, String subscribeTo) {
        Client subscriber = findClientById(subscriberId);
        Client targetClient = findClientById(subscribeTo);

        if (subscriber != null && targetClient != null) {
            subscriber.supprimerAbonne(targetClient);
            Platform.runLater(() -> serverLog.appendText(subscriberId + " unsubscribed to " + subscribeTo + "\n"));
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
