package com.example.Quidpro.Quidpro.Controladores;

import com.example.Quidpro.Quidpro.Entidades.ImagenesUsuario;
import com.example.Quidpro.Quidpro.Entidades.Usuario;
import com.example.Quidpro.Quidpro.Servicios.CiudadServicio;
import com.example.Quidpro.Quidpro.Servicios.ImagenesUsuarioServicio;
import com.example.Quidpro.Quidpro.Servicios.RolServicio;
import com.example.Quidpro.Quidpro.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {
    @Autowired
    private final UsuarioServicio usuarioServicio;
    private final CiudadServicio ciudadServicio;
    private final RolServicio rolServicio;
    private final ImagenesUsuarioServicio imagenesUsuarioServicio;

    public UsuarioControlador (UsuarioServicio usuarioServicio, CiudadServicio ciudadServicio, RolServicio rolServicio, ImagenesUsuarioServicio imagenesUsuarioServicio){
        this.usuarioServicio = usuarioServicio;
        this.ciudadServicio = ciudadServicio;
        this.rolServicio = rolServicio;
        this.imagenesUsuarioServicio = imagenesUsuarioServicio;
    }
    //Metodo para consultar usuarios por id
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> consultarUsuariobyId(@PathVariable Integer id){
        Usuario usuario = usuarioServicio.consultarUsuarioById(id);
        return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }
    //Metodo para consultar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> consultarUsuarios(){
        List<Usuario> usuarios = usuarioServicio.consultarUsuarios();
        return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
    }
    //Metodo para crear usuario
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Usuario> crearUsuario(
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("direccion") String direccion,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("idCiudad") Integer idCiudad,
            @RequestParam("idRol") Integer idRol,
            @RequestParam("imagen") MultipartFile imagen
    ) {
        ImagenesUsuario imagenesUsuario = null;
        try {
            if (imagen.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            imagenesUsuario = imagenesUsuarioServicio.guardarImagenes(imagen);
            Integer idImagen = imagenesUsuario.getId();
            // Crear el objeto Usuario manualmente
            Usuario usuario = new Usuario();
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setDireccion(direccion);
            usuario.setCorreo(correo);
            usuario.setTelefono(telefono);
            usuario.setImagenUsuario(imagenesUsuario);
            Usuario usuarioGuardado = usuarioServicio.crearUsuario(usuario, idCiudad, idRol, idImagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (Exception e) {
            if (imagenesUsuario != null && imagenesUsuario.getId() != null) {
                imagenesUsuarioServicio.eliminarImagen(imagenesUsuario.getId());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Integer id,
            @RequestParam(value = "nombres", required = false) String nombres,
            @RequestParam(value = "apellidos", required = false) String apellidos,
            @RequestParam(value = "direccion", required = false) String direccion,
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "idCiudad", required = false) Integer idCiudad,
            @RequestParam(value = "idRol", required = false) Integer idRol,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) {
        System.out.println("ID recibido: " + id); // Aquí verificas si el id llega correctamente

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            Usuario usuarioExistente = usuarioServicio.consultarUsuarioById(id);
            if (usuarioExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usuario no encontrado
            }
            if (nombres != null) {
                usuarioExistente.setNombres(nombres);
            }
            if (apellidos != null) {
                usuarioExistente.setApellidos(apellidos);
            }
            if (direccion != null) {
                usuarioExistente.setDireccion(direccion);
            }
            if (correo != null) {
                usuarioExistente.setCorreo(correo);
            }
            if (telefono != null) {
                usuarioExistente.setTelefono(telefono);
            }
            Integer idImagen = null;
            if (imagen != null && !imagen.isEmpty()) {
                if (usuarioExistente.getImagenUsuario() != null) {
                    imagenesUsuarioServicio.actualizarImagen(usuarioExistente.getImagenUsuario().getId(), imagen);
                } else {
                    ImagenesUsuario nuevaImagen = imagenesUsuarioServicio.guardarImagenes(imagen);
                    usuarioExistente.setImagenUsuario(nuevaImagen);
                }
            }
            Usuario usuarioGuardado = usuarioServicio.actualizarUsuario(id, usuarioExistente, idCiudad, idRol, idImagen);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioGuardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //Metodo para eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id){
        String mensaje = usuarioServicio.eliminarUsuario(id);
        return ResponseEntity.ok(mensaje);
    }

}
