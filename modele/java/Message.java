package java;

public class Message {
    private int idMessage;
    private String contenu;
    private String date;
    private Utilisateur utilisateur;

    public Message(int idMessage, String contenu, String date, Utilisateur utilisateur) {
        this.idMessage = idMessage;
        this.contenu = contenu;
        this.date = date;
        this.utilisateur = utilisateur;
    }

    public int getidMessage() {
        return this.idMessage;
    }

    public String getContenu() {
        return contenu;
    }

    public String getDate(){
        return date;
    }

    public int getIdUtilisateur() {
        return this.utilisateur.getIdUtilisateur();
    }

    public String getNomUtilisateur(){
        return this.utilisateur.getNomUtilisateur();
    }
}
