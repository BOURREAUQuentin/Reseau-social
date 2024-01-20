package java;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private int id;
    private String nomUtilisateur;

    public Client(int id, String nomUtilisateur) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.listeAbonnes = new ArrayList<>();
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public List<Client> getListeAbonnes() {
        return listeAbonnes;
    }

    @Override
    public String toString() {
        return nomUtilisateur;
    }
}
