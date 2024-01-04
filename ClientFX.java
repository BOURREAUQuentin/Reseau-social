import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFX extends Application {

    private Socket socket;
    private PrintWriter ecrivain;
    private BufferedReader lecteur;
    private TextArea journalClient;
    private TextField champMessage;
    private Button boutonEnvoyer;
    private TextField champNom;
    private BorderPane pageConnexion;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pageConnexion = creerPageConnexion();
        Scene scene = new Scene(pageConnexion, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client Messagerie");

        // Ajouter le gestionnaire d'événements pour la fermeture de la fenêtre
        primaryStage.setOnCloseRequest(event -> {
            fermerConnexion();
            Platform.exit();
        });

        primaryStage.show();
    }

    private BorderPane creerPageConnexion() {
        BorderPane racine = new BorderPane();
        racine.setPadding(new Insets(10));

        champNom = new TextField();
        champNom.setPromptText("Entrez votre nom...");
        champNom.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                seConnecterAuServeur();
            }
        });

        Button boutonConnexion = new Button("Connexion");
        boutonConnexion.setOnAction(e -> seConnecterAuServeur());

        HBox centreHBox = new HBox(10, champNom, boutonConnexion);
        centreHBox.setPadding(new Insets(10));
        racine.setCenter(centreHBox);

        return racine;
    }

    private void seConnecterAuServeur() {
        String nomClient = champNom.getText();
        if (!nomClient.isEmpty()) {
            try {
                socket = new Socket("localhost", 12345);
                ecrivain = new PrintWriter(socket.getOutputStream(), true);
                lecteur = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Envoyer le nom au serveur
                ecrivain.println(nomClient);

                // Créer la page principale du client
                BorderPane racine = creerPagePrincipale();
                Scene scene = new Scene(racine, 400, 300);
                Stage stage = (Stage) pageConnexion.getScene().getWindow();
                stage.setScene(scene);

                // Démarrer le thread pour la lecture des messages du serveur
                new Thread(() -> {
                    try {
                        String message;
                        while ((message = lecteur.readLine()) != null) {
                            journalClient.appendText(message + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        fermerConnexion();
                        Platform.exit();  // Fermer l'application JavaFX
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BorderPane creerPagePrincipale() {
        BorderPane racine = new BorderPane();
        racine.setPadding(new Insets(10));

        journalClient = new TextArea();
        journalClient.setEditable(false);
        journalClient.setWrapText(true);
        racine.setCenter(journalClient);

        champMessage = new TextField();
        champMessage.setPromptText("Tapez votre message...");
        champMessage.setOnAction(e -> envoyerMessage());

        boutonEnvoyer = new Button("Envoyer");
        boutonEnvoyer.setOnAction(e -> envoyerMessage());

        HBox basHBox = new HBox(10, champMessage, boutonEnvoyer);
        basHBox.setPadding(new Insets(10));
        racine.setBottom(basHBox);

        return racine;
    }

    private void envoyerMessage() {
        String message = champMessage.getText();
        if (!message.isEmpty()) {
            ecrivain.println(message);
            champMessage.clear();
        }
    }

    private void fermerConnexion() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
