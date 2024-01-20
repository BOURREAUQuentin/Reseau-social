package src;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainClient extends Application {
    private Utilisateur utilisateurConnecte;
    private static MainClient instanceClient;

    public static MainClient getInstance() {
        if (instanceClient == null) {
            instanceClient = new MainClient();
        }
        return instanceClient;
    }

    public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }

    public Utilisateur getUtilisateurConnecte() {
        return this.utilisateurConnecte;
    }

    @Override
    public void start(Stage stage){
        // TODO
    }

    @Override
    public void start(Stage primaryStage) {
        PageConnexion pageConnexion = new PageConnexion(primaryStage);
        pageConnexion.show();
    }

    public static void main(String[] args) {
        // lancement de l'application JavaFX cliente
        launch(args);
    }
}
