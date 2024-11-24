package com.example.Quidpro.Quidpro.Servicios;

import com.example.Quidpro.Quidpro.Entidades.Departamento;
import com.example.Quidpro.Quidpro.Repositorios.DepartamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServicio {
    @Autowired
    private final DepartamentoRepositorio departamentoRepositorio;
    public DepartamentoServicio(DepartamentoRepositorio departamentoRepositorio) {
        this.departamentoRepositorio = departamentoRepositorio;
    }
    //Definicion de Métodos crud para la entidad Departamento (CRUD)
    //Metodo para Crear Registros
    public Departamento crearDepartamento(Departamento departamento){
        return departamentoRepositorio.save(departamento);
    }
    //Metodo para Consultar todos los Registros
    public List<Departamento> consultarDepartamentos(){
        return departamentoRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Optional<Departamento> consultarDepartamentoById(Integer id){
        return departamentoRepositorio.findById(id);
    }
    //Metodo para Actualizar un Registro por Id
    public Departamento actualizarDepartamento(Integer id, Departamento nuevoDepartamento){
        Optional<Departamento> departamentoActualizar = consultarDepartamentoById(id);
        if (departamentoActualizar.isPresent()){
            Departamento departamentoActualizado = departamentoActualizar.get();
            departamentoActualizado.setDepartamento(nuevoDepartamento.getDepartamento());
            return departamentoRepositorio.save(departamentoActualizado);
        } else{
            throw new RuntimeException("Departamento no encontrado con el ID: " + id);
        }
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarDepartamento(Integer id){
        Optional<Departamento> departamentoEliminar = consultarDepartamentoById(id);
        if(departamentoEliminar.isPresent()){
            departamentoRepositorio.deleteById(id);
            return "Departamento eliminado con éxito";
        } else{
            throw new RuntimeException("Departamento no encontrado con el ID: " + id);
        }
    }
}
