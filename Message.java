import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * La classe Message représente un message dans une application de messagerie.
 * Chaque message a un identifiant unique, un contenu, une date, une heure, un nombre de likes et le nom de l'utilisateur
 * qui l'a créé.
 */
public class Message {
    private static int nextId = 1;

    private int idMessage;
    private String contenu;
    private LocalDate date;
    private LocalTime heure;
    private int nbLikes;
    private String nomUtilisateur;

    /**
     * Initialise une nouvelle instance de la classe Message avec le contenu du message et le nom de l'utilisateur.
     *
     * @param contenu Le contenu du message.
     * @param nomUtilisateur Le nom de l'utilisateur qui a créé le message.
     */
    public Message(String contenu, String nomUtilisateur) {
        this.idMessage = nextId++;
        this.contenu = contenu;
        LocalDateTime now = LocalDateTime.now();
        this.date = now.toLocalDate();
        this.heure = now.toLocalTime();
        this.nbLikes = 0;
        this.nomUtilisateur = nomUtilisateur;
    }

    /**
     * Retourne l'identifiant unique du message.
     *
     * @return L'identifiant unique du message.
     */
    public int getIdMessage() {
        return idMessage;
    }

    /**
     * Retourne le contenu du message.
     *
     * @return Le contenu du message.
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Retourne la date à laquelle le message a été créé.
     *
     * @return La date du message.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retourne l'heure à laquelle le message a été créé.
     *
     * @return L'heure du message.
     */
    public LocalTime getHeure() {
        return heure;
    }

    /**
     * Retourne le nombre de likes que le message a reçus.
     *
     * @return Le nombre de likes du message.
     */
    public int getNbLikes() {
        return nbLikes;
    }

    /**
     * Retourne le nom de l'utilisateur qui a créé le message.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    /**
     * Incrémente le nombre de likes du message.
     */
    public void likeMessage(){
        nbLikes++;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du message.
     *
     * @return Une chaîne de caractères représentant le message.
     */
    @Override
    public String toString() {
        return " '" + contenu + "' | date: " + heure + " le " + date + " | nbLikes: " + nbLikes;
    }
}

