package java;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Message {
    private int id;
    private String contenu;
    private LocalDate date;
    private LocalTime heure;
    private int nbLikes;
    private int idUtilisateur;

    public Message(int id, String contenu, int idUtilisateur) {
        this.id = id;
        this.contenu = contenu;
        LocalDateTime now = LocalDateTime.now();
        this.date = now.toLocalDate();
        this.heure = now.toLocalTime();
        this.nbLikes = 0;
        this.idUtilisateur = idUtilisateur;
    }

    public int getId() {
        return id;
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

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void likeMessage(){
        nbLikes++;
    }
}
