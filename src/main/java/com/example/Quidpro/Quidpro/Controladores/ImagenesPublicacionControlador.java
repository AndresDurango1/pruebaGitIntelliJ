package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import com.example.Quidpro.Quidpro.Entidades.Publicacion;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Servicios.ImagenesPublicacionServicio;
import com.example.Quidpro.Quidpro.Servicios.PublicacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/imagenesPublicacion")
@CrossOrigin(origins = "http://localhost:4200")
public class ImagenesPublicacionControlador {
    @Autowired
    private final ImagenesPublicacionServicio imagenesPublicacionServicio;
    private final PublicacionServicio publicacionServicio;
    public ImagenesPublicacionControlador(ImagenesPublicacionServicio imagenesPublicacionServicio, PublicacionServicio publicacionServicio) {
        this.imagenesPublicacionServicio = imagenesPublicacionServicio;
        this.publicacionServicio = publicacionServicio;
    }
    // Metodo para consultar imagenes publicaciones por id
    @GetMapping("/{id}")
    public ResponseEntity<ImagenesPublicacion> consultarImagenPublicacionById(@PathVariable Integer id) {
        try {
            ImagenesPublicacion imagen = imagenesPublicacionServicio.consultarImagenPorId(id);
            return new ResponseEntity<>(imagen, HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    // Metodo para consultar todas las imagenes de publicaciones
    @GetMapping
    public ResponseEntity<List<ImagenesPublicacion>> consultarImagenesPublicaciones() {
        try {
            List<ImagenesPublicacion> imagenes = imagenesPublicacionServicio.consultarImagenes();
            return new ResponseEntity<>(imagenes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    // Metodo para guardar una imagen
    @PostMapping
    public ResponseEntity<List<ImagenesPublicacion>> guardarImagenesConPublicacion(
            @PathVariable("idPublicacion") Integer idPublicacion,
            @RequestParam("archivos") MultipartFile[] archivos) {
        try {
            // Obtener la publicación desde el servicio
            Publicacion publicacion = publicacionServicio.consultarPublicacionById(idPublicacion);
            if (publicacion == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            // Guardar las imágenes asociadas a la publicación
            List<ImagenesPublicacion> imagenesGuardadas = imagenesPublicacionServicio.guardarImagenes(archivos, publicacion);
            return new ResponseEntity<>(imagenesGuardadas, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
    // Metodo para actualizar varias imágenes
    @PutMapping("/{id}")
    public ResponseEntity<List<ImagenesPublicacion>> actualizarImagenes(
            @PathVariable int id,
            @RequestParam("imagenesIds") List<Integer> imagenesIds, // Recibir lista de IDs
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            // Iterar sobre los IDs y actualizar las imágenes
            List<ImagenesPublicacion> imagenesActualizadas = new ArrayList<>();
            for (Integer imagenId : imagenesIds) {
                ImagenesPublicacion imagenActualizada = imagenesPublicacionServicio.actualizarImagen(imagenId, archivo, );
                imagenesActualizadas.add(imagenActualizada);
            }
            return new ResponseEntity<>(imagenesActualizadas, HttpStatus.OK);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}