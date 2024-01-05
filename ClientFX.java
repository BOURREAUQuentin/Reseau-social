import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientFX extends Application {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private TextArea clientLog;
    private TextField messageField;
    private Button sendButton;
    private TextField nameField;
    private BorderPane connectionPage;
    private Set<Client> connectedClients = new HashSet<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        connectionPage = createConnectionPage();
        Scene scene = new Scene(connectionPage, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client Messagerie");

        primaryStage.setOnCloseRequest(event -> {
            closeConnection();
            Platform.exit();
        });

        primaryStage.show();
    }

    private BorderPane createConnectionPage() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        nameField = new TextField();
        nameField.setPromptText("Entrez votre nom...");
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                connectToServer();
            }
        });

        Button connectButton = new Button("Connexion");
        connectButton.setOnAction(e -> connectToServer());

        HBox centerHBox = new HBox(10, nameField, connectButton);
        centerHBox.setPadding(new Insets(10));
        root.setCenter(centerHBox);

        return root;
    }

    private BorderPane createMainPage() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        clientLog = new TextArea();
        clientLog.setEditable(false);
        clientLog.setWrapText(true);
        root.setCenter(clientLog);

        VBox clientsBox = new VBox(5);
        clientsBox.setPadding(new Insets(10));
        clientsBox.getChildren().add(new Label("Clients connectés"));

        root.setLeft(clientsBox);

        messageField = new TextField();
        messageField.setPromptText("Tapez votre message...");
        messageField.setOnAction(e -> sendMessage());

        sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> sendMessage());

        HBox bottomHBox = new HBox(10, messageField, sendButton);
        bottomHBox.setPadding(new Insets(10));
        root.setBottom(bottomHBox);

        return root;
    }

    private void connectToServer() {
        String clientName = nameField.getText();
        if (!clientName.isEmpty()) {
            try {
                socket = new Socket("localhost", 12345);
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Envoyer le nom au serveur
                writer.println(clientName);

                // Créer la page principale du client
                BorderPane root = createMainPage();
                Scene scene = new Scene(root, 400, 300);
                Stage stage = (Stage) connectionPage.getScene().getWindow();
                stage.setScene(scene);

                // Démarrer le thread pour la lecture des messages du serveur
                new Thread(() -> {
                    try {
                        String message;
                        while ((message = reader.readLine()) != null) {
                            if (message.startsWith("CLIENT_LIST ")) {
                                handleClientListMessage(message);
                            } else {
                                clientLog.appendText(message + "\n");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        closeConnection();
                        Platform.exit();
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            messageField.clear();
        }
    }

    private void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientListMessage(String message) {
        String[] parts = message.split(" ");
        if (parts.length == 2) {
            String[] clients = parts[1].split(",");
            Platform.runLater(() -> {
                VBox clientsBox = (VBox) ((BorderPane) clientLog.getParent()).getLeft();
                clientsBox.getChildren().clear();
                clientsBox.getChildren().add(new Label("Clients connectés"));
    
                connectedClients.clear();
    
                for (String client : clients) {
                    if (!client.equals(nameField.getText())) { // Exclure le client actuel
                        Label clientLabel = new Label(client);
                        Button subscribeButton = new Button("S'abonner");
                        subscribeButton.setOnAction(event -> toggleSubscription(client, subscribeButton));
                        HBox clientBox = new HBox(5, clientLabel, subscribeButton);
                        clientsBox.getChildren().add(clientBox);
                        connectedClients.add(new Client(client));
                    }
                }
            });
        }
    }

    private void toggleSubscription(String targetClient, Button subscribeButton) {
        String message;
        Client target = findClientById(targetClient);
    
        if (target != null) {
            if (subscribeButton.getText().equals("S'abonner")) {
                message = "SUBSCRIBE " + targetClient;
                target.ajouterAbonne(findClientById(nameField.getText()));
                subscribeButton.setText("Se désabonner");
            } else {
                message = "UNSUBSCRIBE " + targetClient;
                target.supprimerAbonne(findClientById(nameField.getText()));
                subscribeButton.setText("S'abonner");
            }
    
            writer.println(message);
        }
    }

    private Client findClientById(String clientId) {
        for (Client client : connectedClients) {
            if (client.getNomUtilisateur().equals(clientId)) {
                return client;
            }
        }
        return null;
    }
    
    private void subscribeToClient(String targetClient) {
        String message = "SUBSCRIBE " + targetClient;
        writer.println(message);
    }    
}
