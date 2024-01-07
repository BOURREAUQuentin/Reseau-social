import java.util.ArrayList;
import java.util.List;

public class Client {
    private String nomUtilisateur;
    private List<Client> listeAbonnes;
    private List<Message> mesMessages;

    public Client(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
        this.listeAbonnes = new ArrayList<>();
        this.mesMessages = new ArrayList<>();
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public List<Client> getListeAbonnes() {
        return listeAbonnes;
    }

    public List<Message> getMesMessages() {
        return mesMessages;
    }

    public void ajouterAbonne(Client abonne) {
        listeAbonnes.add(abonne);
        System.out.println(this.nomUtilisateur + " a ses abonnes : " + listeAbonnes);
    }

    public void ajouterMessage(Message nouveauMessage) {
        mesMessages.add(nouveauMessage);
    }

    public void supprimerAbonne(Client abonne) {
        listeAbonnes.remove(abonne);
        System.out.println(this.nomUtilisateur + " a ses abonnes : " + listeAbonnes);
    }

    public void supprimerMessage(Message message) {
        mesMessages.remove(message);
    }

    @Override
    public String toString() {
        return nomUtilisateur;
    }
}
