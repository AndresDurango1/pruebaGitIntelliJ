package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.ImagenesComentario;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Servicios.ImagenesComentarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/imagenesComentario")
public class ImagenesComentarioControlador {
    @Autowired
    private final ImagenesComentarioServicio imagenesComentarioServicio;
    public ImagenesComentarioControlador(ImagenesComentarioServicio imagenesComentarioServicio) {
        this.imagenesComentarioServicio = imagenesComentarioServicio;
    }
    //Metodo para consultar imagen de un comentario por id
    @GetMapping("/{id}")
    public ResponseEntity<ImagenesComentario> consultarImagenComentarioById(@PathVariable Integer id){
        try {
            ImagenesComentario imagen = imagenesComentarioServicio.consultarImagenPorId(id);
            return new ResponseEntity<>(imagen, HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //Metodo para consultar todas las imagenes de publicaciones
    @GetMapping
    public ResponseEntity<List<ImagenesComentario>> consultarImagenesComentario(){
        try{
            List<ImagenesComentario> imagenes = imagenesComentarioServicio.consultarImagenes();
            return new ResponseEntity<>(imagenes, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //Metodo para guardar una imagen
    @PostMapping
    public ResponseEntity<List<ImagenesComentario>> guardarImagenesComentario(@RequestParam("archivos") MultipartFile[] archivos){
        try {
            List<ImagenesComentario> imagenesGuardadas = imagenesComentarioServicio.guardarImagenes(archivos);
            return new ResponseEntity<>(imagenesGuardadas, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Metodo para actualizar una imagen por id
    @PutMapping("/{id}")
    public ResponseEntity<ImagenesComentario> actualizarImagen(@PathVariable int id, @RequestParam("archivo") MultipartFile archivo) {
        try {
            ImagenesComentario imagenActualizada = imagenesComentarioServicio.actualizarImagen(id, archivo);
            return new ResponseEntity<>(imagenActualizada, HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Metodo para eliminar imagenes por id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarImagenPublicacion(@PathVariable Integer id){
        try {
            String mensaje = imagenesComentarioServicio.eliminarImagen(id);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
