package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "emprendimientos")
public class Emprendimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprendimiento")
    private int id;
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 256)
    private String descripcion;
    @Column(nullable = false)
    private LocalDate fecha_creacion;
    /*RELACIONES DE MULTIPLICICAD CON LAS OTRAS CLASES*/
    //Relacion Muchos a Uno con la clase Ciudad
    @ManyToOne(targetEntity = Ciudad.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad")
    @JsonIgnore
    private Ciudad ciudad;
    //Relacion Muchos a Uno con la clase Estados
    @ManyToOne(targetEntity = Estados.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado")
    @JsonIgnore
    private Estados estado;
    //Relacion Muchos a Muchos con la clase Sector
    @ManyToMany
    @JoinTable(
            name = "emprendimientos_sectores",
            joinColumns = @JoinColumn(name = "id_emprendimiento"),
            inverseJoinColumns = @JoinColumn(name = "id_sector")
    )
    private Set<Sector> sectores = new HashSet<>();
    //Relacion Muchos a Muchos con la clase Usuario
    @ManyToMany
    @JoinTable(
            name = "usuario_emprendimientos",
            joinColumns = @JoinColumn(name = "id_emprendimiento"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private Set<Usuario> usuarios = new HashSet<>();
    /*METODOS*/
    //Metodo constructor vacio
    public Emprendimiento() {
    }
    //Metodo constructor con todos los atributos
    public Emprendimiento(int id, String nombre, String descripcion, LocalDate fecha_creacion, Ciudad ciudad, Estados estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.ciudad = ciudad;
        this.estado = estado;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDate getFecha_creacion() {
        return fecha_creacion;
    }
    public void setFecha_creacion(LocalDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
    public Estados getEstado() {
        return estado;
    }
    public void setEstado(Estados estado) {
        this.estado = estado;
    }
    public Ciudad getCiudad() {
        return ciudad;
    }
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
    public Set<Sector> getSectores() {
        return sectores;
    }
    public void setSectores(Set<Sector> sectores) {
        this.sectores = sectores;
    }
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
