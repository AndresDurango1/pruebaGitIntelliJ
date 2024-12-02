package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.ImagenesUsuario;
import com.example.Quidpro.Quidpro.Servicios.ImagenesUsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/imagenesUsuario")
@CrossOrigin(origins = "http://localhost:4200")
public class ImagenesUsuarioControlador {
    @Autowired
    private final ImagenesUsuarioServicio imagenesUsuarioServicio;
    public ImagenesUsuarioControlador(ImagenesUsuarioServicio imagenesUsuarioServicio) {
        this.imagenesUsuarioServicio = imagenesUsuarioServicio;
    }
    // Consultar ImagenUsuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<ImagenesUsuario> consultarImagenUsuarioById(@PathVariable Integer id) {
        try {
            ImagenesUsuario imagenUsuario = imagenesUsuarioServicio.consultarImagenPorId(id);
            return ResponseEntity.ok(imagenUsuario);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    // Consultar todas las imágenes de usuario
    @GetMapping
    public ResponseEntity<List<ImagenesUsuario>> consultarImagenesUsuarios() {
        List<ImagenesUsuario> imagenes = imagenesUsuarioServicio.consultarImagenes();
        return ResponseEntity.ok(imagenes);
    }
    // Guardar una imagen de usuario
    @PostMapping
    public ResponseEntity<ImagenesUsuario> guardarImagenUsuario(@RequestParam("archivo") MultipartFile archivo) {
        try {
            ImagenesUsuario imagenUser = imagenesUsuarioServicio.guardarImagenes(archivo);
            return new ResponseEntity<>(imagenUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Actualizar una imagen de usuario
    @PutMapping("/{id}")
    public ResponseEntity<ImagenesUsuario> actualizarImagenUsuario(@PathVariable Integer id, @RequestParam("archivo") MultipartFile archivo) {
        try {
            ImagenesUsuario imagenActualizada = imagenesUsuarioServicio.actualizarImagen(id, archivo);
            return new ResponseEntity<>(imagenActualizada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    // Eliminar una imagen de usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarImagenUsuario(@PathVariable Integer id) {
        try {
            String mensaje = imagenesUsuarioServicio.eliminarImagen(id);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
