import javafx.application.Application;
import javafx.stage.Stage;

public class MainClient extends Application {
    private Utilisateur utilisateurConnecte;
    private static MainClient instanceClient;
    private Connexion connexion;

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
        instance = this;
        this.utilisateurBd = null;
        try {
            if (sqlConnect == null) {
                sqlConnect = new ConnexionBd();
                sqlConnect.connect("localhost", "SAE_RESEAUX", "temha", "temha1011");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        PageConnexion pageConnexion = new PageConnexion(primaryStage, );
        pageConnexion.showPageConnexion();
    }

    public static void main(String[] args) {
        // lancement de l'application JavaFX cliente
        launch(args);
    }
}
