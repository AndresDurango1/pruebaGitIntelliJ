package com.example.Quidpro.Quidpro.Entidades;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int id;
    @Column(nullable = false, length = 20)
    private String rol;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Muchos con la clase Usuario
    @OneToMany(targetEntity = Usuario.class, fetch = FetchType.LAZY, mappedBy = "rol")
    private List<Usuario> usuarios;
    /*METODOS*/
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
