package com.example.Quidpro.Quidpro.Entidades;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "departamentos")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
    private int id;
    @Column(nullable = false, length = 100)
    private String departamento;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a muchos con clase Ciudad
    @OneToMany(targetEntity = Ciudad.class, fetch = FetchType.LAZY, mappedBy = "departamento")
    private List<Ciudad> ciudades;

    /*METODOS*/
    //Metodo contructor vacio
    public Departamento() {
    }
    //Metodo contructor con todos los atributos
    public Departamento(int id, String departamento) {
        this.id = id;
        this.departamento = departamento;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
