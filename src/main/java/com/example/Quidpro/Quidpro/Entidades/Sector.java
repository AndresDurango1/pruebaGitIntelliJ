package com.example.Quidpro.Quidpro.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "sectores")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector")
    private int id;
    @Column(nullable = false, length = 50)
    private String sector;

    //Metodo contructor vacio
    public Sector() {
    }
    //Metodo contructor con todos los atributos
    public Sector(int id, String sector) {
        this.id = id;
        this.sector = sector;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
}
