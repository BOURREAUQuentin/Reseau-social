import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbonneSQL{
    
    public AbonneSQL(){
    }

    public static List<Utilisateur> getUtilisateursAbonnements(String nomUtilisateur) throws ClassNotFoundException{
        List<Utilisateur> listeUtilisateurs = new ArrayList<>();
        try{
            int id = getIdUtilisateur(nomUtilisateur);
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT idU, nomUtilisateur, mdpU FROM ABONNE natural join UTILISATEUR where abonnementA = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Utilisateur client = new Utilisateur(rs.getInt(1), rs.getString(2), rs.getString(3));
                listeUtilisateurs.add(client);
            }
            return listeUtilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return listeUtilisateurs;
        }
    }

    private static int getIdUtilisateur(String nomUtilisateur) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("select idU from UTILISATEUR where nomUtilisateur = ?");
            ps.setString(1, nomUtilisateur);
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
 
    public static List<Utilisateur> getUtilisateurAbonnes(String nomUtilisateur) throws ClassNotFoundException{
        List<Utilisateur> listeUtilisateurs = new ArrayList<>();
        try{
            int id = getIdUtilisateur(nomUtilisateur);
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT idU, nomUtilisateur, mdpU FROM ABONNE join UTILISATEUR on ABONNE.abonnementA = UTILISATEUR.idU where ABONNE.abonneA = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Utilisateur utilisateur = new Utilisateur(rs.getInt(1), rs.getString(2), rs.getString(3));
                listeUtilisateurs.add(utilisateur);
            }
            return listeUtilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return listeUtilisateurs;
        }
    }

    public static List<Utilisateur> getUtilisateurNonAbonnes(String nomUtilisateur) throws ClassNotFoundException{
        List<Utilisateur> listeUtilisateurs = new ArrayList<>();
        try{
            int id = getIdUtilisateur(nomUtilisateur);
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR U WHERE U.idU != (SELECT idU FROM UTILISATEUR WHERE nomUtilisateur = ?) AND U.idU NOT IN (SELECT ABONNE.abonnementA FROM ABONNE WHERE ABONNE.abonneA = ?)");
            ps.setString(1, nomUtilisateur);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Utilisateur utilisateur = new Utilisateur(rs.getInt(1), rs.getString(2), rs.getString(3));
                listeUtilisateurs.add(utilisateur);
            }
            return listeUtilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return listeUtilisateurs;
        }
    }

    public static void ajouterAbonnement(String nomUtilisateur, String nomUtilisateurCible) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("INSERT INTO ABONNE (abonnementA, abonneA) values (?,?)");
            int idUtilisateur = getIdUtilisateur(nomUtilisateur);
            int idUtilisateurCible = getIdUtilisateur(nomUtilisateurCible);
            ps.setInt(1, idUtilisateurCible);
            ps.setInt(2, idUtilisateur);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void supprimerAbonnement(String nomUtilisateur, String nomUtilisateurCible) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("DELETE FROM ABONNE where abonnementA = ? AND abonneA = ?");
            int idUtilisateur = getIdUtilisateur(nomUtilisateur);
            int idUtilisateurCible = getIdUtilisateur(nomUtilisateurCible);
            ps.setInt(1, idUtilisateurCible);
            ps.setInt(2, idUtilisateur);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


}