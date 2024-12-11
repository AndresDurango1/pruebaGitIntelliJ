package com.example.Quidpro.Quidpro.Entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.*;


@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int id;
    @Column(nullable = false, length = 50)
    private String nombres;
    @Column(nullable = false, length = 50)
    private String apellidos;
    @Column(nullable = false, length = 100)
    private String direccion;
    @Column(nullable = false, length = 100)
    private String correo;
    @Column(nullable = false, length = 50)
    private String telefono;
    @Column(nullable = false, length =  256)
    private String contrasena;
    @Column(nullable = false, length =  50)
    private String usuario;
    @Column(nullable = false, length =  256)
    private String identificacion;

    //Relacion Uno a Uno con la clase ImagenesUsuario
    @OneToOne(targetEntity = ImagenesUsuario.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_imagenUsuario", nullable = true)
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ImagenesUsuario imagenUsuario;

    //Relacion Uno a Muchos con la clase Comentario
    @OneToMany(targetEntity = Comentario.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "usuario")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Comentario> comentarios = new ArrayList<>();

    //Relacion Uno a Muchos con la clase Publicacion
    @OneToMany(targetEntity = Publicacion.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "usuario")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Publicacion> publicaciones = new ArrayList<>();

    //Relacion Muchos a Uno con Entidad Rol
    @ManyToOne(targetEntity = Rol.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    @JsonIgnore
    private Rol rol;

    //Relacion Muchos a Uno con Clase Ciudad
    @ManyToOne(targetEntity = Ciudad.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad", nullable = false)
    @JsonIgnore
    private Ciudad ciudad;

    //Relacion Muchos a Muchos con la clase Emprendimiento
    @ManyToMany(mappedBy = "usuarios", cascade = CascadeType.ALL)
    private Set<Emprendimiento> emprendimientos = new HashSet<>();

    /*METODOS*/
    //Metodo contructor vacio
    public Usuario() {
    }
    //Metodo constructo con todos los atributos
    public Usuario(int id, String nombres, String apellidos, String direccion, String correo, String telefono, String contrasena, String usuario, String identificacion, ImagenesUsuario imagenUsuario, Ciudad ciudad, Rol rol) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.usuario = usuario;
        this.identificacion = identificacion;
        this.imagenUsuario = imagenUsuario;
        this.ciudad = ciudad;
        this.rol = rol;
    }
    //Metodos GETTER y SETTER
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }
    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }
    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public ImagenesUsuario getImagenUsuario() {
        return imagenUsuario;
    }
    public void setImagenUsuario(ImagenesUsuario imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Set<Emprendimiento> getEmprendimientos() {
        return emprendimientos;
    }
    public void setEmprendimientos(Set<Emprendimiento> emprendimientos) {
        this.emprendimientos = emprendimientos;
    }

    public String getContrasena() {return contrasena;}
    public void setContrasena(String contrasena) {this.contrasena = contrasena;}

    public String getUsuario() {return usuario;}
    public void setUsuario(String usuario) {this.usuario = usuario;}

    public String getIdentificacion() {return identificacion;}
    public void setIdentificacion(String identificacion) {this.identificacion = identificacion;}

}
