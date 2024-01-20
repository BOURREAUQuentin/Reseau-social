package src;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainClient extends Application {
    private Client clientConnecte;
    private static MainClient instanceClient;

    public static MainClient getInstance() {
        if (instanceClient == null) {
            instanceClient = new MainClient();
        }
        return instanceClient;
    }

    public void setClientConnecte(Client clientConnecte) {
        this.clientConnecte = clientConnecte;
    }

    public Client getClientConnecte() {
        return this.clientConnecte;
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
