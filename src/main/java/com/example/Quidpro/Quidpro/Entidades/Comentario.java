package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private int id;
    @Column(nullable = false, length = 256)
    private String texto;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Muchos con la clase ImagenesComentario
    @OneToMany(targetEntity = ImagenesComentario.class,  fetch = FetchType.LAZY)
    @JoinColumn(name = "id_imagenComentario")
    private List<ImagenesComentario> imagenesComentarios;

    //Relacion Muchos a Uno con la clase Usuario
    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    //Relacion Muchos a Uno con la clase Publicacion: Una publicacion puede tener muchos comentarios, pero un comentario solo pertenece a una publicacion
    @ManyToOne(targetEntity = Publicacion.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    @JsonIgnore
    private Publicacion publicacion;

    /*METODOS*/
    //Metodo constructor vacio
    public Comentario() {
    }
    //Metodo constructor con todos los atributos
    public Comentario(int id, String texto, List<ImagenesComentario> imagenesComentarios, Usuario usuario, Publicacion publicacion) {
        this.id = id;
        this.texto = texto;
        this.imagenesComentarios = imagenesComentarios;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }
    //Metodos GETTER  y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public List<ImagenesComentario> getImagenes() {
        return imagenesComentarios;
    }
    public void setImagenes(List<ImagenesComentario> imagenes) {
        this.imagenesComentarios = imagenes;
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
    public void setImagenesComentarios(List<ImagenesComentario> imagenesComentarios) {
        this.imagenesComentarios = imagenesComentarios;
    }
}
