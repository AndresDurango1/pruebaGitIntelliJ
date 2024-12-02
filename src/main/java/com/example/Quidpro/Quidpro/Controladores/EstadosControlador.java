package com.example.Quidpro.Quidpro.Controladores;

import com.example.Quidpro.Quidpro.Entidades.Estados;
import com.example.Quidpro.Quidpro.Servicios.EstadosServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estados")
public class EstadosControlador {
    @Autowired
    private final EstadosServicio estadosServicio;
    public EstadosControlador(EstadosServicio estadosServicio){
        this.estadosServicio = estadosServicio;
    }
    //Metodo para consultar por id
    @GetMapping("/{id}")
    public ResponseEntity<Estados> consultarEstadoById(@PathVariable Integer id){
        Optional<Estados> estado = estadosServicio.consultarEstadoById(id);
        return estado.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    //Metodo para consultar todos los estados
    @GetMapping
    public ResponseEntity<List<Estados>> consultarEstados(){
        List<Estados> estados = estadosServicio.consultarEstados();
        return new ResponseEntity<>(estados, HttpStatus.OK);
    }
    //Metodo para crear un estado
    @PostMapping
    public ResponseEntity<Estados> crearEstado(
            @RequestParam(name = "estado", required = true) String estadoValue
    ){
        Estados estadoNuevo = new Estados();
        estadoNuevo.setEstado(estadoValue);
        Estados estadoGuardar = estadosServicio.crearEstados(estadoNuevo);
        return new ResponseEntity<>(estadoGuardar, HttpStatus.CREATED);
    }
    //Metodo para actualizar un estado
    @PutMapping("/{id}")
    public ResponseEntity<Estados> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam(name = "estado", required = true) String estadoValue){
        Estados estado = new Estados();
        estado.setEstado(estadoValue);
        Estados estadoActualizado = estadosServicio.actualizarEstado(id, estado);
        return new ResponseEntity<>(estadoActualizado, HttpStatus.OK);
    }
    //Metodo para eliminar un estado
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEstado(@PathVariable Integer id){
        String respuesta = estadosServicio.eliminarEstado(id);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
