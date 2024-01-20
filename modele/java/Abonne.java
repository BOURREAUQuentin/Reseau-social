import java.util.List;
import java.util.ArrayList;

public class Abonne{
    private int abonnementA;
    private int abonneA;
    private List<Utilisateur> listeAbonnes;

    public Abonne(int abonnementA, int abonneA){
        this.abonnementA = abonnementA;
        this.abonneA = abonneA;
        this.listeAbonnes = new ArrayList<>();
    }

    public int getAbonnementA(){
        return this.abonnementA;
    }

    public int getAbonneA(){
        return this.abonneA;
    }

    public void ajouterAbonne(Utilisateur abonne){
        this.listeAbonnes.add(abonne);
    }

    public void supprimerAbonne(Utilisateur utilisateur){
        this.listeAbonnes.remove(utilisateur);
    }
}