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

import java.Utilisateur;
import src.Client;

import java.Utilisateur;
import java.Message;

public class ClientSQL {
    /** connexion à la base de donnée */
    private Connection connexion = Connexion.laConnexion;

    public ClientSQL(){
    }

    public int prochainIdUtilisateur(){
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT max(idU) maxId FROM UTILISATEUR");
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

    public List<Utilisateur> getLesUtilisateur(){
        List<Utilisateur> lesUtilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps= connexion.prepareStatement("SELECT * FROM UTILISATEUR");
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

    public Utilisateur getUtilisateur(String nomUtilisateur, String motDePasse){
        try{
            PreparedStatement ps= connexion.prepareStatement("SELECT * FROM UTILISATEUR where nomUtilisateur = ? AND mdpU = ?");
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

    public Utilisateur getUtilisateurParNomUtilisateur(String nomUtilisateur){
        try{
            PreparedStatement ps= connexion.prepareStatement("SELECT * FROM UTILISATEUR where nomUtilisateur = ?");
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

    public boolean ajouterClient(String nomUtilisateur, String motDePasse){
        try{
            if (utilisateurExiste(nomUtilisateur)){
                return false;
            }
            PreparedStatement ps = connexion.prepareStatement("INSERT INTO UTILISATEUR (idU, nomUtilisateur, mdpU) values (?,?,?)");
            ps.setInt(1, this.prochainIdUtilisateur());
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

    public boolean utilisateurExiste(String nomUtilisateur){
        try{
            PreparedStatement ps = connexion.prepareStatement("SELECT nomUtilisateur FROM UTILISATEUR where nomUtilisateur = ?");
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

    public boolean supprimerUtilisateur(String nomUtilisateur){
        try{
            PreparedStatement ps = connexion.prepareStatement("DELETE FROM UTILISATEUR WHERE nomUtilisateur = ?");
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
