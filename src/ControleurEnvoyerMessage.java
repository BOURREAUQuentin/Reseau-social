import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.Button;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton envoyer un message.
 */
public class ControleurEnvoyerMessage implements EventHandler<ActionEvent> {
    /**
     * vue du reseau social
     **/
    private ClientModele clientModele;

    /**
      * @param modelePendu modèle du reseau social
    * @param p vue du reseau social
    */
    public ControleurEnvoyerMessage(ClientModele client) {
         this.clientModele = clientModele;
    }

    /**
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button bEnvoyerMessage = (Button) actionEvent.getSource();
        if (bEnvoyerMessage.getText().equals("Envoyer")){
            this.clientModele.sendMessage();
        }
        else{
            System.out.println("Erreur lors de l'envoi du message");
        }
    }
}
