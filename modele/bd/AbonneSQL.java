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

public class AbonneSQL{

    /** connexion à la base de donnée */
    private Connection connexion = Connexion.laConnexion;
    
    public AbonneSQL(){
    }

    public List<Client> getUtilisateursAbonnements(String nomUtilisateur){
        List<Client> listeUtilisateurs = new ArrayList<>();
        try{
            int id = this.getIdClient(nomUtilisateur);
            PreparedStatement ps = connexion.prepareStatement("SELECT idC, nomUtilisateurC, mdpC FROM ABONNE natural join CLIENT where abonnementA = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3));
                listeUtilisateurs.add(client);
            }
            return listeUtilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return listeUtilisateurs;
        }
    }

    private int getIdClient(String nomUtilisateur){
        try{
            PreparedStatement ps = connexion.prepareStatement("select idC from CLIENT where nomUtilisateurC = ?");
            ps.setString(1, nomUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("idC");
            }
            return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
 
    public List<Client> getUtilisateurAbonnes(String nomUtilisateur){
        List<Client> listeUtilisateurs = new ArrayList<>();
        try{
            int id = this.getIdClient(nomUtilisateur);
            PreparedStatement ps = connexion.prepareStatement("SELECT idC, nomUtilisateurC, mdpC FROM ABONNE natural join CLIENT where abonneA = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3));
                listeUtilisateurs.add(client);
            }
            return listeUtilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return listeUtilisateurs;
        }
    }

    public List<Client> getUtilisateurNonAbonnes(String nomUtilisateur){
        List<Client> listeUtilisateurs = new ArrayList<>();
        try{
            int id = this.getIdClient(nomUtilisateur);
            PreparedStatement ps = connexion.prepareStatement("SELECT * FROM CLIENT.C WHERE C.idC != (SELECT idC FROM CLIENT WHERE nomUtilisateurC = ?) AND C.idC NOT IN (SELECT ABONNE.abonnementA FROM ABONNE WHERE ABONNE.abonneA = ?)");
            ps.setString(1, nomUtilisateur);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Client client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3));
                listeUtilisateurs.add(client);
            }
            return listeUtilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return listeUtilisateurs;
        }
    }

    public void ajouterAbonnement(String nomUtilisateur, String nomUtilisateurCible){
        try{
            PreparedStatement ps = connexion.prepareStatement("INSERT INTO ABONNE (abonnementA, abonneA) values (?,?)");
            ps.setString(1, nomUtilisateur);
            ps.setString(2, nomUtilisateurCible);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void supprimerAbonnement(String nomUtilisateur, String nomUtilisateurCible){
        try{
            PreparedStatement ps = connexion.prepareStatement("DELETE FROM ABONNE where abonnementA = ? AND abonneA = ?");
            ps.setString(1, nomUtilisateur);
            ps.setString(2, nomUtilisateurCible);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


}