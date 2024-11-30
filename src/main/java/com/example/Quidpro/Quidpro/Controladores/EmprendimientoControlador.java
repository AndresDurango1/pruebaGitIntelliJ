package com.example.Quidpro.Quidpro.Controladores;

import com.example.Quidpro.Quidpro.Entidades.Emprendimiento;
import com.example.Quidpro.Quidpro.Servicios.EmprendimientoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprendimientos")
public class EmprendimientoControlador {
    @Autowired
    private final EmprendimientoServicio emprendimientoServicio;
    public EmprendimientoControlador(EmprendimientoServicio emprendimientoServicio){
        this.emprendimientoServicio = emprendimientoServicio;
    }
    //Metodo para consultar un emprendimiento por id
    @GetMapping("/{id}")
    public ResponseEntity<Emprendimiento> consultarEmprendimientoById(@PathVariable Integer id){
        Emprendimiento emprendimiento = emprendimientoServicio.consultarEmprendimientoById(id);
        return new ResponseEntity<Emprendimiento>(emprendimiento, HttpStatus.OK);
    }
    //Metodo para consultar todos los emprendimientos
    @GetMapping
    public ResponseEntity<List<Emprendimiento>> consultarEmprendimientos(){
        List<Emprendimiento> emprendimientos = emprendimientoServicio.consultarEmprendimientos();
        return new ResponseEntity<List<Emprendimiento>>(emprendimientos, HttpStatus.OK);
    }
    //Metodo para crear un emprendimiento
    @PostMapping
    public ResponseEntity<Emprendimiento> crearEmprendimiento(@RequestBody Emprendimiento emprendimiento, @RequestParam Integer idUsuario, @RequestParam Integer idDepartamento, @RequestParam Integer idCiudad, @RequestParam Integer idSector, @RequestParam Integer idEstado){
        Emprendimiento nuevoEmprendimiento = emprendimientoServicio.crearEmprendimiento(emprendimiento, idUsuario, idDepartamento, idCiudad, idSector, idEstado);
        return new ResponseEntity<Emprendimiento>(nuevoEmprendimiento, HttpStatus.CREATED);
    }
    //Metodo para actualizar un emprendimiento
    @PutMapping("/{id}")
    public ResponseEntity<Emprendimiento> actualizarEmprendimiento(@PathVariable Integer id, @RequestBody Emprendimiento nuevoEmprendimiento, @RequestParam Integer idUsuario, @RequestParam Integer idDepartamento, @RequestParam Integer idCiudad, @RequestParam Integer idSector, @RequestParam Integer idEstado){
        Emprendimiento emprendimientoActualizado = emprendimientoServicio.actualizarEmprendimiento(id, nuevoEmprendimiento, idUsuario, idDepartamento, idCiudad, idSector, idEstado);
        return new ResponseEntity<Emprendimiento>(emprendimientoActualizado, HttpStatus.OK);
    }
    //Metodo para eliminar un emprendimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEmprendimiento(@PathVariable Integer id){
        String mensaje = emprendimientoServicio.eliminarEmprendimiento(id);
        return ResponseEntity.ok(mensaje);
    }
}
