package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ciudades")
public class Ciudad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private int id;
    @Column(nullable = false, length = 100)
    private String ciudad;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Muchos con clase Usuario
    @OneToMany(targetEntity = Usuario.class, fetch = FetchType.LAZY, mappedBy = "ciudad")
    private List<Usuario> usuarios;

    //Relacion Uno a Muchos con la clase Emprendimiento
    @OneToMany(targetEntity = Emprendimiento.class, fetch = FetchType.LAZY, mappedBy = "ciudad")
    private List<Emprendimiento> emprendimientos;

    //Relacion Muchos a uno con clase Departamento
    @ManyToOne(targetEntity = Departamento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento", nullable = false)
    @JsonIgnore
    private Departamento departamento;

    /*METODOS*/
    //Metodo constructor vacio
    public Ciudad() {
    }
    //Metodo constructor con todos los atributos
    public Ciudad(int id, String ciudad, Departamento departamento) {
        this.id = id;
        this.ciudad = ciudad;
        this.departamento = departamento;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public Departamento getDepartamento() {
        return departamento;
    }
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Emprendimiento> getEmprendimientos() {
        return emprendimientos;
    }

    public void setEmprendimientos(List<Emprendimiento> emprendimientos) {
        this.emprendimientos = emprendimientos;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
