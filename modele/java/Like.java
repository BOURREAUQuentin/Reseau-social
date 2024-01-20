/**
* La classe Like représente une action de "like" effectuée par un utilisateur sur un message.
*/
public class Like{
    // Identifiant du message aimé.
    private int idMessage;
    // Identifiant de l'utilisateur ayant effectué le "like".
    private int idUtilisateur;

    /**
     * Constructeur de la classe Like.
     *
     * @param idMessage L'identifiant du message aimé.
     * @param idUtilisateur L'identifiant de l'utilisateur ayant effectué le "like".
     */
    public Like(int idMessage, int idUtilisateur){
        this.idMessage = idMessage;
        this.idUtilisateur = idUtilisateur;
    }

    /**
     * Obtient l'identifiant du message aimé.
     *
     * @return L'identifiant du message aimé.
     */
    public int getIdMessage(){
        return this.idMessage;
    }

    /**
     * Obtient l'identifiant de l'utilisateur ayant effectué le "like".
     *
     * @return L'identifiant de l'utilisateur ayant effectué le "like".
     */
    public int getIdUtilisateur(){
        return this.idUtilisateur;
    }
}