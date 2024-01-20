import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * La classe MessageSQL gère l'accès à la base de données pour les messages.
 * Elle fournit des méthodes pour récupérer, ajouter et supprimer des messages,
 * ainsi que pour récupérer des listes de messages selon différents critères.
 */
public class MessageSQL {

    public MessageSQL(){
    }

    /**
     * Renvoie le prochain identifiant disponible pour un message dans la base de données.
     *
     * @return Le prochain identifiant de message disponible.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public static int prochainIdMessage() throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT max(idM) maxId FROM MESSAGE");
            ResultSet rs = ps.executeQuery();
            int maxIdMessageActuel = 0;
            if (rs.next()) {
                maxIdMessageActuel = rs.getInt("maxId");
            }
            return maxIdMessageActuel + 1;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Ajoute un nouveau message à la base de données.
     *
     * @param pseudoExpediteur Le pseudo de l'expéditeur du message.
     * @param contenu Le contenu du message.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public static void ajouterMessage(String pseudoExpediteur, String contenu) throws ClassNotFoundException{
        try{
            // requete pour récupérer l'id de l'expéditeur
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT idU FROM UTILISATEUR where nomUtilisateur = ?");
            ps.setString(1, pseudoExpediteur);
            ResultSet rs = ps.executeQuery();
            int idExpediteur = 0;
            if (rs.next()){
                idExpediteur = rs.getInt("idU");
            }

            // requete pour l'ajout de message
            PreparedStatement ps2 = MainClient.getInstance().getSqlConnect().prepareStatement("INSERT INTO MESSAGE (idM, contenuM, dateM, idU) VALUES (?, ?, ?, ?)");
            ps2.setInt(1, prochainIdMessage());
            ps2.setString(2, contenu);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formateur = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeFormate = now.format(formateur);
            ps2.setString(3, dateTimeFormate);
            ps2.setInt(4, idExpediteur);
            ps2.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Récupère un message à partir de son identifiant.
     *
     * @param idMessage L'identifiant du message à récupérer.
     * @return Le message correspondant à l'identifiant, ou null s'il n'est pas trouvé.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public static Message recupererMessageParId(int idMessage) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT idM, contenuM, dateM, idU, nomUtilisateur FROM MESSAGE Natural join UTILISATEUR where idM = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("idM"), rs.getString("contenuM"), rs.getString("dateM"), rs.getInt("idU"), rs.getString("nomUtilisateur"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Supprime un message de la base de données.
     *
     * @param idMessage L'identifiant du message à supprimer.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public static void supprimerMessage(int idMessage) throws ClassNotFoundException{
        try{
            // suppression des lignes dans la table LIKE où l'id message existe (contraintes clés étrangères)
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("DELETE FROM LIKES WHERE idM = ?");
            ps.setInt(1, idMessage);
            ps.executeUpdate();
            // suppression du message
            PreparedStatement ps2 = MainClient.getInstance().getSqlConnect().prepareStatement("DELETE FROM MESSAGE WHERE idM = ?");
            ps2.setInt(1, idMessage);
            ps2.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * Vérifie si un identifiant de message est présent dans la base de données.
     *
     * @param idMessage L'identifiant du message à vérifier.
     * @return true si l'identifiant de message est présent, sinon false.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public static boolean idMessagePresent(int idMessage) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT * FROM MESSAGE WHERE idM = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère un message à partir de sa date, du nom de l'utilisateur et de son contenu.
     *
     * @param date La date du message à récupérer.
     * @param nomUtilisateur Le nom de l'utilisateur associé au message.
     * @param contenu Le contenu du message à récupérer.
     * @return Le message correspondant aux critères spécifiés, ou null s'il n'est pas trouvé.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public static Message recupererMessage(String date, String nomUtilisateur, String contenu) throws ClassNotFoundException{
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT idM, contenuM, dateM, idU, nomUtilisateur FROM MESSAGE natural join UTILISATEUR where contenuM = ? and dateM = ? and nomUtilisateur = ?");
            ps.setString(1, contenu);
            ps.setString(2, date);
            ps.setString(3, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("idM"), rs.getString("contenuM"), rs.getString("dateM"), rs.getInt("idU"), rs.getString("nomUtilisateur"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Récupère une liste de messages triée par date pour un utilisateur spécifié,
     * incluant à la fois ses propres messages et ceux de ses abonnements.
     *
     * @param nomUtilisateur Le nom de l'utilisateur pour lequel récupérer les messages.
     * @return Une liste de messages triée par date.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    public static List<Message> recupererMessagesClientOrdonne(String nomUtilisateur)  throws ClassNotFoundException {
        // récupère les messages du client mais aussi les messages de ses abonnements
        List<Message> recupererMessagesClientOrdonne = new ArrayList<>();
        recupererMessagesClientOrdonne.addAll(recupererMessagesUtilisateur(nomUtilisateur));
        recupererMessagesClientOrdonne.addAll(recupererMessagesAbonnements(nomUtilisateur));
        Collections.sort(recupererMessagesClientOrdonne, new MessageDateComparator());
        return recupererMessagesClientOrdonne;
    }

    /**
     * Récupère une liste de messages associés à un utilisateur spécifié.
     *
     * @param nomUtilisateur Le nom de l'utilisateur pour lequel récupérer les messages.
     * @return Une liste de messages associés à l'utilisateur.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    private static List<Message> recupererMessagesUtilisateur(String nomUtilisateur) throws ClassNotFoundException {
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("select idM, contenuM, dateM, idU, nomUtilisateur FROM MESSAGE natural join UTILISATEUR where nomUtilisateur = ?");
            ps.setString(1, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("idM"), rs.getString("contenuM"), rs.getString("dateM"), rs.getInt("idU"), rs.getString("nomUtilisateur"));
                messages.add(message);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * Récupère une liste de messages des abonnements d'un utilisateur spécifié.
     *
     * @param nomUtilisateur Le nom de l'utilisateur pour lequel récupérer les abonnements.
     * @return Une liste de messages des abonnements de l'utilisateur.
     * @throws ClassNotFoundException Si la classe du pilote JDBC n'est pas trouvée.
     */
    private static List<Message> recupererMessagesAbonnements(String nomUtilisateur) throws ClassNotFoundException{
        List<Message> messages = new ArrayList<>();
        try{
            PreparedStatement ps = MainClient.getInstance().getSqlConnect().prepareStatement("SELECT M.idM, M.idU, M.contenuM, M.dateM, C.nomUtilisateur FROM MESSAGE M join ABONNE A on M.idU = A.abonneA join UTILISATEUR C on M.idU = C.idU where A.abonnementA = (select idU FROM UTILISATEUR where nomUtilisateur = ?) order by M.dateM desc");
            ps.setString(1, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("idM"), rs.getString("contenuM"), rs.getString("dateM"), rs.getInt("idU"), rs.getString("nomUtilisateur"));
                messages.add(message);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }
}
