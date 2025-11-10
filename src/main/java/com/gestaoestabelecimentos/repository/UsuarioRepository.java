package com.gestaoestabelecimentos.repository;

import com.gestaoestabelecimentos.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuário por email
    Optional<Usuario> findByEmail(String email);

    // Verificar se email existe
    boolean existsByEmail(String email);

    // Buscar ID máximo para sequences
    @Query("SELECT COALESCE(MAX(u.idUsuario), 0) FROM Usuario u")
    Long findMaxId();

    // Buscar usuários por nome (case insensitive)
    List<Usuario> findByNmUsuarioContainingIgnoreCase(String nome);

    // Buscar usuários por cidade (usando LIKE no endereço)
    @Query("SELECT u FROM Usuario u WHERE u.enderecoUsuario LIKE %:cidade%")
    List<Usuario> findByCidade(@Param("cidade") String cidade);

    // Verificar se email existe em outro usuário (para update)
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email AND u.idUsuario != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    // Buscar usuários com pedidos recentes (últimos 7 dias)
    //@Query("SELECT DISTINCT u FROM Usuario u JOIN u.pedidos p WHERE p.dtHrPedido >= CURRENT_DATE - 7")
    //List<Usuario> findUsuariosComPedidosRecentes();

    // Buscar usuários ordenados por nome
    List<Usuario> findAllByOrderByNmUsuarioAsc();

    // Buscar usuários por telefone
    List<Usuario> findByTelUsuario(String telefone);

    // Contar total de usuários
    @Query("SELECT COUNT(u) FROM Usuario u")
    Long countTotalUsuarios();

    // Buscar usuários com cupons ativos
    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.cupons c WHERE c.utilizado = false AND (c.dtValidade IS NULL OR c.dtValidade > CURRENT_TIMESTAMP)")
    List<Usuario> findUsuariosComCuponsAtivos();
}