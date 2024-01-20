package java;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private int idU;
    private String nomUtilisateur;
    private String mdpU;

    public Utilisateur(int id, String nomUtilisateur, String mdp) {
        this.idU = id;
        this.nomUtilisateur = nomUtilisateur;
        this.mdpU = mdp;
    }

    public int getIdUtilisateur(){
        return this.idU;
    }

    public String getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    public String getMdp(){
        return this.mdpU;
    }
    
    @Override
    public String toString() {
        return nomUtilisateur;
    }
}
