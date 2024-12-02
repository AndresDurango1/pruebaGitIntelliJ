package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publicaciones")
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicacion")
    private Integer id;
    @Column(nullable = false, length = 50)
    private String titulo;
    @Column(nullable = false, length = 256)
    private String descripcion;
    @Column(nullable = false)
    private LocalDate fecha_creacion;
    @Column(nullable = false)
    private LocalDate fecha_actualizacion;
    @Column(nullable = true, length = 50)
    private String tag;

    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Muchos con la clase Comentario
    @OneToMany(targetEntity = Comentario.class, fetch = FetchType.LAZY, mappedBy = "publicacion")
    private List<Comentario> comentarios = new ArrayList<>();

    //Relacion Uno a Muchos con la clase ImagenPublicacion
    @OneToMany(targetEntity = ImagenesPublicacion.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "publicacion")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ImagenesPublicacion> imagenesPublicaciones = new ArrayList<>();

    //Relacion Muchos a Uno con la clase Usuario
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    /*Metodos*/
    //Metodo constructor vacio
    public Publicacion() {
    }
    //Metodo constructor con todos los atributos
    public Publicacion(Integer id, String titulo, String descripcion, LocalDate fecha_creacion, LocalDate fecha_actualizacion, String tag, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.fecha_actualizacion = fecha_actualizacion;
        this.tag = tag;
        this.usuario = usuario;
    }
    //Metodos GETTER y SETTER
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
    public LocalDate getFecha_actualizacion() {
        return fecha_actualizacion;
    }
    public void setFecha_actualizacion(LocalDate fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public List<Comentario> getComentarios() {
        return comentarios;
    }
    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
    public List<ImagenesPublicacion> getImagenesPublicaciones() {
        return imagenesPublicaciones;
    }
    public void setImagenesPublicaciones(List<ImagenesPublicacion> imagenesPublicaciones) {
        this.imagenesPublicaciones = imagenesPublicaciones;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}