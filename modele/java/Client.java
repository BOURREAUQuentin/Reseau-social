package modele.java;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private int id;
    private String nomUtilisateur;
    private List<Client> listeAbonnes;

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

    public void ajouterAbonne(Client abonne) {
        listeAbonnes.add(abonne);
    }

    public void supprimerAbonne(Client abonne) {
        listeAbonnes.remove(abonne);
    }

    @Override
    public String toString() {
        return nomUtilisateur;
    }
}
