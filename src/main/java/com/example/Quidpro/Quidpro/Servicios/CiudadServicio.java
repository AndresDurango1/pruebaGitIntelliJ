package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.Ciudad;
import com.example.Quidpro.Quidpro.Entidades.Departamento;
import com.example.Quidpro.Quidpro.Repositorios.CiudadRepositorio;
import com.example.Quidpro.Quidpro.Repositorios.DepartamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CiudadServicio {
    @Autowired
    private final CiudadRepositorio ciudadRepositorio;
    @Autowired
    private final DepartamentoRepositorio departamentoRepositorio;
    public CiudadServicio(CiudadRepositorio ciudadRepositorio, DepartamentoRepositorio departamentoRepositorio) {
        this.ciudadRepositorio = ciudadRepositorio;
        this.departamentoRepositorio = departamentoRepositorio;
    }
    //Definicion de Métodos crud para la entidad Ciudad (CRUD)
    //Metodo para Crear Registros
    public Ciudad crearCiudad(Ciudad ciudad, Integer idDepartamento){
        Departamento departamento = departamentoRepositorio.findById(idDepartamento)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con el ID: " + idDepartamento));
        if (ciudad.getCiudad() == null || ciudad.getCiudad().isEmpty()) {
            throw new RuntimeException("El nombre de la ciudad es obligatorio.");
        }
        ciudad.setDepartamento(departamento);
        return ciudadRepositorio.save(ciudad);
    }
    //Metodo para Consultar todos los Registros
    public List<Ciudad> consultarCiudades(){
        return ciudadRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Ciudad consultarCiudadById(Integer id){
        return ciudadRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada con el ID: " + id));
    }
    //Metodo para Actualizar un Registro por Id
    public Ciudad actualizarCiudad(Integer id, Ciudad nuevaCiudad, Integer idDepartamento){
        Ciudad ciudadActual = consultarCiudadById(id);
        if (nuevaCiudad.getCiudad() != null && !nuevaCiudad.getCiudad().isEmpty()) {
            ciudadActual.setCiudad(nuevaCiudad.getCiudad());
        }
        if (idDepartamento != null) {
            Departamento departamento = departamentoRepositorio.findById(idDepartamento)
                    .orElseThrow(() -> new RuntimeException("Departamento no encontrado con el ID: " + idDepartamento));
            ciudadActual.setDepartamento(departamento);
        }
        return ciudadRepositorio.save(ciudadActual);
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarCiudad(Integer id){
        Ciudad ciudadEliminar = consultarCiudadById(id);
        ciudadRepositorio.deleteById(id);
        return "Ciudad eliminada con éxito";
    }
    // Metodo para Consultar Ciudades por ID de Departamento
    public List<Ciudad> consultarCiudadesPorDepartamento(Integer idDepartamento) {
        Departamento departamento = departamentoRepositorio.findById(idDepartamento)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con el ID: " + idDepartamento));
        return ciudadRepositorio.findByDepartamento(departamento);
    }


}
