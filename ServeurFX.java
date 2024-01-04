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
    private Map<String, PrintWriter> connectedClients = new HashMap<>();
    private ObservableList<String> clientList = FXCollections.observableArrayList();
    private TextArea serverLog = new TextArea();
    private ListView<String> clientsListView = new ListView<>(clientList);

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

        new Thread(this::startServer).start();
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
            connectedClients.put(clientId, serverResponse);
            updateClientListView();

            Platform.runLater(() -> serverLog.appendText("Client " + clientId + " connected to the server!\n"));

            String message;
            while ((message = clientReader.readLine()) != null) {
                final String finalClientId = clientId;
                final String finalMessage = message;
                Platform.runLater(() -> serverLog.appendText("Client " + finalClientId + ": " + finalMessage + "\n"));

                for (PrintWriter destinationWriter : connectedClients.values()) {
                    destinationWriter.println("Client " + finalClientId + ": " + finalMessage);
                }

                if (message.startsWith("@")) {
                    String[] parts = message.split(" ", 2);
                    if (parts.length == 2) {
                        String[] recipients = parts[0].substring(1).split(",");
                        sendClientListToOne(clientId, recipients);
                    }
                }
            }

            try {
                clientReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectedClients.remove(clientId);
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
            clientList.setAll(connectedClients.keySet());
            sendClientListToAll();
        });
    }

    private void sendClientListToAll() {
        StringBuilder clientListString = new StringBuilder();
        for (String client : clientList) {
            clientListString.append(client).append(",");
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
        for (String client : clientList) {
            if (!client.equals(clientId) && containsIgnoreCase(recipients, client)) {
                clientListString.append(client).append(",");
            }
        }
        if (clientListString.length() > 0) {
            clientListString.setLength(clientListString.length() - 1); // Remove the trailing comma
        }

        connectedClients.get(clientId).println("CLIENT_LIST " + clientListString.toString());
    }

    private boolean containsIgnoreCase(String[] array, String target) {
        for (String s : array) {
            if (s.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}
