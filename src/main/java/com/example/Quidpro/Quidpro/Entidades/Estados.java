package com.example.Quidpro.Quidpro.Entidades;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "estados")
public class Estados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 50)
    private String estado;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Muchos con la clase Emprendimiento
    @OneToMany(targetEntity = Emprendimiento.class, fetch = FetchType.LAZY, mappedBy = "estado")
    private List<Emprendimiento> emprendimientos;
    /*METODOS*/
    //Metodo constructor vacio
    public Estados(){
    }
    //Metodo constructor con todos los atributos
    public Estados(int id, String estado) {
        this.id = id;
        this.estado = estado;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
