import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * La classe UtilisateurSQL gère les opérations liées aux utilisateurs dans une base de données.
 */
public class UtilisateurSQL {

    public UtilisateurSQL(){
    }

    /**
     * Récupère le prochain identifiant disponible pour un nouvel utilisateur.
     *
     * @return Le prochain identifiant disponible.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Récupère la liste de tous les utilisateurs de la base de données.
     *
     * @return Une liste d'objets Utilisateur représentant tous les utilisateurs.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Récupère un utilisateur à partir de son nom d'utilisateur et de son mot de passe.
     *
     * @param nomUtilisateur Le nom d'utilisateur de l'utilisateur.
     * @param motDePasse Le mot de passe de l'utilisateur.
     * @return Un objet Utilisateur correspondant aux informations fournies, ou null si non trouvé.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Récupère un utilisateur à partir de son nom d'utilisateur.
     *
     * @param nomUtilisateur Le nom d'utilisateur de l'utilisateur.
     * @return Un objet Utilisateur correspondant au nom d'utilisateur, ou null si non trouvé.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Ajoute un nouvel utilisateur à la base de données.
     *
     * @param nomUtilisateur Le nom d'utilisateur du nouvel utilisateur.
     * @param motDePasse Le mot de passe du nouvel utilisateur.
     * @return True si l'ajout est réussi, False si l'utilisateur existe déjà.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Vérifie si un utilisateur existe déjà dans la base de données.
     *
     * @param nomUtilisateur Le nom d'utilisateur à vérifier.
     * @return True si l'utilisateur existe, False sinon.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param nomUtilisateur Le nom d'utilisateur de l'utilisateur à supprimer.
     * @return True si la suppression est réussie, False sinon.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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
