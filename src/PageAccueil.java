import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.application.Platform;

/**
 * La classe PageAccueil représente la page principale de l'application.
 * Elle affiche la liste des messages et fournit une interface pour interagir avec le client.
 */
public class PageAccueil {
    private Stage stage;
    private static Client client;

    /**
     * Constructeur de la classe PageAccueil.
     *
     * @param stage  La fenêtre principale de l'application.
     * @param client Le client associé à la page d'accueil.
     */
    public PageAccueil(Stage stage, Client client) {
        this.stage = stage;
        PageAccueil.client = client;
    }

    /**
     * Affiche la page d'accueil de l'application.
     *
     * @return Un objet BorderPane représentant la mise en page de la page d'accueil.
     */
    public BorderPane showPageAccueil(){
        BorderPane borderPane = new BorderPane();
        TextField messageInput = new TextField("Voici la liste des messages");
        Scene scene = new Scene(borderPane, 850, 600);
        stage.setScene(scene);
        stage.show();
    }
}
