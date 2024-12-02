package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.Comentario;
import com.example.Quidpro.Quidpro.Entidades.ImagenesComentario;
import com.example.Quidpro.Quidpro.Excepciones.ResourceNotFoundException;
import com.example.Quidpro.Quidpro.Servicios.ComentarioServicio;
import com.example.Quidpro.Quidpro.Servicios.ImagenesComentarioServicio;
import com.example.Quidpro.Quidpro.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioControlador {
    @Autowired
    private final ComentarioServicio comentarioServicio;
    private final ImagenesComentarioServicio imagenesComentarioServicio;
    private final UsuarioServicio usuarioServicio;
    public ComentarioControlador (ComentarioServicio comentarioServicio, ImagenesComentarioServicio imagenesComentarioServicio, UsuarioServicio usuarioServicio){
        this.comentarioServicio = comentarioServicio;
        this.imagenesComentarioServicio = imagenesComentarioServicio;
        this.usuarioServicio = usuarioServicio;
    }
    //Metodos auxiliares para manejar excepciones
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    //Metodo para consultar un comentario por id
    @GetMapping("/{id}")
    public ResponseEntity<Comentario> consultarComentarioById(@PathVariable Integer id){
        Comentario comentario = comentarioServicio.consultarComentarioById(id);
        return new ResponseEntity<Comentario>(comentario, HttpStatus.OK);
    }
    //Metodo para consultar todos los comentarios
    @GetMapping
    public ResponseEntity<List<Comentario>> consultarComentarios(){
        List<Comentario> comentarios = comentarioServicio.consultarComentarios();
        return new ResponseEntity<List<Comentario>>(comentarios, HttpStatus.OK);
    }
    //Metodo para crear un comentario con imagenes
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Comentario> crearComentario (
            @RequestParam("texto") String texto,
            @RequestParam(value = "fecha_creacion", required = false) LocalDate fecha_creacion,
            @RequestParam(value = "fecha_actualizacion", required = false) LocalDate fecha_actualizacion,
            @RequestParam Integer idPublicacion,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes
        ){
        try{
            if (imagenes != null && imagenes.length > 0) {
                System.out.println("Se recibieron " + imagenes.length + " imágenes.");
            } else {
                System.out.println("No se recibieron imágenes.");
            }
            if(fecha_creacion == null) {
                fecha_creacion = LocalDate.now();
            }
            if (fecha_actualizacion == null) {
                fecha_actualizacion = LocalDate.now();
            }
            Comentario comentario = new Comentario();
            comentario.setTexto(texto);
            comentario.setFecha_creacion(fecha_creacion);
            comentario.setFecha_actualizacion(fecha_actualizacion);
            Comentario comentarioGuardado = comentarioServicio.crearComentario(comentario, idPublicacion, idUsuario);

            List<Integer> idsImagenes = new ArrayList<>();

            if(imagenes != null){
                List<ImagenesComentario> imagenesGuardadas = imagenesComentarioServicio.guardarImagenes(imagenes, comentarioGuardado);
                comentarioGuardado.setImagenesComentarios(imagenesGuardadas);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(comentarioGuardado);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para actualizar un comentario
    @PutMapping("/{id}")
    public ResponseEntity<Comentario> actualizarComentario (
            @PathVariable Integer id,
            @RequestParam("texto") String texto,
            @RequestParam(value = "fecha_creacion", required = false) LocalDate fecha_creacion,
            @RequestParam(value = "fecha_actualizacion", required = false) LocalDate fecha_actualizacion,
            @RequestParam Integer idPublicacion,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes){
        try {
            // 1. Validar y obtener la publicación existente
            Comentario comentarioExistente = comentarioServicio.consultarComentarioById(id);
            if (comentarioExistente == null && imagenes.length > 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            // 2. Actualizar los datos básicos del comentario
            comentarioExistente.setTexto(texto);
            comentarioExistente.setFecha_actualizacion(fecha_actualizacion != null ? fecha_actualizacion : LocalDate.now());
            // 3. Gestionar imágenes existentes y nuevas
            List<ImagenesComentario> imagenesExistente = comentarioExistente.getImagenesComentarios();
            List<ImagenesComentario> imagenesFinales = new ArrayList<>();
            Set<Integer> idsImagenesActualizadas = new HashSet<>();
            if(imagenes != null && imagenes.length > 0){
                for (MultipartFile imagen : imagenes){
                    ImagenesComentario imagenExistente = imagenesExistente.stream()
                            .filter(img -> img.getTitulo().equals("publicacion_"+comentarioExistente.getId()+"_"+imagen.getOriginalFilename()))
                            .findFirst()
                            .orElse(null);
                    System.out.println("Imagen Existente: " + imagenExistente);
                    if(imagenExistente != null){
                        imagenExistente = imagenesComentarioServicio.actualizarImagen(imagenExistente.getId(),imagen, comentarioExistente);
                        idsImagenesActualizadas.add(imagenExistente.getId());
                        imagenesFinales.add(imagenExistente);
                    } else {
                        // Guardar nueva imagen
                        List<ImagenesComentario> imagenesGuardadas = imagenesComentarioServicio.guardarImagenes(imagenes, comentarioExistente);
                        imagenesFinales.addAll(imagenesGuardadas);
                    }
                }
                Iterator<ImagenesComentario> iterator = imagenesExistente.iterator();
                List<ImagenesComentario> imagenesAEliminar = new ArrayList<>();
                while (iterator.hasNext()) {
                    ImagenesComentario imagenExistente = iterator.next();
                    if (!idsImagenesActualizadas.contains(imagenExistente.getId())) {
                        imagenesAEliminar.add(imagenExistente);
                    }
                }
                // Eliminar imágenes fuera del bucle de iteración
                for (ImagenesComentario imagen : imagenesAEliminar) {
                    // Desasociar la imagen de la publicación
                    comentarioExistente.getImagenesComentarios().remove(imagen);
                    imagenesComentarioServicio.eliminarImagen(List.of(imagen));
                }
            }
            // Actualizar las imágenes de la publicación con las nuevas o modificadas
            comentarioExistente.setImagenesComentarios(imagenesFinales);
            // 4. Guardar comentario actualizado
            Comentario comentarioActualizado = comentarioServicio.actualizarComentarioById(id, comentarioExistente, idPublicacion, idUsuario);
            return ResponseEntity.status(HttpStatus.OK).body(comentarioActualizado);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para eliminar un comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarComentario(@PathVariable Integer id){
        String mensaje = comentarioServicio.eliminarComentario(id);
        return ResponseEntity.ok(mensaje);
    }
}
