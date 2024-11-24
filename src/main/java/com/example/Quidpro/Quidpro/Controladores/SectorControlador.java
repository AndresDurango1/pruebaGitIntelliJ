package com.example.Quidpro.Quidpro.Controladores;

import com.example.Quidpro.Quidpro.Entidades.Sector;
import com.example.Quidpro.Quidpro.Servicios.SectorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sector")
public class SectorControlador {
    @Autowired
    private final SectorServicio sectorServicio;
    public SectorControlador(SectorServicio sectorServicio) {
        this.sectorServicio = sectorServicio;
    }
    //Metodo para consultar por id
    @GetMapping("/{id}")
    public ResponseEntity<Sector> consultarSectorById(@PathVariable Integer id){
        Optional<Sector> sector = sectorServicio.consultarSectorById(id);
        return sector.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    //Metodo para consultar todos
    @GetMapping
    public ResponseEntity<List<Sector>> consultarSectores(){
        List<Sector> sectores = sectorServicio.consultarSectores();
        return new ResponseEntity<>(sectores, HttpStatus.OK);
    }
    //Metodo para guardar un nuevo departamento
    @PostMapping
    public ResponseEntity<Sector> crearSector(@RequestBody Sector sector){
        Sector sectorGuardar = sectorServicio.crearSector(sector);
        return new ResponseEntity<>(sectorGuardar, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Sector> editarSector(@PathVariable Integer id, @RequestBody Sector sector){
        Sector sectorActualizado = sectorServicio.actualizarSector(id, sector);
        return new ResponseEntity<>(sectorActualizado, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarSector(@PathVariable Integer id){
        String respuesta = sectorServicio.eliminarSector(id);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
