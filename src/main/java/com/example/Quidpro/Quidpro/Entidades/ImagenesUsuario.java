package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "imagenesUsuarios")
public class ImagenesUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagenUsuario")
    private Integer id;
    @Column(nullable = false, length = 50)
    private String titulo;
    @Column(nullable = false, length = 256)
    private String url_imagenUsuario;
    /*RELACIONES DE MULTIPLICIDAD CON OTRAS CLASES*/
    //Relacion Uno a Uno con la clase Usuario
    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.LAZY, mappedBy = "imagenUsuario")
    @JsonIgnore
    private Usuario usuario;
    /*METODOS*/
    //Metodo constructor vacio
    public ImagenesUsuario() {
    }
    //Metodo constructor con todos los parametros
    public ImagenesUsuario(Integer id, String titulo, String url_imagenUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.url_imagenUsuario = url_imagenUsuario;
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
    public String getUrl_imagenUsuario() {
        return url_imagenUsuario;
    }
    public void setUrl_imagenUsuario(String url_imagenUsuario) {
        this.url_imagenUsuario = url_imagenUsuario;
    }
}
