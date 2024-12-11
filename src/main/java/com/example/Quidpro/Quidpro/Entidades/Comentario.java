package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Integer id;
    @Column(nullable = false, length = 256)
    private String texto;
    @Column(nullable = false)
    private LocalDate fecha_creacion;
    @Column(nullable = false)
    private LocalDate fecha_actualizacion;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Muchos con la clase ImagenesComentario
    @OneToMany(targetEntity = ImagenesComentario.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "comentario")
    @JsonIgnore
    private List<ImagenesComentario> imagenesComentarios;

    //Relacion Muchos a Uno con la clase Usuario
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
    //Relacion Muchos a Uno con la clase Publicacion
    @ManyToOne(targetEntity = Publicacion.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    @JsonIgnore
    private Publicacion publicacion;

    /*METODOS*/
    //Metodo constructor vacio
    public Comentario() {
    }
    //Metodo constructor con todos los atributos
    public Comentario(Integer id, String texto, LocalDate fechaCreacion, LocalDate fechaActualizacion, Usuario usuario, Publicacion publicacion) {
        this.id = id;
        this.texto = texto;
        this.fecha_creacion = fechaCreacion;
        this.fecha_actualizacion = fechaActualizacion;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }
    //Metodos GETTER  y SETTER
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }
    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ImagenesComentario> getImagenesComentarios() {
        return imagenesComentarios;
    }
    public void setImagenesComentarios(List<ImagenesComentario> imagenesComentarios) { this.imagenesComentarios = imagenesComentarios; }

    public LocalDate getFecha_creacion() {
        return fecha_creacion;
    }
    public void setFecha_creacion(LocalDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public LocalDate getFecha_actualizacion() {
        return fecha_actualizacion;
    }
    public void setFecha_actualizacion(LocalDate fecha_actualizacion) { this.fecha_actualizacion = fecha_actualizacion; }
}
