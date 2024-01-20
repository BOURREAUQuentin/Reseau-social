public class Like{
    private int idMessage;
    private int idClient;

    public Like(int idMessage, int idClient){
        this.idMessage = idMessage;
        this.idClient = idClient;
    }

    public int getIdMessage(){
        return this.idMessage;
    }

    public int idClient(){
        return this.idClient;
    }
}