package com.example.Quidpro.Quidpro.Entidades;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sectores")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector")
    private int id;
    @Column(nullable = false, length = 50)
    private String sector;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    @ManyToMany(mappedBy = "sectores")
    private Set<Emprendimiento> emprendimientos = new HashSet<>();
    /*METODOS*/
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
