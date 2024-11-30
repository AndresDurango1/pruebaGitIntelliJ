package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import com.example.Quidpro.Quidpro.Entidades.Publicacion;
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
@RequestMapping("/api/publicaciones")
public class PublicacionControlador {
    @Autowired
    private final PublicacionServicio publicacionServicio;
    private final ImagenesPublicacionServicio imagenesPublicacionServicio;
    public PublicacionControlador(PublicacionServicio publicacionServicio, ImagenesPublicacionServicio imagenesPublicacionServicio){
        this.publicacionServicio = publicacionServicio;
        this.imagenesPublicacionServicio = imagenesPublicacionServicio;
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
    @PostMapping
    public ResponseEntity<Publicacion> crearPublicacion(
            @RequestBody Publicacion publicacion,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false)MultipartFile[] imagenes
            ){
        try {
            List<Integer> idsImagenes = new ArrayList<>();
            if(imagenes != null){
                List<ImagenesPublicacion> imagenesGuardadas = imagenesPublicacionServicio.guardarImagenes(imagenes);
                idsImagenes = imagenesGuardadas.stream().map(ImagenesPublicacion::getId).toList();
            }
            Publicacion publicacionGuardada = publicacionServicio.crearPublicacion(publicacion, idUsuario, idsImagenes);
            return ResponseEntity.status(HttpStatus.CREATED).body(publicacionGuardada);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para actualizar una publicacion
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> actualizarPublicacion(
            @PathVariable Integer id,
            @RequestBody Publicacion publicacion,
            @RequestParam Integer idUsuario,
            @RequestParam(value = "imagenes", required = false)MultipartFile[] imagenes
    ){
        try {
            List<Integer> idsImagenes = new ArrayList<>();
            if(imagenes != null){
                List<ImagenesPublicacion> imagenesGuardadas = imagenesPublicacionServicio.guardarImagenes(imagenes);
                idsImagenes = imagenesGuardadas.stream().map(ImagenesPublicacion::getId).toList();
            }
            Publicacion publicacionActualizada = publicacionServicio.actualizarPublicacion(id, publicacion, idUsuario, idsImagenes);
            return ResponseEntity.status(HttpStatus.OK).body(publicacionActualizada);
        } catch (Exception e){
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
