import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;

public class PageInscription {
    private Stage stage;
    private Map<String, Node> lesElementsGraphiques;

    public PageInscription(Stage stage) {
        this.stage = stage;
        this.lesElementsGraphiques = new HashMap<>();
    }

    public Stage getStage(){
        return this.stage;
    }

    public Map<String, Node> getLesElementsGraphiques(){
        return this.lesElementsGraphiques;
    }

    private GridPane getPanelInscription(){
        GridPane gridPaneInscription = new GridPane();
        gridPaneInscription.setStyle("-fx-background-color: rgb(21, 203, 153)");
        gridPaneInscription.setPadding(new Insets(10, 10, 10, 10));
        gridPaneInscription.setVgap(8);
        gridPaneInscription.setHgap(10);

        Label labelNomUtilisateur = new Label("Nom d'utilisateur : ");
        TextField textFieldNomUtilisateur = new TextField();
        textFieldNomUtilisateur.setPromptText("chhumyLeGoat");
        this.lesElementsGraphiques.put("textFieldNomUtilisateur", textFieldNomUtilisateur);
        Label labelPassword = new Label("Mot de passe : ");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("*********");
        this.lesElementsGraphiques.put("passwordField", passwordField);
        Button boutonInscription = new Button("S'inscrire");
        boutonInscription.setOnAction(new ControleurBoutonInscription(this));
        boutonInscription.setPrefWidth(500);
        boutonInscription.setStyle("-fx-background-color: #424244; -fx-background-radius: 10");
        boutonInscription.setOnMouseEntered(e -> boutonInscription.setStyle("-fx-background-color: #252527; -fx-background-radius: 10")); // style lors du survol
        boutonInscription.setOnMouseExited(e -> boutonInscription.setStyle("-fx-background-color: #424244; -fx-background-radius: 10")); // réinitialisation du style
        boutonInscription.setTextFill(Color.WHITE);
        boutonInscription.setFont(Font.font("Arial", 25));
        Button boutonConnexion = new Button("Déjà un compte ?");
        boutonConnexion.setOnAction(new ControleurRetourConnexion(this));
        boutonConnexion.setPrefWidth(500);
        boutonConnexion.setStyle("-fx-background-color: #424244; -fx-background-radius: 10");
        boutonConnexion.setOnMouseEntered(e -> boutonConnexion.setStyle("-fx-background-color: #252527; -fx-background-radius: 10")); // style lors du survol
        boutonConnexion.setOnMouseExited(e -> boutonConnexion.setStyle("-fx-background-color: #424244; -fx-background-radius: 10")); // réinitialisation du style
        boutonConnexion.setTextFill(Color.WHITE);
        boutonConnexion.setFont(Font.font("Arial", 25));

        // ajout des elements au gridpane
        gridPaneInscription.add(labelNomUtilisateur, 0, 0);
        gridPaneInscription.add(textFieldNomUtilisateur, 0, 1);
        gridPaneInscription.add(labelPassword, 1, 0);
        gridPaneInscription.add(passwordField, 1, 1);
        gridPaneInscription.add(boutonInscription, 2, 0, 2, 1);
        gridPaneInscription.add(boutonConnexion, 3, 0, 2, 1);
        
        return gridPaneInscription;
    }

    public void showPageInscription() {
        stage.setTitle("Inscription");
        GridPane gridPaneInscription = this.getPanelInscription();
        Scene scene = new Scene(gridPaneInscription, 450, 450);
        stage.setScene(scene);
        stage.show();
    }
}