package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.Estados;
import com.example.Quidpro.Quidpro.Repositorios.EstadosRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadosServicio {
    @Autowired
    private final EstadosRepositorio estadosRepositorio;
    public EstadosServicio(EstadosRepositorio estadosRepositorio) {
        this.estadosRepositorio = estadosRepositorio;
    }
    //Definicion de Métodos crud para la entidad Departamento (CRUD)
    //Metodo para Crear Registros
    public Estados crearEstados(Estados estados){
        if (estados == null || estados.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo o tener nombre nulo.");
        }
        return estadosRepositorio.save(estados);
    }
    //Metodo para Consultar todos los Registros
    public List<Estados> consultarEstados(){
        return estadosRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Optional<Estados> consultarEstadoById(Integer id){
        return estadosRepositorio.findById(id);
    }
    //Metodo para Actualizar un Registro por Id
    public Estados actualizarEstado(Integer id, Estados estado){
        Optional<Estados> estadosActualizar = consultarEstadoById(id);
        if (estadosActualizar.isPresent()){
            Estados estadoActualizado = estadosActualizar.get();
            estadoActualizado.setEstado(estado.getEstado());
            return estadosRepositorio.save(estadoActualizado);
        } else{
            throw new RuntimeException("Estado no encontrado con el ID: " + id);
        }
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarEstado(Integer id){
        Optional<Estados> estadoEliminar = consultarEstadoById(id);
        if(estadoEliminar.isPresent()){
            estadosRepositorio.deleteById(id);
            return "Estado eliminado con éxito";
        } else{
            throw new RuntimeException("Estado no encontrado con el ID: " + id);
        }
    }
}
