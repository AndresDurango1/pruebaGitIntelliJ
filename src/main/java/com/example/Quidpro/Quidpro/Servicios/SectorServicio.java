package com.example.Quidpro.Quidpro.Servicios;

import com.example.Quidpro.Quidpro.Entidades.Sector;
import com.example.Quidpro.Quidpro.Repositorios.SectorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectorServicio {
    @Autowired
    private final SectorRepositorio sectorRepositorio;
    public SectorServicio(SectorRepositorio sectorRepositorio) {
        this.sectorRepositorio = sectorRepositorio;
    }
    //Definicion de Métodos crud para la entidad Departamento (CRUD)
    //Metodo para Crear Registros
    public Sector crearSector(Sector sector){
        return sectorRepositorio.save(sector);
    }
    //Metodo para Consultar todos los Registros
    public List<Sector> consultarSectores(){
        return sectorRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Optional<Sector> consultarSectorById(Integer id){
        return sectorRepositorio.findById(id);
    }
    //Metodo para Actualizar un Registro por Id
    public Sector actualizarSector(Integer id, Sector sector){
        Optional<Sector> sectorActualizar = consultarSectorById(id);
        if (sectorActualizar.isPresent()){
            Sector sectorActualizado = sectorActualizar.get();
            sectorActualizado.setSector(sector.getSector());
            return sectorRepositorio.save(sectorActualizado);
        } else{
            throw new RuntimeException("Sector no encontrado con el ID: " + id);
        }
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarSector(Integer id){
        Optional<Sector> sectorEliminar = consultarSectorById(id);
        if(sectorEliminar.isPresent()){
            sectorRepositorio.deleteById(id);
            return "Sector eliminado con éxito";
        } else{
            throw new RuntimeException("Sector no encontrado con el ID: " + id);
        }
    }
}

