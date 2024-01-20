package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeSQL {
    /** connexion à la base de donnée */
    private Connection connexion = Connexion.laConnexion;

    public LikeSQL(){
    }

    public int getIdClient(String pseudoClient){
        try{
            PreparedStatement ps = connexion.prepareStatement("select idC from CLIENT where nomUtilisateurC = ?");
            ps.setString(1, pseudoClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("idC");
            }
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void ajouterLike(int idMessage, String nomUtilisateurClient){
        try{
            int idClient = this.getIdClient(nomUtilisateurClient);
            PreparedStatement ps2 = connexion.prepareStatement("insert into LIKE (idM, idC) values (?, ?)");
            ps2.setInt(1, idMessage);
            ps2.setInt(2, idClient);
            ps2.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void supprimerLike(int idMessage, String nomUtilisateurClient){
        try{
            int idClient = this.getIdClient(nomUtilisateurClient);
            PreparedStatement ps = connexion.prepareStatement("delete from LIKE where idM = ? and idC = ?");
            ps.setInt(1, idMessage);
            ps.setInt(2, idClient);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int nbLikesMessage(int idMessage){
        int nbLikes = 0;
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT count(*) nbLikes FROM LIKE where idM = ?");
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
}
