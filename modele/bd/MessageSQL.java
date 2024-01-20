package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.Message;

public class MessageSQL {
    /** connexion à la base de donnée */
    private Connection connexion = Connexion.laConnexion;

    public MessageSQL(){
    }

    public List<Message> getMessages(){
        try{
            List<Message> res = new ArrayList<>();
            PreparedStatement ps = connexion.prepareStatement("SELECT * FROM MESSAGE");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String img;
                if (rs.getObject(6) != null){
                    img = "/img/" + rs.getInt(1) + rs.getString("pseudout") + ".png";
                }
                else{ img = "/img/null.png"; }
                res.add(new User(rs.getInt("idut"), rs.getString("pseudout"), rs.getString("emailut"), rs.getString("mdput"),rs.getInt("idrole"),img,rs.getString("biographie")));
            }
            return res;
        } 
        catch(SQLException e){
            return null;
        }
    }

}
