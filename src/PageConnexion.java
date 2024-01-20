import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * La classe PageConnexion représente la page de connexion de l'application.
 * Elle permet aux utilisateurs existants de se connecter à leur compte.
 */
public class PageConnexion {
    private Stage stage;
    private Map<String, Node> lesElementsGraphiques;

    /**
     * Constructeur de la classe PageConnexion.
     *
     * @param stage La fenêtre principale de l'application.
     */
    public PageConnexion(Stage stage) {
        this.stage = stage;
        this.lesElementsGraphiques = new HashMap<>();
    }

    /**
     * Obtient la fenêtre principale de l'application.
     *
     * @return La fenêtre principale.
     */
    public Stage getStage(){
        return this.stage;
    }

    /**
     * Obtient les éléments graphiques de la page de connexion.
     *
     * @return Une map contenant les éléments graphiques.
     */
    public Map<String, Node> getLesElementsGraphiques(){
        return this.lesElementsGraphiques;
    }

    /**
     * Crée et retourne le panneau de connexion avec ses éléments graphiques.
     *
     * @return Un objet GridPane représentant le panneau de connexion.
     */
    private GridPane getPanelConnexion(){
        GridPane gridPaneConnexion = new GridPane();
        gridPaneConnexion.setStyle("-fx-background-color: rgb(21, 203, 153)");
        gridPaneConnexion.setPadding(new Insets(10, 10, 10, 10));
        gridPaneConnexion.setVgap(8);
        gridPaneConnexion.setHgap(10);

        Label labelNomUtilisateur = new Label("Nom d'utilisateur : ");
        TextField textFieldNomUtilisateur = new TextField();
        textFieldNomUtilisateur.setPromptText("chhumyLeGoat");
        this.lesElementsGraphiques.put("tfNomUtilisateur", textFieldNomUtilisateur);
        Label labelPassword = new Label("Mot de passe : ");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("*********");
        this.lesElementsGraphiques.put("passwdf", passwordField);
        Button boutonConnexion = new Button("Se connecter");
        boutonConnexion.setOnAction(new ControleurBoutonConnexion(this));
        boutonConnexion.setPrefWidth(500);
        boutonConnexion.setStyle("-fx-background-color: #424244; -fx-background-radius: 10");
        boutonConnexion.setOnMouseEntered(e -> boutonConnexion.setStyle("-fx-background-color: #252527; -fx-background-radius: 10")); // style lors du survol
        boutonConnexion.setOnMouseExited(e -> boutonConnexion.setStyle("-fx-background-color: #424244; -fx-background-radius: 10")); // réinitialisation du style
        boutonConnexion.setTextFill(Color.WHITE);
        boutonConnexion.setFont(Font.font("Arial", 15));
        Button boutonInscription = new Button("Pas encore de compte ?");
        boutonInscription.setOnAction(new ControleurRetourInscription(this));
        boutonInscription.setPrefWidth(500);
        boutonInscription.setStyle("-fx-background-color: #424244; -fx-background-radius: 10");
        boutonInscription.setOnMouseEntered(e -> boutonInscription.setStyle("-fx-background-color: #252527; -fx-background-radius: 10")); // style lors du survol
        boutonInscription.setOnMouseExited(e -> boutonInscription.setStyle("-fx-background-color: #424244; -fx-background-radius: 10")); // réinitialisation du style
        boutonInscription.setTextFill(Color.WHITE);
        boutonInscription.setFont(Font.font("Arial", 15));

        // ajout des elements au gridpane
        gridPaneConnexion.add(labelNomUtilisateur, 0, 0);
        gridPaneConnexion.add(textFieldNomUtilisateur, 0, 1);
        gridPaneConnexion.add(labelPassword, 0, 2);
        gridPaneConnexion.add(passwordField, 0, 3);
        gridPaneConnexion.add(boutonConnexion, 0, 4, 1, 2);
        gridPaneConnexion.add(boutonInscription, 0, 6, 1, 2);
        
        return gridPaneConnexion;
    }

    /**
     * Affiche la page de connexion de l'application.
     */
    public void showPageConnexion() {
        stage.setTitle("Connexion");
        GridPane gridPaneConnexion = this.getPanelConnexion();
        Scene scene = new Scene(gridPaneConnexion, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
}
