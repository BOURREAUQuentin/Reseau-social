import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Le ControleurRetourInscription est un gestionnaire d'événements pour le bouton de retour à l'inscription.
 * Il est utilisé pour traiter les actions liées au retour à la page d'inscription.
 */
public class ControleurRetourInscription implements EventHandler<ActionEvent>{
    private PageConnexion appli;

    /**
     * Constructeur pour une instance associée à la page de connexion.
     *
     * @param appli La page de connexion associée.
     */
    public ControleurRetourInscription(PageConnexion appli){
        this.appli = appli;
    }

    /**
     * Gère l'événement déclenché lorsqu'un utilisateur appuie sur le bouton de retour à l'inscription.
     *
     * @param event L'événement d'action déclenché.
     */
    @Override
    public void handle(ActionEvent event){
        PageInscription pageInscription = new PageInscription(this.appli.getStage());
        pageInscription.showPageInscription();
    }
}