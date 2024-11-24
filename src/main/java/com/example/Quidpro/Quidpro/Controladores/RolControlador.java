package com.example.Quidpro.Quidpro.Controladores;


import com.example.Quidpro.Quidpro.Entidades.Rol;
import com.example.Quidpro.Quidpro.Servicios.RolServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rol")
public class RolControlador {
    @Autowired
    private final RolServicio rolServicio;
    public RolControlador(RolServicio rolServicio) {
        this.rolServicio = rolServicio;
    }
    //Metodo para consultar por id
    @GetMapping("/{id}")
    public ResponseEntity<Rol> consultarRolById(@PathVariable Integer id){
        Optional<Rol> rol = rolServicio.consultarRolById(id);
        return rol.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    //Metodo para consultar todos
    @GetMapping
    public ResponseEntity<List<Rol>> consultarRoles(){
        List<Rol> roles = rolServicio.consultarRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
    //Metodo para guardar un nuevo departamento
    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol){
        Rol rolGuardar = rolServicio.crearRol(rol);
        return new ResponseEntity<>(rolGuardar, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Rol> editarRol(@PathVariable Integer id, @RequestBody Rol rol){
        Rol rolActualizado = rolServicio.actualizarRol(id, rol);
        return new ResponseEntity<>(rolActualizado, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRol(@PathVariable Integer id){
        String respuesta = rolServicio.eliminarRol(id);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
