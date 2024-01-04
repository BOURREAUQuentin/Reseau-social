import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFX extends Application {

    private PrintWriter writer;
    private BufferedReader reader;
    private TextArea chatArea;
    private TextField messageField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Connexion au serveur
        connectToServer();

        // Mise en place de l'interface graphique
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        ScrollPane chatScrollPane = new ScrollPane(chatArea);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setFitToHeight(true);

        messageField = new TextField();
        messageField.setPromptText("Tapez votre message ici...");

        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> sendMessage());

        VBox inputBox = new VBox(5, messageField, sendButton);

        root.setCenter(chatScrollPane);
        root.setBottom(inputBox);

        // Ajout du gestionnaire d'événements pour la touche "Entrée"
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Client Messagerie");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> closeConnection());
        primaryStage.show();

        // Lire les messages du serveur en arrière-plan
        new Thread(this::readMessages).start();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            appendToChatArea("Connecté au serveur !");
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            appendToChatArea("Moi: " + message);
            messageField.clear();
        }
    }

    private void readMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                appendToChatArea("Serveur: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToChatArea(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    private void closeConnection() {
        try {
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
