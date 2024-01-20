import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurRetourInscription implements EventHandler<ActionEvent>{
    private PageConnexion appli;

    public ControleurRetourInscription(PageConnexion appli){
        this.appli = appli;
    }

    @Override
    public void handle(ActionEvent event){
        PageInscription pageInscription = new PageInscription(this.appli.getStage());
        pageInscription.showPageInscription();
    }
}