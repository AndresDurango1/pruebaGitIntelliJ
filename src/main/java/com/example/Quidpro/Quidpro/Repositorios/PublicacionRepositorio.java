package com.example.Quidpro.Quidpro.Repositorios;
import com.example.Quidpro.Quidpro.Entidades.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PublicacionRepositorio extends JpaRepository<Publicacion, Integer> {
    @Query("SELECT p FROM Publicacion p LEFT JOIN FETCH p.imagenesPublicaciones WHERE p.id = :id")
    Optional<Publicacion> findByIdWithImages(@Param("id") Integer id);

}
