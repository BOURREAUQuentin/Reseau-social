/**
 * La classe Message représente un message posté par un utilisateur dans un réseau social.
 */
public class Message {
    // Identifiant du message.
    private int idMessage;
    // Contenu textuel du message.
    private String contenu;
    // Date et heure de création du message.
    private String date;
    // Identifiant de l'utilisateur ayant posté le message.
    private int idUtilisateur;
    // Nom de l'utilisateur ayant posté le message.
    private String nomUtilisateur;

    /**
     * Constructeur de la classe Message.
     *
     * @param idMessage L'identifiant du message.
     * @param contenu Le contenu textuel du message.
     * @param date La date et l'heure de création du message.
     * @param idUtilisateur L'identifiant de l'utilisateur ayant posté le message.
     * @param nomUtilisateur Le nom de l'utilisateur ayant posté le message.
     */
    public Message(int idMessage, String contenu, String date, int idUtilisateur, String nomUtilisateur) {
        this.idMessage = idMessage;
        this.contenu = contenu;
        this.date = date;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
    }

    /**
     * Obtient l'identifiant du message.
     *
     * @return L'identifiant du message.
     */
    public int getIdMessage() {
        return idMessage;
    }

    /**
     * Obtient le contenu textuel du message.
     *
     * @return Le contenu textuel du message.
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Obtient la date et l'heure de création du message.
     *
     * @return La date et l'heure de création du message.
     */
    public String getDate(){
        return date;
    }

    /**
     * Obtient l'identifiant de l'utilisateur ayant posté le message.
     *
     * @return L'identifiant de l'utilisateur ayant posté le message.
     */
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    /**
     * Obtient le nom de l'utilisateur ayant posté le message.
     *
     * @return Le nom de l'utilisateur ayant posté le message.
     */
    public String getNomUtilisateur(){
        return nomUtilisateur;
    }
}
