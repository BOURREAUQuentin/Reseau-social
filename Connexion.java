import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private Connection laConnexion = null;
    private boolean connecte = false;

    public Connexion() throws ClassNotFoundException{
		this.laConnexion = null;
		this.connecte = false;
		Class.forName("org.mariadb.jdbc.Driver");
	}

	public Connection getConnection() throws ClassNotFoundException{
		return laConnexion;
	}

    public void connecter(String nomServeur, String nomBase, String nomLogin, String motDePasse) throws SQLException {
		this.laConnexion = null;
		this.connecte = false;
		this.laConnexion = DriverManager.getConnection("jdbc:mysql://"+nomServeur+":3306/"+nomBase,nomLogin,motDePasse);
		this.connecte = this.laConnexion != null;
	}

    public void close() throws SQLException {
		this.laConnexion.close();
		this.connecte=false;

	}

    public boolean isConnecte() {
        return this.connecte;
    }
}
