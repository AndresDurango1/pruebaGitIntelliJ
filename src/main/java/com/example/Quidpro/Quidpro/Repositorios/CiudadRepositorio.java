package com.example.Quidpro.Quidpro.Repositorios;
import com.example.Quidpro.Quidpro.Entidades.Ciudad;
import com.example.Quidpro.Quidpro.Entidades.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CiudadRepositorio extends JpaRepository<Ciudad, Integer> {
    List<Ciudad> findByDepartamento(Departamento departamento);
}
