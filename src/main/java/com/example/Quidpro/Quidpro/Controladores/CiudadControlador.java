package com.example.Quidpro.Quidpro.Controladores;

import com.example.Quidpro.Quidpro.Entidades.Ciudad;
import com.example.Quidpro.Quidpro.Servicios.CiudadServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadControlador {
    @Autowired
    private final CiudadServicio ciudadServicio;
    public CiudadControlador(CiudadServicio ciudadServicio) {
        this.ciudadServicio = ciudadServicio;
    }
    // Consultar Ciudad por ID
    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> consultarCiudadById(@PathVariable Integer id) {
        Ciudad ciudad = ciudadServicio.consultarCiudadById(id);
        return ResponseEntity.ok(ciudad);
    }
    // Consultar todas las Ciudades
    @GetMapping
    public ResponseEntity<List<Ciudad>> consultarCiudades() {
        List<Ciudad> ciudades = ciudadServicio.consultarCiudades();
        return ResponseEntity.ok(ciudades);
    }
    // Crear Ciudad
    @PostMapping
    public ResponseEntity<Ciudad> crearCiudad(@RequestBody Ciudad ciudad, @RequestParam Integer idDepartamento) {
        Ciudad nuevaCiudad = ciudadServicio.crearCiudad(ciudad, idDepartamento);
        return ResponseEntity.ok(nuevaCiudad);
    }
    // Actualizar Ciudad
    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizarCiudad(
            @PathVariable Integer id,
            @RequestBody Ciudad nuevaCiudad,
            @RequestParam(required = false) Integer idDepartamento) {
        Ciudad ciudadActualizada = ciudadServicio.actualizarCiudad(id, nuevaCiudad, idDepartamento);
        return ResponseEntity.ok(ciudadActualizada);
    }
    // Eliminar Ciudad
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCiudad(@PathVariable Integer id) {
        String mensaje = ciudadServicio.eliminarCiudad(id);
        return ResponseEntity.ok(mensaje);
    }
}
