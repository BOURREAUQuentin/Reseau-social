import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PageAccueil {
    private Stage stage;
    private static Client client;

    public PageAccueil(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    public void showPageAccueil(){
        BorderPane borderPane = new BorderPane();
        // Panel en bas pour écrire des messages
        TextField messageInput = new TextField();
        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> {
            String message = messageInput.getText();
            if (message.isEmpty() || message.equals("") || message.isBlank()) {
                return;
            }
            client.envoyerMessage(message);
            messageInput.clear();
        });
        HBox messageBox = new HBox(messageInput, sendButton);
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(10));
        borderPane.setBottom(messageBox);

        Scene scene = new Scene(borderPane, 850, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void afficheMessage(String message){
        // TODO
    }
}
