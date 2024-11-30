package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "imagenesPublicaciones")
public class ImagenesPublicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagenPublicacion", insertable = false, updatable = false)
    private int id;
    @Column(nullable = false, length = 50)
    private String titulo;
    @Column(nullable = false, length = 256)
    private String url_imagenPublicacion;

    @ManyToOne(targetEntity = Publicacion.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_imagenPublicacion", nullable = false)
    @JsonIgnore
    private Publicacion publicacion;

    /*METODOS*/
    //Metodo constructor vacio
    public ImagenesPublicacion() {
    }
    //Metodo constructor con todos los atributos
    public ImagenesPublicacion(int id, String titulo, String url_imagenPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.url_imagenPublicacion = url_imagenPublicacion;
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
    public String getUrl_imagenPublicacion() {
        return url_imagenPublicacion;
    }
    public void setUrl_imagenPublicacion(String url_imagenPublicacion) {
        this.url_imagenPublicacion = url_imagenPublicacion;
    }
}
