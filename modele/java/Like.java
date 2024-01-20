public class Like{
    private int idMessage;
    private int idUtilisateur;

    public Like(int idMessage, int idUtilisateur){
        this.idMessage = idMessage;
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdMessage(){
        return this.idMessage;
    }

    public int getIdUtilisateur(){
        return this.idUtilisateur;
    }
}