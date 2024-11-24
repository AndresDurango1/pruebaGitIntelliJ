package com.example.Quidpro.Quidpro.Servicios;

import com.example.Quidpro.Quidpro.Entidades.Rol;
import com.example.Quidpro.Quidpro.Repositorios.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServicio {
    @Autowired
    private final RolRepositorio rolRepositorio;
    public RolServicio(RolRepositorio rolRepositorio) {
        this.rolRepositorio = rolRepositorio;
    }
    //Definicion de Métodos crud para la entidad Departamento (CRUD)
    //Metodo para Crear Registros
    public Rol crearRol(Rol rol){
        return rolRepositorio.save(rol);
    }
    //Metodo para Consultar todos los Registros
    public List<Rol> consultarRoles(){
        return rolRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Optional<Rol> consultarRolById(Integer id){
        return rolRepositorio.findById(id);
    }
    //Metodo para Actualizar un Registro por Id
    public Rol actualizarRol(Integer id, Rol rol){
        Optional<Rol> rolActualizar = consultarRolById(id);
        if (rolActualizar.isPresent()){
            Rol rolActualizado = rolActualizar.get();
            rolActualizado.setRol(rol.getRol());
            return rolRepositorio.save(rolActualizado);
        } else{
            throw new RuntimeException("Rol no encontrado con el ID: " + id);
        }
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarRol(Integer id){
        Optional<Rol> rolEliminar = consultarRolById(id);
        if(rolEliminar.isPresent()){
            rolRepositorio.deleteById(id);
            return "Rol eliminado con éxito";
        } else{
            throw new RuntimeException("Rol no encontrado con el ID: " + id);
        }
    }
}
