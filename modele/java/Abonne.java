import java.util.List;
import java.util.ArrayList;

/**
 * La classe Abonne représente la relation d'abonnement entre deux utilisateurs dans un réseau social.
 */
public class Abonne{
    // Identifiant de l'utilisateur abonné à un autre utilisateur.
    private int abonnementA;
    // Identifiant de l'utilisateur qui est abonné.
    private int abonneA;
    // Liste des utilisateurs abonnés à l'utilisateur.
    private List<Utilisateur> listeAbonnes;

    /**
     * Constructeur de la classe Abonne.
     *
     * @param abonnementA L'identifiant de l'utilisateur abonné.
     * @param abonneA L'identifiant de l'utilisateur qui est abonné.
     */
    public Abonne(int abonnementA, int abonneA){
        this.abonnementA = abonnementA;
        this.abonneA = abonneA;
        this.listeAbonnes = new ArrayList<>();
    }

    /**
     * Obtient l'identifiant de l'utilisateur abonné.
     *
     * @return L'identifiant de l'utilisateur abonné.
     */
    public int getAbonnementA(){
        return this.abonnementA;
    }

    /**
     * Obtient l'identifiant de l'utilisateur qui est abonné.
     *
     * @return L'identifiant de l'utilisateur qui est abonné.
     */
    public int getAbonneA(){
        return this.abonneA;
    }

    /**
     * Ajoute un utilisateur à la liste des abonnés.
     *
     * @param abonne L'utilisateur à ajouter à la liste des abonnés.
     */
    public void ajouterAbonne(Utilisateur abonne){
        this.listeAbonnes.add(abonne);
    }

    /**
     * Supprime un utilisateur de la liste des abonnés.
     *
     * @param utilisateur L'utilisateur à supprimer de la liste des abonnés.
     */
    public void supprimerAbonne(Utilisateur utilisateur){
        this.listeAbonnes.remove(utilisateur);
    }
}