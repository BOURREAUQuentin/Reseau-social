import java.util.HashMap;
import java.util.Map;
import bd.UtilisateurSQL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import src.Client;

public class ControleurBoutonConnexion implements EventHandler<ActionEvent>{
    private PageInscription appliInscription;
    private PageConnexion appliConnexion;
    private UtilisateurSQL utilisateurSQL;

    public ControleurBoutonConnexion(PageInscription appliInscription){
        this.appliInscription = appliInscription;
        this.appliConnexion = null;
        this.utilisateurSQL = new UtilisateurSQL();
    }

    public ControleurBoutonConnexion(PageConnexion appliConnexion){
        this.appliConnexion = appliConnexion;
        this.appliInscription = null;
        this.utilisateurSQL = new UtilisateurSQL();
    }

    @Override
    public void handle(ActionEvent event){
        Button button = (Button) (event.getSource());
        Map<String, Node> map = new HashMap<String, Node>();
        if (this.appliInscription == null){
            map = this.appliConnexion.getLesElementsGraphiques();
        }
        else{
            map = this.appliInscription.getLesElementsGraphiques();
        }
        TextField tfNom = (TextField) map.get("tfNomUtilisateur");
        TextField passwdf = (TextField) map.get("passwdf");
        String nomUtilisateur = tfNom.getText();
        String motDePasse = passwdf.getText();
        try{
            Utilisateur utilisateurConnecte = this.utilisateurSQL.getUtilisateur(nomUtilisateur, motDePasse);
            if (utilisateurConnecte != null){
                Client client = new Client(nomUtilisateur);
                client.connexionServeur();
                MainClient.getInstance().setUtilisateurConnecte(utilisateurConnecte);
                PageAccueil pageAccueil = null;
                if (this.appliInscription == null){
                    pageAccueil = new PageAccueil(this.appliConnexion.getStage(), client);
                }
                else{
                    pageAccueil = new PageAccueil(this.appliInscription.getStage(), client);
                }
                pageAccueil.showPageAccueil();
            }
            else {
                showAlert("Erreur de connexion", "Vos identifiants ne sont pas corrects.");
            }
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
