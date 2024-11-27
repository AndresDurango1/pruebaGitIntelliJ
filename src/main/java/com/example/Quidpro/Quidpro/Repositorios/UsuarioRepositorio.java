package com.example.Quidpro.Quidpro.Repositorios;
import com.example.Quidpro.Quidpro.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
}
