import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Le ControleurBoutonInscription est un gestionnaire d'événements pour le bouton d'inscription.
 * Il est utilisé pour traiter les actions liées à l'inscription à l'application.
 */
public class ControleurBoutonInscription implements EventHandler<ActionEvent>{

    private PageInscription appli;
    private UtilisateurSQL utilisateurSQL;

    /**
     * Constructeur pour une instance associée à la page d'inscription.
     *
     * @param appli La page d'inscription associée.
     */
    public ControleurBoutonInscription(PageInscription appli){
        this.appli = appli;
        this.utilisateurSQL = new UtilisateurSQL();
    }

    /**
     * Gère l'événement déclenché lorsqu'un utilisateur appuie sur le bouton d'inscription.
     *
     * @param event L'événement d'action déclenché.
     */
    @Override
    public void handle(ActionEvent event){
        Button button = (Button) (event.getSource());
        Map<String, Node> map = this.appli.getLesElementsGraphiques();
        TextField tfNom = (TextField) map.get("tfNomUtilisateur");
        TextField passwdf = (TextField) map.get("passwdf");
        String nomUtilisateur = tfNom.getText();
        String motDePasse = passwdf.getText();
        Boolean clientExiste = false;
        try {
            clientExiste = this.utilisateurSQL.ajouterUtilisateur(nomUtilisateur, motDePasse);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clientExiste){
            try{
                Utilisateur utilisateurConnecte = this.utilisateurSQL.getUtilisateur(nomUtilisateur, motDePasse);
                if (utilisateurConnecte != null){
                    Client client = new Client(nomUtilisateur);
                    client.connexionServeur();
                    MainClient.getInstance().setUtilisateurConnecte(utilisateurConnecte);
                    PageAccueil pageAccueil = new PageAccueil(this.appli.getStage(), client);
                    pageAccueil.showPageAccueil();
                }
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        else {
            showAlert("Erreur d'inscription", "Votre nom d'utilisateur est déjà utilisé. Choisissez un nouveau nom d'utilisateur.");
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte avec le titre et le contenu spécifiés.
     *
     * @param titre    Le titre de l'alerte.
     * @param contenu  Le contenu de l'alerte.
     */
    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
