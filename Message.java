import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Message {
    private static int nextId = 1;

    private int idMessage;
    private String contenu;
    private LocalDate date;
    private LocalTime heure;
    private int nbLikes;
    private String nomUtilisateur;

    public Message(String contenu, String nomUtilisateur) {
        this.idMessage = nextId++;
        this.contenu = contenu;
        LocalDateTime now = LocalDateTime.now();
        this.date = now.toLocalDate();
        this.heure = now.toLocalTime();
        this.nbLikes = 0;
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public String getContenu() {
        return contenu;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public int getNbLikes() {
        return nbLikes;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void likeMessage(){
        nbLikes++;
    }

    @Override
    public String toString() {
        return " '" + contenu + "' | date: " + heure + " le " + date + " | nbLikes: " + nbLikes;
    }
}

