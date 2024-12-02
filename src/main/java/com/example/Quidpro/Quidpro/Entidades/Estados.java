package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estados")
public class Estados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private int id;
    @Column(nullable = false, length = 50)
    private String estado;

    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Muchos con la clase Emprendimiento
    @OneToMany(targetEntity = Emprendimiento.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "estado")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Emprendimiento> emprendimientos = new ArrayList<>();
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

    public List<Emprendimiento> getEmprendimientos() {
        return emprendimientos;
    }

    public void setEmprendimientos(List<Emprendimiento> emprendimientos) {
        this.emprendimientos = emprendimientos;
    }
}
