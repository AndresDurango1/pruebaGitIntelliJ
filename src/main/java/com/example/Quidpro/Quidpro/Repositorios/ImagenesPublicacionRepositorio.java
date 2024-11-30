package com.example.Quidpro.Quidpro.Repositorios;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImagenesPublicacionRepositorio extends JpaRepository<ImagenesPublicacion, Integer> {
    List<ImagenesPublicacion> findByPublicacionId(int publicacionId);
}
