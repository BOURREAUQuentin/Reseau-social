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

    private int prochainIdMessage(){
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
            PreparedStatement ps = connexion.prepareStatement("SELECT idC FROM CLIENT where nomUtilisateurC = ?");
            ps.setString(1, pseudoExpediteur);
            ResultSet rs = ps.executeQuery();
            int idExpediteur = 0;
            if (rs.next()){
                idExpediteur = rs.getInt("idC");
            }

            // requete pour l'ajout de message
            PreparedStatement ps2 = connexion.prepareStatement("INSERT INTO MESSAGE (idM, contenuM, dateM, idC) VALUES (?, ?, ?, ?)");
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
            PreparedStatement ps = connexion.prepareStatement("SELECT idM, contenuM, dateM, idC, nomUtilisateurC FROM MESSAGE Natural join CLIENT where idM = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message m = new Message(rs.getInt("idM"), rs.getString("contenuM"), rs.getString("dateM"), rs.getInt("idC"), rs.getString("nomUtilisateurC"));
                return m;
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
            PreparedStatement ps = connexion.prepareStatement("SELECT idM, contenuM, dateM, idC, nomUtilisateurC FROM MESSAGE natural join CLIENT where contenuM = ? and dateM = ? and nomUtilisateur = ?");
            ps.setString(1, contenu);
            ps.setString(2, date);
            ps.setString(3, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message m = new Message(rs.getInt("idM"), rs.getString("contenuM"), rs.getString("dateM"), rs.getInt("idC"), rs.getString("nomUtilisateurC"));
                return m;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static List<Message> recupererLesMessageDeTousSesAmisDansOrdreDate(String pseudo) throws ClassNotFoundException {
        List<Message> messages = new ArrayList<>();

        try {
            // Requête SQL avec LEFT JOIN pour récupérer les messages des amis
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("select id_M,id_U,contenu,date,pseudo from MESSAGES natural join UTILISATEUR where pseudo=?");

            PreparedStatement ps2 = Main.getInstance().getSqlConnect().prepareStatement("SELECT M.id_M,M.id_U,M.contenu,M.date,U.pseudo FROM MESSAGES M JOIN AMIS A ON M.id_U = A.suivi JOIN UTILISATEUR U ON M.id_U = U.id_U WHERE A.suiveur = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?) ORDER BY M.date DESC");
            ps.setString(1, pseudo);
            ps2.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
                messages.add(message);
                
            }
            while (rs2.next()) {
                Message message = new Message(rs2.getInt(1), rs2.getInt(2), rs2.getString(3), rs2.getString(4), rs2.getString(5));
                messages.add(message);
                
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Tri de la liste par date dans l'ordre decroissant
        Collections.sort(messages, (msg1, msg2) -> msg1.getDate().compareTo(msg2.getDate()));
        
        //Collections.sort(messages, (msg1, msg2) -> msg1.getDate().compareTo(msg2.getDate()));

        return messages;
    }
}
