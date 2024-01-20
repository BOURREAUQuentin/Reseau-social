import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PageAccueil {
    private Stage stage;
    private static Client client;

    public PageAccueil(Stage stage, Client client) {
        this.stage = stage;
        this.client=client;
        stage.setTitle("Bienvenue sur TUIT'O");
    }

    public void show(){
        // TODO
    }
}
