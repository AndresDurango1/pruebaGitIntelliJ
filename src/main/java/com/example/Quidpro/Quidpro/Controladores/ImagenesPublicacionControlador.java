package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Servicios.ImagenesPublicacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/imagenesPublicacion")
public class ImagenesPublicacionControlador {
    @Autowired
    private final ImagenesPublicacionServicio imagenesPublicacionServicio;
    public ImagenesPublicacionControlador(ImagenesPublicacionServicio imagenesPublicacionServicio){
        this.imagenesPublicacionServicio = imagenesPublicacionServicio;
    }
    //Metodo para consultar imagenes publicaciones por id
    @GetMapping("/{id}")
    public ResponseEntity<ImagenesPublicacion> consultarImagenPublicacionById(@PathVariable Integer id){
        try {
            ImagenesPublicacion imagen = imagenesPublicacionServicio.consultarImagenPorId(id);
            return new ResponseEntity<>(imagen, HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //Metodo para consultar todas las imagenes de publicaciones
    @GetMapping
    public ResponseEntity<List<ImagenesPublicacion>> consultarImagenesPublicaciones(){
        try{
            List<ImagenesPublicacion> imagenes = imagenesPublicacionServicio.consultarImagenes();
            return new ResponseEntity<>(imagenes, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //Metodo para guardar una imagen
    @PostMapping
    public ResponseEntity<List<ImagenesPublicacion>> guardarImagenesPublicacion(@RequestParam("archivos")MultipartFile[] archivos){
        try {
            List<ImagenesPublicacion> imagenesGuardadas = imagenesPublicacionServicio.guardarImagenes(archivos);
            return new ResponseEntity<>(imagenesGuardadas, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Metodo para actualizar una imagen por id
    @PutMapping("/{id}")
    public ResponseEntity<ImagenesPublicacion> actualizarImagen(@PathVariable int id, @RequestParam("archivo") MultipartFile archivo) {
        try {
            ImagenesPublicacion imagenActualizada = imagenesPublicacionServicio.actualizarImagen(id, archivo);
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
            String mensaje = imagenesPublicacionServicio.eliminarImagen(id);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
