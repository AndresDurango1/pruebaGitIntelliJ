package com.example.Quidpro.Quidpro.Controladores;

import com.example.Quidpro.Quidpro.Entidades.Departamento;
import com.example.Quidpro.Quidpro.Servicios.DepartamentoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departamento")
public class DepartamentoControlador {

    private final DepartamentoServicio departamentoServicio;
    @Autowired
    public DepartamentoControlador(DepartamentoServicio departamentoServicio) {
        this.departamentoServicio = departamentoServicio;
    }
    //Metodo para consultar por id
    @GetMapping("/{id}")
    public ResponseEntity<Departamento> consultarDepartamentoById(@PathVariable Integer id){
        Optional<Departamento> departamento = departamentoServicio.consultarDepartamentoById(id);
        return departamento.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    //Metodo para consultar todos
    @GetMapping
    public ResponseEntity<List<Departamento>> consultarDepartamentos(){
        List<Departamento> departamentos = departamentoServicio.consultarDepartamentos();
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }
    //Metodo para guardar un nuevo departamento
    @PostMapping
    public ResponseEntity<Departamento> crearDepartamento(@RequestBody Departamento departamento){
        Departamento departamentoGuardar = departamentoServicio.crearDepartamento(departamento);
        return new ResponseEntity<>(departamentoGuardar, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Departamento> editarDepartamento(@PathVariable Integer id, @RequestBody Departamento departamento){
        Departamento departamentoActualizado = departamentoServicio.actualizarDepartamento(id, departamento);
        return new ResponseEntity<>(departamentoActualizado, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDepartamento(@PathVariable Integer id){
        String respuesta = departamentoServicio.eliminarDepartamento(id);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
