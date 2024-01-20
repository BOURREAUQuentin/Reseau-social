/**
 * La classe Utilisateur représente un utilisateur dans un réseau social.
 */
public class Utilisateur {
    // Identifiant de l'utilisateur.
    private int idU;
    // Nom de l'utilisateur.
    private String nomUtilisateur;
    // Mot de passe de l'utilisateur.
    private String mdpU;

    /**
     * Constructeur de la classe Utilisateur.
     *
     * @param id L'identifiant de l'utilisateur.
     * @param nomUtilisateur Le nom de l'utilisateur.
     * @param mdp Le mot de passe de l'utilisateur.
     */
    public Utilisateur(int id, String nomUtilisateur, String mdp) {
        this.idU = id;
        this.nomUtilisateur = nomUtilisateur;
        this.mdpU = mdp;
    }

    /**
     * Obtient l'identifiant de l'utilisateur.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public int getIdUtilisateur(){
        return this.idU;
    }

    /**
     * Obtient le nom de l'utilisateur.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    /**
     * Obtient le mot de passe de l'utilisateur.
     *
     * @return Le mot de passe de l'utilisateur.
     */
    public String getMdp(){
        return this.mdpU;
    }
    
    /**
     * Redéfinition de la méthode toString pour obtenir une représentation textuelle de l'utilisateur.
     *
     * @return Le nom de l'utilisateur.
     */
    @Override
    public String toString() {
        return nomUtilisateur;
    }
}
