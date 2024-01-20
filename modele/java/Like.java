package java;

public class Like{
    private Message message;
    private Utilisateur utilisateur;

    public Like(Message message, Utilisateur utilisateur){
        this.message = message;
        this.utilisateur = utilisateur;
    }

    public int getIdMessage(){
        return this.message.getidMessage();
    }

    public int getIdUtilisateur(){
        return this.utilisateur.getIdUtilisateur();
    }
}