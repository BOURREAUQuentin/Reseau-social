import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;

import java.net.Socket;
import java.util.Optional;
import javafx.scene.control.Button;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton envoyer un message.
 */
public class ControleurAbonner implements EventHandler<ActionEvent> {
    /**
     * vue du reseau social
     **/
    private ClientModele clientModele;
    private ClientFX clientFX;
    private ServeurModele serveurModele;
    private ServeurFX serveurFX;
    private Socket clientSocket;

    public ControleurAbonner(ClientFX clientFX, ClientModele clientModele, ServeurFX serveurFX, ServeurModele serveurModele, Socket clientSocket) {
         this.clientModele = clientModele;
         this.serveurModele = serveurModele;
         this.clientFX = clientFX;
         this.serveurFX = serveurFX;
         this.clientSocket = clientSocket;
    }

    /**
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button bEnvoyerMessage = (Button) actionEvent.getSource();
        if (bEnvoyerMessage.getText().equals("S'abonner")){
            //this.clientModele.toggleAbonne();
            this.serveurModele.handleClient(this.clientSocket);
            //mettre à jour l'affichage de la vue
        }
        else{
            System.out.println("Erreur lors de l'abonnement");
        }
    }
}
