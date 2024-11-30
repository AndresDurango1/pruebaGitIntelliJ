package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.*;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Excepciones.ResourceNotFoundException;
import com.example.Quidpro.Quidpro.Repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmprendimientoServicio {
    private final EmprendimientoRepositorio emprendimientoRepositorio;
    private final DepartamentoRepositorio departamentoRepositorio;
    private final CiudadRepositorio ciudadRepositorio;
    private final EstadosRepositorio estadosRepositorio;
    private final SectorRepositorio sectorRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public EmprendimientoServicio(EmprendimientoRepositorio emprendimientoRepositorio, DepartamentoRepositorio departamentoRepositorio, CiudadRepositorio ciudadRepositorio, EstadosRepositorio estadosRepositorio, SectorRepositorio sectorRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.emprendimientoRepositorio = emprendimientoRepositorio;
        this.departamentoRepositorio = departamentoRepositorio;
        this.ciudadRepositorio = ciudadRepositorio;
        this.estadosRepositorio = estadosRepositorio;
        this.sectorRepositorio = sectorRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }
    //Metodo auxiliar para validar campos
    private boolean esCampoValido(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
    //Metodo Helper para validar existencia de las entidades
    private <T> T validarExistencia(Optional<T> optional, String mensaje) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(mensaje));
    }
    //Metodo Helper para asignar las relaciones entre las entidades
    private void asignarRelaciones(Emprendimiento emprendimiento, Integer idUsuario, Integer idDepartamento, Integer idCiudad, Integer idSector, Integer idEstado){
        Departamento departamento = validarExistencia(departamentoRepositorio.findById(idDepartamento), "Departamento no encontrado");
        Ciudad ciudad = validarExistencia(ciudadRepositorio.findById(idCiudad), "Ciudad no encontrada");
        Estados estados = validarExistencia(estadosRepositorio.findById(idEstado), "Estado no encontrado");
        Sector sector = validarExistencia(sectorRepositorio.findById(idSector), "Sector no encontrado");
        Usuario usuario =  validarExistencia(usuarioRepositorio.findById(idUsuario), "Usuario no encontrado");
        //Asignacion de las entidades al emprendimiento
        emprendimiento.setCiudad(ciudad);
        emprendimiento.setEstado(estados);
        emprendimiento.setUsuarios(Set.of(usuario));
        emprendimiento.setSectores(Set.of(sector));
    }
    //DEFINICION DE METODOS CRUD PARA LA CLASE EMPRENDIMIENTO
    //Metodo para Crear Registros
    public Emprendimiento crearEmprendimiento(Emprendimiento emprendimiento, Integer idUsuario, Integer idDepartamento, Integer idCiudad, Integer idSector, Integer idEstado){
        // Validacion de  los otros campos obligatorios de Emprendimiento
        if (!esCampoValido(emprendimiento.getNombre())) {
            throw new InvalidDataException("El nombre del emprendimiento es obligatorio.");
        }
        if (!esCampoValido(emprendimiento.getDescripcion())) {
            throw new InvalidDataException("La descripción del emprendimiento es obligatoria.");
        }
        if (emprendimiento.getFecha_creacion() == null) {
            throw new InvalidDataException("La fecha de creación es obligatoria.");
        }
        asignarRelaciones(emprendimiento, idUsuario, idDepartamento, idCiudad, idSector, idEstado);
        return emprendimientoRepositorio.save(emprendimiento);
    }
    //Metodo para Consultar todos los Registros
    public List<Emprendimiento> consultarEmprendimientos(){
        return emprendimientoRepositorio.findAll();
    }
    //Metodo para Consultar un Registro por id
    public Emprendimiento consultarEmprendimientoById(Integer id){
        return emprendimientoRepositorio.findById(id).orElseThrow(() -> new InvalidDataException("No se encontró un emprendimiento con el ID proporcionado: " + id));
    }
    //Metodo para Actualizar un Registro por Id
    public Emprendimiento actualizarEmprendimiento(Integer id, Emprendimiento nuevoEmprendimiento, Integer idUsuario, Integer idDepartamento, Integer idCiudad, Integer idSector, Integer idEstado){
        Emprendimiento emprendimientoActualizado = consultarEmprendimientoById(id);
        //Validacion de los campos
        if (esCampoValido(nuevoEmprendimiento.getNombre())) {
            emprendimientoActualizado.setNombre(nuevoEmprendimiento.getNombre());
        }
        if (esCampoValido(nuevoEmprendimiento.getDescripcion())) {
            emprendimientoActualizado.setDescripcion(nuevoEmprendimiento.getDescripcion());
        }
        if (nuevoEmprendimiento.getFecha_creacion() != null) {
            emprendimientoActualizado.setFecha_creacion(nuevoEmprendimiento.getFecha_creacion());
        }
        if (nuevoEmprendimiento.getEstado() != null) {
            emprendimientoActualizado.setEstado(nuevoEmprendimiento.getEstado());
        }
        if (nuevoEmprendimiento.getCiudad() != null) {
            emprendimientoActualizado.setCiudad(nuevoEmprendimiento.getCiudad());
        }
        if (nuevoEmprendimiento.getSectores() != null && !nuevoEmprendimiento.getSectores().isEmpty()) {
            emprendimientoActualizado.setSectores(nuevoEmprendimiento.getSectores());
        }
        if (nuevoEmprendimiento.getUsuarios() != null && !nuevoEmprendimiento.getUsuarios().isEmpty()) {
            emprendimientoActualizado.setUsuarios(nuevoEmprendimiento.getUsuarios());
        }
        if(idUsuario !=null || idDepartamento != null || idCiudad != null || idSector != null || idEstado != null){
            asignarRelaciones(emprendimientoActualizado, idUsuario, idDepartamento, idCiudad, idSector, idEstado);
        }
        return emprendimientoRepositorio.save(emprendimientoActualizado);
    }
    //Metodo para Eliminar un Registro por Id
    public String eliminarEmprendimiento(Integer id){
        Emprendimiento emprendimientoEliminar = consultarEmprendimientoById(id);
        emprendimientoRepositorio.delete(emprendimientoEliminar);
        return "Emprendimiento eliminado con éxito";
    }
}
