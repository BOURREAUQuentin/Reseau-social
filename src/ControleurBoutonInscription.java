import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import bd.ClientSQL;
import src.Client;
import src.MainClient;

public class ControleurBoutonInscription implements EventHandler<ActionEvent>{

    private PageInscription appli;
    private ClientSQL clientSQL;

    public ControleurBoutonInscription(PageInscription appli){
        this.appli = appli;
        this.clientSQL = new ClientSQL();
    }

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
            clientExiste = this.clientSQL.ajouterClient(nomUtilisateur, motDePasse);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clientExiste){
            try{
                Client clientConnecte = this.clientSQL.getClient(nomUtilisateur, motDePasse);
                if (clientConnecte != null){
                    Client client = new Client(nomUtilisateur);
                    client.connexionServeur();
                    MainClient.getInstance().setClientConnecte(clientConnecte);
                    PageAccueil pageAccueil = new PageAccueil(this.appli.getStage(), client);
                    pageAccueil.show();
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

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
