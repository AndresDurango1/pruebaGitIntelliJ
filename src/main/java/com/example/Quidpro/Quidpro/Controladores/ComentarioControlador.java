package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.Comentario;
import com.example.Quidpro.Quidpro.Entidades.ImagenesComentario;
import com.example.Quidpro.Quidpro.Servicios.ComentarioServicio;
import com.example.Quidpro.Quidpro.Servicios.ImagenesComentarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioControlador {
    @Autowired
    private final ComentarioServicio comentarioServicio;
    private final ImagenesComentarioServicio imagenesComentarioServicio;
    public ComentarioControlador (ComentarioServicio comentarioServicio, ImagenesComentarioServicio imagenesComentarioServicio){
        this.comentarioServicio = comentarioServicio;
        this.imagenesComentarioServicio = imagenesComentarioServicio;
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
    @PostMapping
    public ResponseEntity<Comentario> crearComentario (
            @RequestBody Comentario comentario,
            @RequestParam Integer idPublicacion,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes
        ){
        try{
            List<Integer> idsImagenes = new ArrayList<>();
            if(imagenes != null){
                List<ImagenesComentario> imagenesGuardadas = imagenesComentarioServicio.guardarImagenes(imagenes);
                idsImagenes = imagenesGuardadas.stream().map(ImagenesComentario::getId).toList();
            }
            Comentario comentarioGuardado = comentarioServicio.crearComentario(comentario, idPublicacion, idUsuario, idsImagenes);
            return  ResponseEntity.status(HttpStatus.CREATED).body(comentarioGuardado);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para actualizar un comentario
    @PutMapping("/{id}")
    public ResponseEntity<Comentario> actualizarComentario (
            @PathVariable Integer id,
            @RequestBody Comentario comentario,
            @RequestParam Integer idPublicacion,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes){
        try {
            List<Integer> idsImagenes = new ArrayList<>();
            if(imagenes != null){
                List<ImagenesComentario> imagenesGuardadas = imagenesComentarioServicio.guardarImagenes(imagenes);
                idsImagenes = imagenesGuardadas.stream().map(ImagenesComentario::getId).toList();
            }
            Comentario comentarioActualizado = comentarioServicio.actualizarComentarioById(id, comentario, idPublicacion, idUsuario, idsImagenes);
            return ResponseEntity.status(HttpStatus.OK).body(comentarioActualizado);
        } catch (Exception e){
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
