package com.example.Quidpro.Quidpro.Servicios;

import com.example.Quidpro.Quidpro.Entidades.*;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Excepciones.ResourceNotFoundException;
import com.example.Quidpro.Quidpro.Repositorios.ImagenesPublicacionRepositorio;
import com.example.Quidpro.Quidpro.Repositorios.PublicacionRepositorio;
import com.example.Quidpro.Quidpro.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionServicio {

    @Autowired
    private final PublicacionRepositorio publicacionRepositorio;
    private final ImagenesPublicacionRepositorio imagenesPublicacionRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    public PublicacionServicio (PublicacionRepositorio publicacionRepositorio, ImagenesPublicacionRepositorio imagenesPublicacionRepositorio, UsuarioRepositorio usuarioRepositorio){
        this.publicacionRepositorio = publicacionRepositorio;
        this.imagenesPublicacionRepositorio = imagenesPublicacionRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
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
    private void asignarRelaciones(Publicacion publicacion, Integer idUsuario, List<Integer> idsImagenPublicacion) {
        Usuario usuario = validarExistencia(usuarioRepositorio.findById(idUsuario), "Usuario no encontrado");
        List<ImagenesPublicacion> imagenesPublicaciones = new ArrayList<>();
        if(idsImagenPublicacion != null){
            for(Integer idImagenPublicacion:idsImagenPublicacion){
                ImagenesPublicacion imagenPublicacion = validarExistencia(imagenesPublicacionRepositorio.findById(idImagenPublicacion), "imagen no encontrada");
                imagenesPublicaciones.add(imagenPublicacion);
            }
        }
        publicacion.setUsuario(usuario);
        publicacion.setImagenesPublicaciones(imagenesPublicaciones);
    }
    //Definicion de Métodos crud para la entidad Publicacion (CRUD)
    //Metodo para Crear Registros
    public Publicacion crearPublicacion(Publicacion publicacion, Integer idUsuario, List<Integer> idsImagenPublicacion){
        if(!esCampoValido(publicacion.getDescripcion())){
            throw new InvalidDataException("La descripción de la publicación es obligatorio.");
        }
        if(!esCampoValido(publicacion.getTitulo())){
            throw new InvalidDataException("El título de la publicación es obligatorio.");
        }
        asignarRelaciones(publicacion, idUsuario,idsImagenPublicacion);
        return publicacionRepositorio.save(publicacion);
    }
    //Metodo para Consultar todos los Registros
    public List<Publicacion> consultarPublicaciones(){
        return publicacionRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Publicacion consultarPublicacionById(Integer id){
        return validarExistencia(publicacionRepositorio.findById(id), "Publicación no encontrada");
    }
    //Metodo para Actualizar un Registro por Id
    public Publicacion actualizarPublicacion(Integer id, Publicacion publicacion, Integer idUsuario,List<Integer> idsImagenPublicacion){
        Publicacion publicacionExiste = consultarPublicacionById(id);
        if(!esCampoValido(publicacion.getDescripcion())){
            throw new InvalidDataException("La descripción de la publicación es obligatorio.");
        }
        if(!esCampoValido(publicacion.getTitulo())){
            throw new InvalidDataException("El título de la publicación es obligatorio.");
        }
        publicacionExiste.setDescripcion(publicacion.getDescripcion());
        publicacionExiste.setTitulo(publicacion.getTitulo());
        publicacionExiste.setTag(publicacion.getTag());
        asignarRelaciones(publicacionExiste, idUsuario,idsImagenPublicacion);
        return publicacionRepositorio.save(publicacionExiste);
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarPublicacion(Integer id){
        Publicacion publicacionEliminar = consultarPublicacionById(id);
        try {
            publicacionRepositorio.delete(publicacionEliminar);
            return "Publicacion eliminada con éxito";
        } catch (Exception e) {
            return "Ocurrio un error eliminando la publicacion" + e;
        }
    }
}
