import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeSQL {

    public LikeSQL(){
    }

    private static int getIdUtilisateur(String pseudoClient) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("select idU from UTILISATEUR where nomUtilisateur = ?");
            ps.setString(1, pseudoClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("idU");
            }
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void ajouterLike(int idMessage, String nomUtilisateurClient) throws ClassNotFoundException{
        try{
            int idClient = getIdUtilisateur(nomUtilisateurClient);
            PreparedStatement ps2 = MainClient.getInstance().getSqlConnect().prepareStatement("insert into LIKES (idM, idU) values (?, ?)");
            ps2.setInt(1, idMessage);
            ps2.setInt(2, idClient);
            ps2.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void supprimerLike(int idMessage, String nomUtilisateurClient) throws ClassNotFoundException{
        try{
            int idClient = getIdUtilisateur(nomUtilisateurClient);
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("delete from LIKES where idM = ? and idU = ?");
            ps.setInt(1, idMessage);
            ps.setInt(2, idClient);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int nbLikesMessage(int idMessage) throws ClassNotFoundException{
        int nbLikes = 0;
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT count(*) nbLikes FROM LIKES where idM = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                nbLikes = rs.getInt("nbLikes");
            }
            return nbLikes;
        }
        catch(SQLException e){
            e.printStackTrace();
            return nbLikes;
        }
    }

    public static int nbLikesMessageParUtilisateur(int idMessage, int idUtilisateur) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT count(*) nbLikes FROM LIKES where idM = ? and idU = ?");
            ps.setInt(1, idMessage);
            ps.setInt(2, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt("nbLikes");
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return cpt;
        }
    }
}
