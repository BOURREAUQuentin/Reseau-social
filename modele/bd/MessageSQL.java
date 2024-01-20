package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.Message;

public class MessageSQL {
    /** connexion à la base de donnée */
    private Connection connexion = Connexion.laConnexion;

    public MessageSQL(){
    }

    public int prochainIdMessage(){
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT max(idM) maxId FROM MESSAGE");
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

    public void ajouterMessage(String pseudoExpediteur, String contenu){
        try{
            // requete pour récupérer l'id de l'expéditeur
            PreparedStatement ps = connexion.prepareStatement("SELECT idU FROM UTILISATEUR where nomUtilisateur = ?");
            ps.setString(1, pseudoExpediteur);
            ResultSet rs = ps.executeQuery();
            int idExpediteur = 0;
            if (rs.next()){
                idExpediteur = rs.getInt("idU");
            }

            // requete pour l'ajout de message
            PreparedStatement ps2 = connexion.prepareStatement("INSERT INTO MESSAGE (idM, contenuM, dateM, idU) VALUES (?, ?, ?, ?)");
            ps2.setInt(1, this.prochainIdMessage());
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

    public Message recupererMessageParId(int idMessage){
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT idM, contenuM, dateM, idU, nomUtilisateur FROM MESSAGE Natural join UTILISATEUR where idM = ?");
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

    public void supprimerMessage(int idMessage){
        try{
            // suppression des lignes dans la table LIKE où l'id message existe (contraintes clés étrangères)
            PreparedStatement ps = connexion.prepareStatement("DELETE FROM LIKE WHERE idM = ?");
            ps.setInt(1, idMessage);
            ps.executeUpdate();
            // suppression du message
            PreparedStatement ps2 = connexion.prepareStatement("DELETE FROM MESSAGE WHERE idM = ?");
            ps2.setInt(1, idMessage);
            ps2.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }

    public Message recupererMessage(String date, String nomUtilisateur, String contenu){
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT idM, contenuM, dateM, idU, nomUtilisateur FROM MESSAGE natural join UTILISATEUR where contenuM = ? and dateM = ? and nomUtilisateur = ?");
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

    public List<Message> recupererMessagesClientOrdonne(String nomUtilisateur) {
        // récupère les messages du client mais aussi les messages de ses abonnements
        List<Message> recupererMessagesClientOrdonne = new ArrayList<>();
        recupererMessagesClientOrdonne.addAll(recupererMessagesUtilisateur(nomUtilisateur));
        recupererMessagesClientOrdonne.addAll(recupererMessagesAbonnements(nomUtilisateur));
        Collections.sort(recupererMessagesClientOrdonne, new MessageDateComparator());
        return recupererMessagesClientOrdonne;
    }

    private List<Message> recupererMessagesUtilisateur(String nomUtilisateur) {
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = connexion.prepareStatement("select idM, contenuM, dateM, idU, nomUtilisateur FROM MESSAGE natural join UTILISATEUR where nomUtilisateur = ?");
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

    private List<Message> recupererMessagesAbonnements(String nomUtilisateur){
        List<Message> messages = new ArrayList<>();
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT M.idM, M.idU, M.contenuM, M.dateM, C.nomUtilisateur FROM MESSAGE M join ABONNE A on M.idU = A.abonneA join UTILISATEUR C on M.idU = C.idU where A.abonnementA = (select idU FROM UTILISATEUR where nomUtilisateur = ?) order by M.dateM desc");
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
