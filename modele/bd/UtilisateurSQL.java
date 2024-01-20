import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtilisateurSQL {

    public UtilisateurSQL(){
    }

    public static int prochainIdUtilisateur() throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT max(idU) maxId FROM UTILISATEUR");
            ResultSet rs = ps.executeQuery();
            int maxIdUtilisateurActuel = 0;
            if (rs.next()) {
                maxIdUtilisateurActuel = rs.getInt("maxId");
            }
            return maxIdUtilisateurActuel + 1;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Utilisateur> getLesUtilisateurs() throws ClassNotFoundException{
        List<Utilisateur> lesUtilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps= MainClient.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Utilisateur utilisateur = new Utilisateur(rs.getInt(1), rs.getString(2), rs.getString(3));
                lesUtilisateurs.add(utilisateur);
            }
            return lesUtilisateurs;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return lesUtilisateurs;
        }
    }

    public static Utilisateur getUtilisateur(String nomUtilisateur, String motDePasse) throws ClassNotFoundException{
        try{
            PreparedStatement ps= MainClient.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR where nomUtilisateur = ? AND mdpU = ?");
            ps.setString(1, nomUtilisateur);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Utilisateur utilisateur = new Utilisateur(rs.getInt(1), rs.getString(2), rs.getString(3));
                return utilisateur;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Utilisateur getUtilisateurParNomUtilisateur(String nomUtilisateur) throws ClassNotFoundException{
        try{
            PreparedStatement ps= MainClient.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR where nomUtilisateur = ?");
            ps.setString(1, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Utilisateur utilisateur = new Utilisateur(rs.getInt(1), rs.getString(2), rs.getString(3));
                return utilisateur;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean ajouterUtilisateur(String nomUtilisateur, String motDePasse) throws ClassNotFoundException{
        try{
            if (utilisateurExiste(nomUtilisateur)){
                return false;
            }
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("INSERT INTO UTILISATEUR (idU, nomUtilisateur, mdpU) values (?,?,?)");
            ps.setInt(1, prochainIdUtilisateur());
            ps.setString(2, nomUtilisateur);
            ps.setString(3, motDePasse);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean utilisateurExiste(String nomUtilisateur) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT nomUtilisateur FROM UTILISATEUR where nomUtilisateur = ?");
            ps.setString(1, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return true;
            }
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean supprimerUtilisateur(String nomUtilisateur) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("DELETE FROM UTILISATEUR WHERE nomUtilisateur = ?");
            ps.setString(1, nomUtilisateur);
            ps.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
