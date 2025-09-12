package com.crmshot.repository;

import com.crmshot.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Usuario> findByAtivoTrue();
    
    List<Usuario> findByPerfil(Usuario.PerfilUsuario perfil);
    
    @Query("SELECT u FROM Usuario u WHERE u.ativo = true AND u.perfil = :perfil")
    List<Usuario> findVendedoresAtivos(@Param("perfil") Usuario.PerfilUsuario perfil);
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ativo = true")
    Long countUsuariosAtivos();
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ativo = true AND u.perfil = :perfil")
    Long countUsuariosAtivosPorPerfil(@Param("perfil") Usuario.PerfilUsuario perfil);
}
