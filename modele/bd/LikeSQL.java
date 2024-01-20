import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * La classe LikeSQL gère les opérations liées aux likes dans une base de données.
 */
public class LikeSQL {

    public LikeSQL(){
    }

    /**
     * Récupère l'identifiant d'un utilisateur à partir de son nom d'utilisateur.
     *
     * @param pseudoClient Le nom d'utilisateur de l'utilisateur.
     * @return L'identifiant de l'utilisateur, ou 0 si l'utilisateur n'est pas trouvé.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Ajoute un like à un message donné par un utilisateur donné.
     *
     * @param idMessage L'identifiant du message.
     * @param nomUtilisateurClient Le nom d'utilisateur du client qui ajoute le like.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Supprime le like d'un message donné par un utilisateur donné.
     *
     * @param idMessage L'identifiant du message.
     * @param nomUtilisateurClient Le nom d'utilisateur du client qui supprime le like.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Récupère le nombre total de likes pour un message donné.
     *
     * @param idMessage L'identifiant du message.
     * @return Le nombre total de likes pour le message.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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

    /**
     * Récupère le nombre de likes d'un utilisateur spécifique pour un message donné.
     *
     * @param idMessage L'identifiant du message.
     * @param idUtilisateur L'identifiant de l'utilisateur.
     * @return Le nombre de likes de l'utilisateur pour le message.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
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
