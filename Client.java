import java.util.ArrayList;
import java.util.List;

/**
 * La classe Client représente un utilisateur de l'application.
 * Chaque client a un nom d'utilisateur, une liste d'abonnés et une liste de messages.
 */
public class Client {
    private String nomUtilisateur;
    private List<Client> listeAbonnes;
    private List<Message> mesMessages;

    /**
     * Constructeur de la classe Client.
     *
     * @param nomUtilisateur Le nom d'utilisateur du client.
     */
    public Client(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
        this.listeAbonnes = new ArrayList<>();
        this.mesMessages = new ArrayList<>();
    }

    /**
     * Obtient le nom d'utilisateur du client.
     *
     * @return Le nom d'utilisateur.
     */
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    /**
     * Obtient la liste d'abonnés du client.
     *
     * @return La liste d'abonnés.
     */
    public List<Client> getListeAbonnes() {
        return listeAbonnes;
    }

    /**
     * Obtient la liste des messages du client.
     *
     * @return La liste des messages.
     */
    public List<Message> getMesMessages() {
        return mesMessages;
    }

    /**
     * Ajoute un abonné à la liste d'abonnés du client.
     *
     * @param abonne Le client à ajouter comme abonné.
     */
    public void ajouterAbonne(Client abonne) {
        listeAbonnes.add(abonne);
        System.out.println(this.nomUtilisateur + " a ses abonnes : " + listeAbonnes);
    }

    /**
     * Ajoute un message à la liste des messages du client.
     *
     * @param nouveauMessage Le nouveau message à ajouter.
     */
    public void ajouterMessage(Message nouveauMessage) {
        mesMessages.add(nouveauMessage);
    }

    /**
     * Supprime un abonné de la liste d'abonnés du client.
     *
     * @param abonne Le client à supprimer des abonnés.
     */
    public void supprimerAbonne(Client abonne) {
        listeAbonnes.remove(abonne);
        System.out.println(this.nomUtilisateur + " a ses abonnes : " + listeAbonnes);
    }

    /**
     * Supprime un message de la liste des messages du client.
     *
     * @param message Le message à supprimer.
     */
    public void supprimerMessage(Message message) {
        mesMessages.remove(message);
    }

    /**
     * Retourne une représentation textuelle du client (son nom d'utilisateur).
     *
     * @return Le nom d'utilisateur du client.
     */
    @Override
    public String toString() {
        return nomUtilisateur;
    }
}
