package java;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private int idC;
    private String nomUtilisateurC;
    private String mdpC;

    public Client(int id, String nomUtilisateur, String mdp) {
        this.idC = id;
        this.nomUtilisateurC = nomUtilisateur;
        this.mdpC = mdp;
    }

    public int getIdUtilisateur(){
        return this.idC;
    }

    public String getNomUtilisateur() {
        return nomUtilisateurC;
    }

    public String getMdp(){
        return this.mdpC;
    }
    
    @Override
    public String toString() {
        return nomUtilisateurC;
    }
}
