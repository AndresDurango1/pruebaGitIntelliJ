package com.example.Quidpro.Quidpro.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int id;
    @Column(nullable = false, length = 20)
    private String rol;

    //Metodo contructor vacio
    public Rol() {
    }
    //Metodo contructor con todos los atributos
    public Rol(int id, String rol) {
        this.id = id;
        this.rol = rol;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
}
