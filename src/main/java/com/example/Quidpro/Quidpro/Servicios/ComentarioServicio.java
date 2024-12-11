package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.*;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Excepciones.ResourceNotFoundException;
import com.example.Quidpro.Quidpro.Repositorios.ComentarioRepositorio;
import com.example.Quidpro.Quidpro.Repositorios.ImagenesComentarioRepositorio;
import com.example.Quidpro.Quidpro.Repositorios.PublicacionRepositorio;
import com.example.Quidpro.Quidpro.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComentarioServicio {
    private final ComentarioRepositorio comentarioRepositorio;
    private final PublicacionRepositorio publicacionRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final ImagenesComentarioRepositorio imagenesComentarioRepositorio;
    private final ImagenesComentarioServicio imagenesComentarioServicio;
    @Autowired
    public ComentarioServicio(ComentarioRepositorio comentarioRepositorio, PublicacionRepositorio publicacionRepositorio, UsuarioRepositorio usuarioRepositorio, ImagenesComentarioRepositorio imagenesComentarioRepositorio, ImagenesComentarioServicio imagenesComentarioServicio) {
        this.comentarioRepositorio = comentarioRepositorio;
        this.publicacionRepositorio = publicacionRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.imagenesComentarioRepositorio = imagenesComentarioRepositorio;
        this.imagenesComentarioServicio = imagenesComentarioServicio;
    }
    // Metodo auxiliar para validar campos
    private boolean esCampoValido(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
    // Metodo helper para validar existencia de las entidades
    private <T> T validarExistencia(Optional<T> optional, String mensaje) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(mensaje));
    }
    // Metodo helper para asignar relaciones
    private void asignarRelaciones(Comentario comentario, Integer idPublicacion, Integer idUsuario) {
        Publicacion publicacion = validarExistencia(publicacionRepositorio.findById(idPublicacion), "Publicación no encontrado");
        Usuario usuario = validarExistencia(usuarioRepositorio.findById(idUsuario), "Usuario no encontrado");
        List<ImagenesComentario> imagenesComentarios = new ArrayList<>();
        comentario.setPublicacion(publicacion);
        comentario.setUsuario(usuario);
        comentario.setImagenesComentarios(imagenesComentarios);
    }
    //Definicion de Métodos crud para la entidad Comentario (CRUD)
    //Metodo para Crear Registros
    public Comentario crearComentario(Comentario comentario, Integer idPublicacion, Integer idUsuario) {
        if (!esCampoValido(comentario.getTexto())) {
            throw new InvalidDataException("El texto del comentario es obligatorio.");
        }
        asignarRelaciones(comentario, idPublicacion, idUsuario);
        return comentarioRepositorio.save(comentario);
    }
    //Metodo para Consultar todos los Registros
    public List<Comentario> consultarComentarios() {
        return comentarioRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Comentario consultarComentarioById(Integer id) {
        return validarExistencia(comentarioRepositorio.findById(id), "Comentario no encontrado con el ID: " + id);
    }
    //Metodo para consultar registros por usuario
    public List<Comentario> consultarComentariosPorUsuario(Integer idUsuario){
        return comentarioRepositorio.findByUsuarioId(idUsuario);
    }
    //Metodo para actualizar un comentario
    public Comentario actualizarComentarioById(Integer id, Comentario comentario, Integer idPublicacion, Integer idUsuario) {
        Comentario comentarioExistente = consultarComentarioById(id);
        if (!esCampoValido(comentario.getTexto())) {
            throw new InvalidDataException("El texto del comentario es obligatorio.");
        }
        comentarioExistente.setTexto(comentario.getTexto());
        asignarRelaciones(comentarioExistente, idPublicacion, idUsuario);
        return comentarioRepositorio.save(comentarioExistente);
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarComentario(Integer id) {
        Comentario comentarioEliminar = consultarComentarioById(id);
        try {
            imagenesComentarioServicio.eliminarImagen(comentarioEliminar.getImagenesComentarios());
            comentarioRepositorio.delete(comentarioEliminar);
            return "Comentario eliminado con éxito";
        } catch (Exception e) {
            return "Ocurrio un error eliminando el comentario" + e;
        }
    }
}
