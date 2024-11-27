package com.example.Quidpro.Quidpro.Repositorios;
import com.example.Quidpro.Quidpro.Entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepositorio extends JpaRepository<Comentario, Integer> {
}
