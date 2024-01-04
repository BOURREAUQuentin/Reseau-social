import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServeurFX extends Application {
    private Map<String, PrintWriter> clientsConnectes = new HashMap<>();
    private ObservableList<String> listeClients = FXCollections.observableArrayList();
    private TextArea serverLog = new TextArea();
    private ListView<String> clientsListView = new ListView<>(listeClients);
    private String idClient;  // Déplacer la déclaration de la variable à l'extérieur du bloc try

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        serverLog.setEditable(false);

        root.setCenter(new ScrollPane(serverLog));
        root.setRight(new ScrollPane(clientsListView));

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Serveur de Messagerie");

        // Définir l'action à effectuer lors de la fermeture de la fenêtre
        primaryStage.setOnCloseRequest(event -> closeServer());

        primaryStage.show();

        new Thread(this::demarrerServeur).start();
    }

    private void demarrerServeur() {
        try {
            ServerSocket serveurSocket = new ServerSocket(12345);
            Platform.runLater(() -> serverLog.appendText("Serveur à l'écoute sur le port 12345\n"));

            while (true) {
                Socket clientSocket = serveurSocket.accept();
                new Thread(() -> gererClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gererClient(Socket clientSocket) {
        try {
            BufferedReader lecteurClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter reponseServeur = new PrintWriter(clientSocket.getOutputStream(), true);

            // Lire l'ID du client
            idClient = lecteurClient.readLine();
            clientsConnectes.put(idClient, reponseServeur);

            Platform.runLater(() -> {
                serverLog.appendText("Client " + idClient + " connecté au serveur !\n");
                listeClients.add(idClient);
            });

            String message;
            while ((message = lecteurClient.readLine()) != null) {
                final String[] finalMessage = {message};
                Platform.runLater(() -> serverLog.appendText("Client " + idClient + ": " + finalMessage[0] + "\n"));

                for (PrintWriter destinaireWriter : clientsConnectes.values()) {
                    destinaireWriter.println("Client " + idClient + ": " + finalMessage[0]);
                }
            }

            lecteurClient.close();
            clientsConnectes.remove(idClient);
            Platform.runLater(() -> {
                serverLog.appendText("Client " + idClient + " déconnecté.\n");
                listeClients.remove(idClient);
            });
        } catch (IOException e) {
            // Gérer l'exception liée à la déconnexion du client
            e.printStackTrace();
            Platform.runLater(() -> {
                serverLog.appendText("Client " + idClient + " déconnecté (erreur).\n");
                listeClients.remove(idClient);
            });
        }
    }

    private void closeServer() {
        Platform.runLater(() -> {
            System.exit(0);
        });
    }
}
