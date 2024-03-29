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

/**
 * La classe ClientFX représente l'interface graphique d'un client dans une application de messagerie.
 * Elle permet à un utilisateur de se connecter à un serveur, d'échanger des messages avec d'autres clients
 * et de gérer ses abonnements.
 */
public class ClientFX extends Application {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private VBox clientLog;
    private TextField messageField;
    private Button sendButton;
    private TextField nameField;
    private BorderPane connectionPage;
    private Set<Client> connectedClients = new HashSet<>();
    private Client clientConnecte;

    /**
     * Méthode principale pour lancer l'application JavaFX.
     *
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Méthode appelée au démarrage de l'application.
     *
     * @param primaryStage La fenêtre principale de l'application.
     */
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

    /**
     * Crée la page de connexion avec les champs de nom et de bouton de connexion.
     *
     * @return Une instance de BorderPane représentant la page de connexion.
     */
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

    /**
     * Crée la page principale de l'application avec le journal des messages, la liste des clients connectés,
     * le champ de saisie de message et le bouton d'envoi.
     *
     * @return Une instance de BorderPane représentant la page principale.
     */
    private BorderPane createMainPage() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // clientLog = new TextArea();
        // clientLog.setEditable(false);
        // clientLog.setWrapText(true);
        // root.setCenter(clientLog);
        clientLog = new VBox(2);
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

    /**
     * Gère la connexion à un serveur en établissant une connexion socket et en lançant un thread pour la lecture des messages.
     */
    private void connectToServer() {
        String clientName = nameField.getText();
        clientConnecte = new Client(clientName);
        if (!clientName.isEmpty()) {
            try {
                socket = new Socket("localhost", 12345);
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Envoyer le nom au serveur
                writer.println(clientName);

                // Créer la page principale du client
                BorderPane root = createMainPage();
                Scene scene = new Scene(root, 800, 500);
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
                                final String finalMessage = message; // Copie finale de la variable message
                                Label messageLabel = new Label(finalMessage);
                                Button likeButton = new Button("Liker");
                                for (Client clientConnecte : connectedClients) {
                                    Platform.runLater(() -> {
                                        HBox messageBox = new HBox(5);
                                        if (!finalMessage.contains("LIKE ")) {
                                            likeButton.setOnAction(
                                                    event -> toggleLike(clientConnecte.getNomUtilisateur(),
                                                            likeButton, finalMessage));
                                            messageBox = new HBox(5, messageLabel, likeButton);
                                        } else {
                                            messageBox = new HBox(5, messageLabel);
                                        }
                                        clientLog.getChildren().add(messageBox);
                                    });
                                }
                                Platform.runLater(() -> {
                                    HBox messageBox = new HBox(5);
                                    if (!finalMessage.contains("LIKE ")) {
                                        likeButton.setOnAction(
                                                event -> toggleLike(clientConnecte.getNomUtilisateur(),
                                                        likeButton, finalMessage));
                                        messageBox = new HBox(5, messageLabel, likeButton);
                                    } else {
                                        messageBox = new HBox(5, messageLabel);
                                    }
                                    clientLog.getChildren().add(messageBox);
                                });
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(() -> {
                            closeConnection();
                            Platform.exit();
                        });
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Envoie un message au serveur à partir du contenu du champ de saisie de message.
     * Le message est également ajouté à la liste des messages du client local.
     */
    private void sendMessage() {
        String messageContent = messageField.getText();
        if (!messageContent.isEmpty()) {
            Message message = new Message(messageContent, nameField.getText());
            clientConnecte.ajouterMessage(message);
            writer.println(message.toString());
            messageField.clear();
        }
    }

    /**
     * Ferme la connexion socket lorsque l'application est fermée.
     */
    private void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Traite un message de la liste des clients envoyée par le serveur et met à jour la liste des clients connectés.
     *
     * @param message Le message de la liste des clients envoyé par le serveur.
     */
    private void handleClientListMessage(String message) {
        String[] parts = message.split(" ");
        if (parts.length == 2) {
            String[] clients = parts[1].split(",");
            Platform.runLater(() -> {
                VBox clientsBox = (VBox) ((BorderPane) clientLog.getParent()).getLeft();
                clientsBox.getChildren().clear();
                clientsBox.getChildren().addAll(new Label("Connecté en tant que " + nameField.getText()),
                        new Label("Clients connectés"));

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

    /**
     * Bascule l'abonnement à un client cible en fonction de l'état actuel du bouton d'abonnement.
     *
     * @param targetClient Le nom du client cible.
     * @param subscribeButton Le bouton d'abonnement associé.
     */
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

    /**
     * Bascule l'état de "j'aime" pour un message en fonction de l'état actuel du bouton "j'aime".
     *
     * @param targetClient Le client qui a envoyé le message.
     * @param likeButton Le bouton "j'aime" associé.
     * @param message Le contenu du message.
     */
    private void toggleLike(String targetClient, Button likeButton, String message) {
        String messageRetour;
        Client target = null;
        if (targetClient.equals(nameField.getText())) {
            target = clientConnecte;
        } else {
            target = findClientById(targetClient);
        }

        if (target != null) {
            System.out.println(likeButton.getText());
            if (likeButton.getText().equals("Liker")) {
                messageRetour = "LIKE " + message;
                // target.ajouterAbonne(findClientById(nameField.getText()));
                likeButton.setText("Unliker");
            } else {
                messageRetour = "UNLIKE " + message;
                // target.supprimerAbonne(findClientById(nameField.getText()));
                likeButton.setText("Liker");
            }

            writer.println(messageRetour);
        }
    }

    /**
     * Recherche un client par son identifiant dans la liste des clients connectés.
     *
     * @param clientId L'identifiant du client à rechercher.
     * @return Le client correspondant s'il est trouvé, sinon null.
     */
    private Client findClientById(String clientId) {
        for (Client client : connectedClients) {
            if (client.getNomUtilisateur().equals(clientId)) {
                return client;
            }
        }
        return null;
    }
}
