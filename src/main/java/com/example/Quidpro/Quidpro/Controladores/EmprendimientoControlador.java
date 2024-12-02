package com.example.Quidpro.Quidpro.Controladores;
import com.example.Quidpro.Quidpro.Entidades.Emprendimiento;
import com.example.Quidpro.Quidpro.Entidades.Sector;
import com.example.Quidpro.Quidpro.Entidades.Usuario;
import com.example.Quidpro.Quidpro.Excepciones.ResourceNotFoundException;
import com.example.Quidpro.Quidpro.Servicios.EmprendimientoServicio;
import com.example.Quidpro.Quidpro.Servicios.SectorServicio;
import com.example.Quidpro.Quidpro.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/emprendimientos")
@CrossOrigin(origins = "http://localhost:4200")
public class EmprendimientoControlador {
    @Autowired
    private final EmprendimientoServicio emprendimientoServicio;
    private final UsuarioServicio usuarioServicio;
    private final SectorServicio sectorServicio;
    public EmprendimientoControlador(EmprendimientoServicio emprendimientoServicio, UsuarioServicio usuarioServicio, SectorServicio sectorServicio){
        this.emprendimientoServicio = emprendimientoServicio;
        this.usuarioServicio = usuarioServicio;
        this.sectorServicio = sectorServicio;
    }
    //Metodos auxiliares para manejar excepciones
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<Emprendimiento> crearEmprendimiento(
            @RequestParam(name = "nombre", required = true) String nombre,
            @RequestParam(name = "descripcion", required = true) String descripcion,
            @RequestParam(name = "fecha_creacion", required = false) LocalDate fecha_creacion,
            @RequestParam(name = "fecha_actualizacion", required = false) LocalDate fecha_actualizacion,
            @RequestParam List<Integer> idUsuarios,
            //@RequestParam Integer idDepartamento,
            @RequestParam Integer idCiudad,
            @RequestParam List<Integer> idSectores,
            @RequestParam Integer idEstado){
        if(fecha_creacion == null){
            fecha_creacion = LocalDate.now();
        }
        if(fecha_actualizacion == null){
            fecha_actualizacion = LocalDate.now();
        }
        Emprendimiento emprendimiento = new Emprendimiento();
        emprendimiento.setNombre(nombre);
        emprendimiento.setDescripcion(descripcion);
        emprendimiento.setFecha_creacion(fecha_creacion);
        emprendimiento.setFecha_actualizacion(fecha_actualizacion);
        Emprendimiento nuevoEmprendimiento = emprendimientoServicio.crearEmprendimiento(emprendimiento, idUsuarios, idCiudad, idSectores, idEstado);
        return new ResponseEntity<Emprendimiento>(nuevoEmprendimiento, HttpStatus.CREATED);
    }
    //Metodo para actualizar un emprendimiento
    @PutMapping("/{id}")
    public ResponseEntity<Emprendimiento> actualizarEmprendimiento(
            @PathVariable Integer id,
            @RequestParam(name = "nombre", required = true) String nombre,
            @RequestParam(name = "descripcion", required = true) String descripcion,
            //@RequestParam(name = "fecha_creacion", required = false) LocalDate fecha_creacion,
            @RequestParam(name = "fecha_actualizacion", required = false) LocalDate fecha_actualizacion,
            @RequestParam List<Integer> idUsuarios,
            //@RequestParam Integer idDepartamento,
            @RequestParam Integer idCiudad,
            @RequestParam List<Integer> idSectores,
            @RequestParam Integer idEstado
    ){
        try {
            // 1. Validar y obtener el emprendimiento existente
            Emprendimiento emprendimientoExistente = emprendimientoServicio.consultarEmprendimientoById(id);
            if(emprendimientoExistente == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            // 2. Actualizar los datos básicos del emprendimiento
            emprendimientoExistente.setNombre(nombre);
            emprendimientoExistente.setDescripcion(descripcion);
            emprendimientoExistente.setFecha_actualizacion(fecha_actualizacion != null ? fecha_actualizacion : LocalDate.now());
            // 3. Gestionar usuarios y sectores existentes y nuevos
            //Usuarios
            Set<Usuario> usuariosExistentes = emprendimientoExistente.getUsuarios();
            Set<Usuario> usuariosFinales = new HashSet<>();
            Set<Integer> idsUsuariosActualizados = new HashSet<>();
            //Sectores
            Set<Sector> sectoresExistentes = emprendimientoExistente.getSectores();
            Set<Sector> sectoresFinales = new HashSet<>();
            Set<Integer> idsSectoresActualizados = new HashSet<>();

            if (idUsuarios != null && !idUsuarios.isEmpty()){
                for (Integer idUsuario : idUsuarios){
                    Usuario usuarioExistente = usuariosExistentes.stream()
                            .filter(usuario -> usuario.getId() == idUsuario)
                            .findFirst()
                            .orElse(null);
                    if(usuarioExistente != null){
                        // El usuario ya existe, solo lo agregamos a la lista final
                        usuariosFinales.add(usuarioExistente);
                        idsUsuariosActualizados.add(usuarioExistente.getId());
                    } else {
                        // Si el usuario no existe, lo buscamos por ID y lo agregamos
                        Usuario usuarioNuevo = usuarioServicio.consultarUsuarioById(idUsuario);
                        usuariosFinales.add(usuarioNuevo);
                    }
                }
                Iterator<Usuario> iterator = usuariosExistentes.iterator();
                List<Usuario> usuariosAEliminar = new ArrayList<>();
                while (iterator.hasNext()){
                    Usuario usuarioExistente = iterator.next();
                    if(!idsUsuariosActualizados.contains(usuarioExistente.getId()));
                    usuariosAEliminar.add(usuarioExistente);
                }
                if(!usuariosAEliminar.isEmpty()){
                    usuariosExistentes.removeAll(usuariosAEliminar);
                }
                emprendimientoExistente.setUsuarios(usuariosFinales);
            }
            //Gestion de los Sectores
            if (idSectores != null && !idSectores.isEmpty()){
                for (Integer idSector : idSectores){
                    Sector sectorExistente = sectoresExistentes.stream()
                            .filter(sector -> sector.getId() == idSector)
                            .findFirst()
                            .orElse(null);
                    if(sectorExistente != null){
                        // El sector ya existe, solo lo agregamos a la lista final
                        sectoresFinales.add(sectorExistente);
                        idsSectoresActualizados.add(sectorExistente.getId());
                    } else {
                        // Si el sector no existe, lo buscamos por ID y lo agregamos
                        Sector sectorNuevo = sectorServicio.consultarSectorById(idSector)
                                .orElseThrow(() -> new RuntimeException("Sector no encontrado con el ID: " + idSector));
                        sectoresFinales.add(sectorNuevo);
                    }
                }
                Iterator<Sector> iterator = sectoresExistentes.iterator();
                List<Sector> sectoresAEliminar = new ArrayList<>();
                while (iterator.hasNext()){
                    Sector sectorExistente = iterator.next();
                    if(!idsSectoresActualizados.contains(sectorExistente.getId()));
                    sectoresAEliminar.add(sectorExistente);
                }
                if(!sectoresAEliminar.isEmpty()){
                    sectoresExistentes.removeAll(sectoresAEliminar);
                }
                emprendimientoExistente.setSectores(sectoresFinales);
                // Imprimir usuarios con sus atributos
                System.out.println("Usuarios Finales:");
                usuariosFinales.forEach(usuario ->
                        System.out.println("ID: " + usuario.getId() + ", Nombre: " + usuario.getNombres())
                );
                // Imprimir sectores con sus atributos
                System.out.println("Sectores Finales:");
                sectoresFinales.forEach(sector ->
                        System.out.println("ID: " + sector.getId() + ", Nombre: " + sector.getSector())
                );

            }
            Emprendimiento emprendimientoActualizado = emprendimientoServicio.actualizarEmprendimiento(id, emprendimientoExistente, idUsuarios, idCiudad, idSectores, idEstado);
            return new ResponseEntity<Emprendimiento>(emprendimientoActualizado, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Metodo para eliminar un emprendimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEmprendimiento(@PathVariable Integer id){
        String mensaje = emprendimientoServicio.eliminarEmprendimiento(id);
        return ResponseEntity.ok(mensaje);
    }
}
