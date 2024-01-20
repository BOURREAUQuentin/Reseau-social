public class Message {
    private int idMessage;
    private String contenu;
    private String date;
    private int idUtilisateur;
    private String nomUtilisateur;

    public Message(int idMessage, String contenu, String date, int idUtilisateur, String nomUtilisateur) {
        this.idMessage = idMessage;
        this.contenu = contenu;
        this.date = date;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public String getContenu() {
        return contenu;
    }

    public String getDate(){
        return date;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public String getNomUtilisateur(){
        return nomUtilisateur;
    }
}
