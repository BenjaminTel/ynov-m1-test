package org.example.model;

public class Utilisateur {
    String nom;
    int age;
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    public Utilisateur(String dorian, int i) {
        this.nom = dorian;
        this.age = i;
    }
}
