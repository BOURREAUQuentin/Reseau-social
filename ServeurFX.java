import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurFX extends Application {

    private PrintWriter writer;
    private TextArea serverLog;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Mise en place de l'interface graphique du serveur
        BorderPane root = new BorderPane();
        root.setPrefSize(400, 300);

        serverLog = new TextArea();
        serverLog.setEditable(false);
        serverLog.setWrapText(true);

        root.setCenter(serverLog);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Serveur Messagerie");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> closeServer());
        primaryStage.show();

        // Démarrer le serveur en arrière-plan
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            TextArea finalServerLog = serverLog;  // Copie finale

            Platform.runLater(() -> finalServerLog.appendText("Le serveur est à l'écoute sur le port 12345\n"));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Platform.runLater(() -> finalServerLog.appendText("Client connecté au serveur !\n"));

                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = reader.readLine()) != null) {
                    final String finalMessage = message;  // Copie finale
                    Platform.runLater(() -> finalServerLog.appendText("Client: " + finalMessage + "\n"));
                    writer.println(message);
                }

                Platform.runLater(() -> finalServerLog.appendText("Client déconnecté.\n"));
                writer.close();
                reader.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeServer() {
        System.exit(0);
    }
}
