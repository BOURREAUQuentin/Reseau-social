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

import javax.imageio.plugins.tiff.TIFFDirectory;
import javax.swing.JTextArea;

public class ClientFX extends Application {

    private TextArea chatArea;
    private TextField messageField;
    private ClientModele clientModele;
    private TextArea finalServerLog;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.clientModele = new ClientModele(this);
        this.chatArea = new TextArea();
        this.finalServerLog = new TextArea();
        this.messageField = new TextField();


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
        sendButton.setOnAction(new ControleurEnvoyerMessage(this.clientModele, this));

        VBox inputBox = new VBox(5, messageField, sendButton);

        root.setCenter(chatScrollPane);
        root.setBottom(inputBox);

        // Ajout du gestionnaire d'événements pour la touche "Entrée"
        this.messageField.setOnAction(new ControleurEnvoyerMessage(this.clientModele, this));

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Client Messagerie");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> this.clientModele.closeConnection());
        primaryStage.show();

        // Lire les messages du serveur en arrière-plan
        this.clientModele.readMessages();
    }

    public void clearFielMessage(){
        this.messageField.clear();
    }

    public String getMessageField(){
        return this.messageField.getText();
    }

    public void runLater(String s){
        Platform.runLater(() -> finalServerLog.appendText(s));
    }

    public void exit(){
        Platform.exit();
    }
}
