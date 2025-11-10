package com.gestaoestabelecimentos.repository;

import com.gestaoestabelecimentos.model.entity.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

    List<Cupom> findByUsuarioIdUsuario(Long idUsuario);

    List<Cupom> findByUtilizado(Boolean utilizado);

    @Query("SELECT COALESCE(MAX(c.idCupom), 0) FROM Cupom c")
    Long findMaxId();

    @Query("SELECT c FROM Cupom c WHERE c.usuario.idUsuario = :idUsuario AND c.utilizado = false AND (c.dtValidade IS NULL OR c.dtValidade > CURRENT_TIMESTAMP)")
    List<Cupom> findCuponsValidosPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT c FROM Cupom c WHERE c.dtValidade < :data AND c.utilizado = false")
    List<Cupom> findCuponsExpirados(@Param("data") LocalDateTime data);

    @Query("SELECT SUM(c.vlCupom) FROM Cupom c WHERE c.usuario.idUsuario = :idUsuario AND c.utilizado = false")
    BigDecimal sumCuponsDisponiveisPorUsuario(@Param("idUsuario") Long idUsuario);
}