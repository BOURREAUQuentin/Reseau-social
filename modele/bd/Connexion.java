package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    public static Connection laConnexion = Connexion.connect("localhost", "DBbourreau", "quentin", "quentin");

    public static Connection connect(String nomServeur, String nomBase, String nomLogin, String motDePasse){
		try{
            return DriverManager.getConnection("jdbc:mysql://" + nomServeur + ":3306/" + nomBase, nomLogin, motDePasse);
        }
        catch(SQLException e){
            System.out.println("Erreur : " + e.getMessage());
        }
        return null;
	}
}
