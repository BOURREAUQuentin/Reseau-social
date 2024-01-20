import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurRetourConnexion implements EventHandler<ActionEvent>{
    private PageInscription appli;

    public ControleurRetourConnexion(PageInscription appli){
        this.appli = appli;
    }

    @Override
    public void handle(ActionEvent event){
        PageConnexion pageConnexion = new PageConnexion(this.appli.getStage());
        pageConnexion.showPageConnexion();
    }
}
