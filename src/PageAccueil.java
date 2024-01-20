import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.application.Platform;

public class PageAccueil {
    private Stage stage;
    private static Client client;

    public PageAccueil(Stage stage, Client client) {
        this.stage = stage;
        PageAccueil.client = client;
    }

    public BorderPane showPageAccueil(){
        BorderPane borderPane = new BorderPane();
        TextField messageInput = new TextField("Voici la liste des messages");
        Scene scene = new Scene(borderPane, 850, 600);
        stage.setScene(scene);
        stage.show();
    }
}
