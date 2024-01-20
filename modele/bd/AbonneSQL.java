import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.annotation.Documented;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modele.java.Utilisateur;

/**
 * La classe AbonneSQL gère l'accès à la base de données pour les abonnements d'utilisateurs.
 * Elle fournit des méthodes pour récupérer la liste des utilisateurs auxquels un utilisateur est abonné,
 * la liste des utilisateurs qui sont abonnés à un utilisateur donné,
 * la liste des utilisateurs qui ne sont pas encore abonnés à un utilisateur donné,
 * ajouter un abonnement, et supprimer un abonnement.
 */
public class AbonneSQL{
    
    public AbonneSQL(){
    }

    /**
     * Récupère la liste des utilisateurs auxquels un utilisateur est abonné.
     *
     * @param nomUtilisateur Le nom de l'utilisateur pour lequel récupérer les abonnements.
     * @return Une liste d'utilisateurs auxquels l'utilisateur est abonné.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Récupère l'identifiant d'un utilisateur à partir de son nom d'utilisateur.
     *
     * @param nomUtilisateur Le nom de l'utilisateur pour lequel récupérer l'identifiant.
     * @return L'identifiant de l'utilisateur, ou 0 si l'utilisateur n'est pas trouvé.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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
 
    /**
     * Récupère la liste des utilisateurs qui sont abonnés à un utilisateur donné.
     *
     * @param nomUtilisateur Le nom de l'utilisateur pour lequel récupérer les abonnés.
     * @return Une liste d'utilisateurs qui sont abonnés à l'utilisateur.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Récupère la liste des utilisateurs qui ne sont pas encore abonnés à un utilisateur donné.
     *
     * @param nomUtilisateur Le nom de l'utilisateur pour lequel récupérer les non-abonnés.
     * @return Une liste d'utilisateurs qui ne sont pas encore abonnés à l'utilisateur.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Ajoute un abonnement entre deux utilisateurs.
     *
     * @param nomUtilisateur L'utilisateur qui souhaite s'abonner.
     * @param nomUtilisateurCible L'utilisateur auquel l'abonnement est destiné.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Supprime un abonnement entre deux utilisateurs.
     *
     * @param nomUtilisateur L'utilisateur qui souhaite annuler son abonnement.
     * @param nomUtilisateurCible L'utilisateur auquel l'abonnement est annulé.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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