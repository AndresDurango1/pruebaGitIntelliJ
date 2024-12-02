package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.Comentario;
import com.example.Quidpro.Quidpro.Entidades.ImagenesComentario;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Servicios.ComentarioServicio;
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
    private final ComentarioServicio comentarioServicio;
    public ImagenesComentarioControlador(ImagenesComentarioServicio imagenesComentarioServicio, ComentarioServicio comentarioServicio) {
        this.imagenesComentarioServicio = imagenesComentarioServicio;
        this.comentarioServicio = comentarioServicio;
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
    public ResponseEntity<List<ImagenesComentario>> guardarImagenesComentario(
            @PathVariable("idComentario") Integer idComentario,
            @RequestParam("archivos") MultipartFile[] archivos
    ){
        try {
            // Obtener el comentario desde el servicio
            Comentario comentario = comentarioServicio.consultarComentarioById(idComentario);
            if(comentario == null){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            // Guardar las imágenes asociadas al comentario
            List<ImagenesComentario> imagenesGuardadas = imagenesComentarioServicio.guardarImagenes(archivos, comentario);
            return new ResponseEntity<>(imagenesGuardadas, HttpStatus.CREATED);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
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
    }*/
}
