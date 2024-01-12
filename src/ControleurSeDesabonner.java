import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.Button;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton envoyer un message.
 */
public class ControleurSAbonner implements EventHandler<ActionEvent> {
    /**
     * vue du reseau social
     **/
    private Client client;

    /**
      * @param modelePendu modèle du reseau social
    * @param p vue du reseau social
    */
    public void ControleurSeDesabonner(Client client, Serveur serveur, ClientFX vueReseau){
         this.client = client;
         this.serveur = serveur;
         this.vueReseau = vueReseau;
    }

    /**
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button bEnvoyerMessage = (Button) actionEvent.getSource();
        if (bEnvoyerMessage.getText().equals("Se désabonner")){
            this.client.toggleAbonne();
            this.serveur.handleClient();
            // rafraichir la vue du client
        }
        else{
            System.out.println("Erreur lors du désabonnement");
        }
    }
}
