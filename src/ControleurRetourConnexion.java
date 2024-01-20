import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Le ControleurRetourConnexion est un gestionnaire d'événements pour le bouton de retour à la connexion.
 * Il est utilisé pour traiter les actions liées au retour à la page de connexion.
 */
public class ControleurRetourConnexion implements EventHandler<ActionEvent>{
    private PageInscription appli;

    /**
     * Constructeur pour une instance associée à la page d'inscription.
     *
     * @param appli La page d'inscription associée.
     */
    public ControleurRetourConnexion(PageInscription appli){
        this.appli = appli;
    }

    /**
     * Gère l'événement déclenché lorsqu'un utilisateur appuie sur le bouton de retour à la connexion.
     *
     * @param event L'événement d'action déclenché.
     */
    @Override
    public void handle(ActionEvent event){
        PageConnexion pageConnexion = new PageConnexion(this.appli.getStage());
        pageConnexion.showPageConnexion();
    }
}
