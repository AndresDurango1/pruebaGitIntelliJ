package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "imagenesComentarios")
public class ImagenesComentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagenComentario")
    private int id;
    @Column(nullable = false, length = 50)
    private String titulo;
    @Column(nullable = false, length = 256)
    private String url_imagenComentario;

    /*METODOS*/
    //Metodo constructor vacío
    public ImagenesComentario() {
    }
    //Metodo constructor con todos los parametros
    public ImagenesComentario(int id, String titulo, String url_imagenComentario) {
        this.id = id;
        this.titulo = titulo;
        this.url_imagenComentario = url_imagenComentario;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getUrl_imagenComentario() {
        return url_imagenComentario;
    }
    public void setUrl_imagenComentario(String url_imagenComentario) {
        this.url_imagenComentario = url_imagenComentario;
    }
}
