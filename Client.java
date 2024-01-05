import java.util.ArrayList;
import java.util.List;

public class Client {
    private String nomUtilisateur;
    private List<Client> listeAbonnes;

    public Client(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
        this.listeAbonnes = new ArrayList<>();
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public List<Client> getListeAbonnes() {
        return listeAbonnes;
    }

    public void ajouterAbonne(Client abonne) {
        listeAbonnes.add(abonne);
        System.out.println(this.nomUtilisateur+" a ses abonnes : "+listeAbonnes);
    }

    public void supprimerAbonne(Client abonne) {
        listeAbonnes.remove(abonne);
        System.out.println(this.nomUtilisateur+" a ses abonnes : "+listeAbonnes);
    }

    @Override
    public String toString() {
        return nomUtilisateur;
    }
}
