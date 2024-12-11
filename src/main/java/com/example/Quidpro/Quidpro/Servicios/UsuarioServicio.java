package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.*;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Excepciones.ResourceNotFoundException;
import com.example.Quidpro.Quidpro.Repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final CiudadRepositorio ciudadRepositorio;
    private final RolRepositorio rolRepositorio;
    private final ImagenesUsuarioRepositorio imagenesUsuarioRepositorio;
    private final ImagenesUsuarioServicio imagenesUsuarioServicio;
    private final PublicacionServicio publicacionServicio;
    private final ImagenesPublicacionServicio imagenesPublicacionServicio;
    private final ComentarioServicio comentarioServicio;
    private final ImagenesComentarioServicio imagenesComentarioServicio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, CiudadRepositorio ciudadRepositorio,
                           RolRepositorio rolRepositorio, ImagenesUsuarioRepositorio imagenesUsuarioRepositorio,
                           ImagenesUsuarioServicio imagenesUsuarioServicio, PublicacionServicio publicacionServicio, ImagenesPublicacionServicio imagenesPublicacionServicio, ComentarioServicio comentarioServicio, ImagenesComentarioServicio imagenesComentarioServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.ciudadRepositorio = ciudadRepositorio;
        this.rolRepositorio = rolRepositorio;
        this.imagenesUsuarioRepositorio = imagenesUsuarioRepositorio;
        this.imagenesUsuarioServicio = imagenesUsuarioServicio;
        this.publicacionServicio = publicacionServicio;
        this.imagenesPublicacionServicio = imagenesPublicacionServicio;
        this.comentarioServicio = comentarioServicio;
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
    private void asignarRelaciones(Usuario usuario, Integer idCiudad, Integer idRol, Integer idImagenUsuario) {
        if (idCiudad != null) {
            Ciudad ciudad = validarExistencia(ciudadRepositorio.findById(idCiudad), "Ciudad no encontrada");
            usuario.setCiudad(ciudad);
        }
        if (idRol != null) {
            Rol rol = validarExistencia(rolRepositorio.findById(idRol), "Rol no encontrado");
            usuario.setRol(rol);
        }
        if (idImagenUsuario != null) {
            ImagenesUsuario imagenUsuario = validarExistencia(imagenesUsuarioRepositorio.findById(idImagenUsuario), "Imagen no encontrada");
            usuario.setImagenUsuario(imagenUsuario);
        }
    }
    // Metodo para crear un usuario
    public Usuario crearUsuario(Usuario usuario, Integer idCiudad, Integer idRol, Integer idImagenUsuario) {
        if (!esCampoValido(usuario.getNombres())) {
            throw new InvalidDataException("El nombres del usuario es obligatorio.");
        }
        if (!esCampoValido(usuario.getApellidos())) {
            throw new InvalidDataException("El apellido del usuario es obligatorio");
        }
        if (!esCampoValido(usuario.getDireccion())) {
            throw new InvalidDataException("La dirección es un campo requerido");
        }
        if (!esCampoValido(usuario.getCorreo()) || !usuario.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidDataException("El correo del usuario es obligatorio y debe tener un formato válido.");
        }
        if (!esCampoValido(usuario.getTelefono()) || !usuario.getTelefono().matches("^\\d{10}$")) {
            throw new InvalidDataException("El número de teléfono debe ser un número válido de 10 dígitos.");
        }
        if (usuarioRepositorio.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new InvalidDataException("Ya existe un usuario con este correo.");
        }
        asignarRelaciones(usuario, idCiudad, idRol, idImagenUsuario);
        return usuarioRepositorio.save(usuario);
    }
    // Metodo para consultar un usuario por ID
    public Usuario consultarUsuarioById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no debe ser nulo");
        }
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }
    //Metodo para consultar todos los usuarios
    public List<Usuario> consultarUsuarios() {
        return usuarioRepositorio.findAll();
    }
    // Metodo para actualizar un usuario
    public Usuario actualizarUsuario(Integer id, Usuario nuevoUsuario, Integer idCiudad, Integer idRol, Integer idImagenUsuario) {
        System.out.println("ID recibido en servicio: " + id);
        Usuario usuarioActualizado = consultarUsuarioById(id);
        System.out.println("Usuario antes de actualizar: " + usuarioActualizado); // Verifica el usuario obtenido
        if (esCampoValido(nuevoUsuario.getNombres())) {
            usuarioActualizado.setNombres(nuevoUsuario.getNombres());
        }
        if (esCampoValido(nuevoUsuario.getApellidos())) {
            usuarioActualizado.setApellidos(nuevoUsuario.getApellidos());
        }
        if (esCampoValido(nuevoUsuario.getDireccion())) {
            usuarioActualizado.setDireccion(nuevoUsuario.getDireccion());
        }
        if (esCampoValido(nuevoUsuario.getCorreo())) {
            usuarioActualizado.setCorreo(nuevoUsuario.getCorreo());
        }
        if (esCampoValido(nuevoUsuario.getTelefono())) {
            usuarioActualizado.setTelefono(nuevoUsuario.getTelefono());
        }
        // Solo asignar relaciones si los IDs no son null
        asignarRelaciones(usuarioActualizado, idCiudad, idRol, idImagenUsuario);
        System.out.println("Usuario después de actualizar: " + usuarioActualizado); // Verifica los cambios antes de guardar
        return usuarioRepositorio.save(usuarioActualizado);
    }
    // Metodo para eliminar un usuario
    public String eliminarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        if (usuario.getImagenUsuario() != null) {
            int idImagen = usuario.getImagenUsuario().getId();
            usuario.setImagenUsuario(null);
            try {
                imagenesUsuarioServicio.eliminarImagen(idImagen);
            } catch (Exception e) {
                System.err.println("Error al eliminar la imagen: " + e.getMessage());
            }
        }
        //Eliminar imagenes de los comentarios
        List<Comentario> comentariosUsuario = comentarioServicio.consultarComentariosPorUsuario(idUsuario);
        if(comentariosUsuario != null){
            for(Comentario comentario : comentariosUsuario){
                imagenesComentarioServicio.eliminarImagen(comentario.getImagenesComentarios());
            }
        } else{
            return "No se encontraron comentarios para el usuario con id: " +idUsuario;
        }
        //Eliminar imagenes de las publicaciones
        List<Publicacion> publicacionesUsuario = publicacionServicio.consultarPublicacionesPorUsuario(idUsuario);
        if(publicacionesUsuario != null){
            for(Publicacion publicacion : publicacionesUsuario){
                imagenesPublicacionServicio.eliminarImagen(publicacion.getImagenesPublicaciones());
            }
        } else{
            return "No se encontraron publicaciones para el usuario con id: " +idUsuario;
        }
        usuarioRepositorio.deleteById(idUsuario);
        return "Usuario eliminado con éxito";
    }
}
