import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;

public class MainClient extends Application {
    private Utilisateur utilisateurConnecte;
    private static MainClient instanceClient;
    private ConnexionBD connexion;

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
    public void init() throws SQLException, ClassNotFoundException {
        instanceClient = this;
        this.utilisateurConnecte = null;
        try {
            if (connexion == null) {
                connexion = new ConnexionBD();
                connexion.connect("localhost", "SAE", "quentin", "quentin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ConnexionBD getSqlConnect() throws ClassNotFoundException {
        try {
            if (connexion == null) {
                connexion = new ConnexionBD();
                connexion.connect("localhost", "SAE", "quentin", "quentin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connexion;
    }

    @Override
    public void start(Stage primaryStage) {
        PageConnexion pageConnexion = new PageConnexion(primaryStage);
        pageConnexion.showPageConnexion();
    }

    public static void main(String[] args) {
        // lancement de l'application JavaFX cliente
        launch(args);
    }
}
