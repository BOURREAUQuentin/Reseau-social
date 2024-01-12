import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;

import java.net.Socket;
import java.util.Optional;
import javafx.scene.control.Button;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton envoyer un message.
 */
public class ControleurDesabonner implements EventHandler<ActionEvent> {
    /**
     * vue du reseau social
     **/
    private ClientModele clientModele;
    private ServeurModele serveurModele;
    private ClientFX clientFX;
    private Socket clientSocket;

    /**
      * @param modelePendu modèle du reseau social
    * @param p vue du reseau social
    */
    public void ControleurDesabonner(ClientModele client, ServeurModele serveur, ClientFX clientFX, Socket clientSocket){
         this.clientModele = client;
         this.serveurModele = serveur;
         this.clientFX = clientFX;
         this.clientSocket = clientSocket;
    }

    /**
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button bEnvoyerMessage = (Button) actionEvent.getSource();
        if (bEnvoyerMessage.getText().equals("Se désabonner")){
            //this.clientModele.unsubscribeClient()
            this.serveurModele.handleClient(this.clientSocket);
            // rafraichir la vue du client
        }
        else{
            System.out.println("Erreur lors du désabonnement");
        }
    }
}
