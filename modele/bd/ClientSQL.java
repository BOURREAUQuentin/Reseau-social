package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.spi.ResolveResult;

import java.Client;
import java.Message;

public class ClientSQL {
    /** connexion à la base de donnée */
    private Connection connexion = Connexion.laConnexion;

    public ClientSQL(){
    }

    public int prochainIdClient(){
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT max(idC) maxId FROM CLIENT");
            ResultSet rs = ps.executeQuery();
            int maxIdClientActuel = 0;
            if (rs.next()) {
                maxIdClientActuel = rs.getInt("maxId");
            }
            return maxIdClientActuel + 1;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<Client> getLesClients(){
        List<Client> lesClients = new ArrayList<>();
        try{
            PreparedStatement ps= connexion.prepareStatement("SELECT * FROM CLIENT");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Client c = new Client(rs.getInt(1), rs.getString(2), rs.getString(3));
                lesClients.add(c);
            }
            return lesClients;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return lesClients;
        }
    }

    public Client getClient(String nomUtilisateur, String motDePasse){
        try{
            PreparedStatement ps= connexion.prepareStatement("SELECT * FROM CLIENT where nomUtilisateurC = ? AND mdpC = ?");
            ps.setString(1, nomUtilisateur);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Client c = new Client(rs.getInt(1), rs.getString(2), rs.getString(3));
                return c;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Client getClientParNomUtilisateur(String nomUtilisateur){
        try{
            PreparedStatement ps= connexion.prepareStatement("SELECT * FROM CLIENT where nomUtilisateurC = ?");
            ps.setString(1, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Client c = new Client(rs.getInt(1), rs.getString(2), rs.getString(3));
                return c;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean ajouterClient(String nomUtilisateur, String motDePasse){
        try{
            if (clientExiste(nomUtilisateur)){
                return false;
            }
            PreparedStatement ps = connexion.prepareStatement("INSERT INTO CLIENT (idC, nomUtilisateurC, mdpC) values (?,?,?)");
            ps.setInt(1, this.prochainIdClient());
            ps.setString(2, nomUtilisateur);
            ps.setString(3, motDePasse);
            ResultSet rs = ps.executeQuery();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clientExiste(String nomUtilisateur){
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT nomUtilisateurC FROM CLIENT where nomUtilisateurC = ?");
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

    public boolean supprimerClient(String nomUtilisateur){
        try{
            PreparedStatement ps = connexion.prepareStatement("DELETE FROM CLIENT WHERE nomUtilisateur = ?");
            ps.setString(1, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
