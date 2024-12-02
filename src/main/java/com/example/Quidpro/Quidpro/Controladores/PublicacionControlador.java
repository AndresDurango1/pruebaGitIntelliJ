package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import com.example.Quidpro.Quidpro.Entidades.Publicacion;
import com.example.Quidpro.Quidpro.Excepciones.ResourceNotFoundException;
import com.example.Quidpro.Quidpro.Servicios.ImagenesPublicacionServicio;
import com.example.Quidpro.Quidpro.Servicios.PublicacionServicio;
import com.example.Quidpro.Quidpro.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionControlador {
    @Autowired
    private final PublicacionServicio publicacionServicio;
    private final ImagenesPublicacionServicio imagenesPublicacionServicio;
    private final UsuarioServicio usuarioServicio;
    public PublicacionControlador(PublicacionServicio publicacionServicio, ImagenesPublicacionServicio imagenesPublicacionServicio, UsuarioServicio usuarioServicio){
        this.publicacionServicio = publicacionServicio;
        this.imagenesPublicacionServicio = imagenesPublicacionServicio;
        this.usuarioServicio = usuarioServicio;
    }
    //Metodos auxiliares para manejar excepciones
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    //Metodo para consultar una publicacion por id
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> consultarPublicacionById(@PathVariable Integer id){
        Publicacion publicacion = publicacionServicio.consultarPublicacionById(id);
        return new ResponseEntity<>(publicacion, HttpStatus.OK);
    }
    //Metodo para consultar todas las publicaciones
    @GetMapping
    public ResponseEntity<List<Publicacion>> consultarPublicaciones(){
        List<Publicacion> publicaciones = publicacionServicio.consultarPublicaciones();
        return new ResponseEntity<List<Publicacion>>(publicaciones, HttpStatus.OK);
    }
    //Metodo para crear una publicacion
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Publicacion> crearPublicacion(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("fecha_creacion") LocalDate fecha_creacion,
            @RequestParam(value = "fecha_actualizacion", required = false) LocalDate fecha_actualizacion,
            @RequestParam("tag") String tag,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes) {
        try {
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
            Publicacion publicacion = new Publicacion();
            publicacion.setTitulo(titulo);
            publicacion.setDescripcion(descripcion);
            publicacion.setFecha_creacion(fecha_creacion);
            publicacion.setFecha_actualizacion(fecha_actualizacion);
            publicacion.setTag(tag);
            Publicacion publicacionGuardada = publicacionServicio.crearPublicacion(publicacion, idUsuario);
            if (imagenes != null) {
                List<ImagenesPublicacion> imagenesGuardadas = imagenesPublicacionServicio.guardarImagenes(imagenes, publicacionGuardada);
                publicacionGuardada.setImagenesPublicaciones(imagenesGuardadas);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(publicacionGuardada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para actualizar una publicación
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> actualizarPublicacion(
            @PathVariable Integer id,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "fecha_actualizacion", required = false) LocalDate fecha_actualizacion,
            @RequestParam("tag") String tag,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes
    ) {
        try {
            // 1. Validar y obtener la publicación existente
            Publicacion publicacionExistente = publicacionServicio.consultarPublicacionById(id);
            if (publicacionExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            // 2. Actualizar los datos básicos de la publicación
            publicacionExistente.setTitulo(titulo);
            publicacionExistente.setDescripcion(descripcion);
            publicacionExistente.setFecha_actualizacion(fecha_actualizacion != null ? fecha_actualizacion : LocalDate.now());
            publicacionExistente.setTag(tag);
            // 3. Gestionar imágenes existentes y nuevas
            List<ImagenesPublicacion> imagenesExistentes = publicacionExistente.getImagenesPublicaciones();
            List<ImagenesPublicacion> imagenesFinales = new ArrayList<>();
            Set<Integer> idsImagenesActualizadas = new HashSet<>();
            if (imagenes != null && imagenes.length > 0) {
                for (MultipartFile imagen : imagenes) {
                    ImagenesPublicacion imagenExistente = imagenesExistentes.stream()
                            .filter(img -> img.getTitulo().equals("publicacion_"+publicacionExistente.getId()+"_"+imagen.getOriginalFilename()))
                            .findFirst()
                            .orElse(null);
                    System.out.println("Imagen Existente: " + imagenExistente);
                    if (imagenExistente != null) {
                        // Actualizar imagen existente
                        imagenExistente = imagenesPublicacionServicio.actualizarImagen(imagenExistente.getId(), imagen, publicacionExistente);
                        idsImagenesActualizadas.add(imagenExistente.getId());
                        imagenesFinales.add(imagenExistente);
                    } else {
                        // Guardar nueva imagen
                        List<ImagenesPublicacion> imagenesGuardadas = imagenesPublicacionServicio.guardarImagenes(imagenes, publicacionExistente);
                        imagenesFinales.addAll(imagenesGuardadas);
                    }
                }
                Iterator<ImagenesPublicacion> iterator = imagenesExistentes.iterator();
                List<ImagenesPublicacion> imagenesAEliminar = new ArrayList<>();
                while (iterator.hasNext()) {
                    ImagenesPublicacion imagenExistente = iterator.next();
                    if (!idsImagenesActualizadas.contains(imagenExistente.getId())) {
                        // Agregar la imagen a eliminar, no la elimines todavía
                        imagenesAEliminar.add(imagenExistente);
                    }
                }
                // Eliminar imágenes fuera del bucle de iteración
                for (ImagenesPublicacion imagen : imagenesAEliminar) {
                    // Desasociar la imagen de la publicación
                    publicacionExistente.getImagenesPublicaciones().remove(imagen);
                    imagenesPublicacionServicio.eliminarImagen(List.of(imagen));
                }
            }
            // Actualizar las imágenes de la publicación con las nuevas o modificadas
            publicacionExistente.setImagenesPublicaciones(imagenesFinales);
            // 4. Guardar publicación actualizada
            Publicacion publicacionActualizada = publicacionServicio.actualizarPublicacion(id, publicacionExistente, idUsuario);
            return ResponseEntity.status(HttpStatus.OK).body(publicacionActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para eliminar una publicacion
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable Integer id){
        String mensaje = publicacionServicio.eliminarPublicacion(id);
        return ResponseEntity.ok(mensaje);
    }
}
