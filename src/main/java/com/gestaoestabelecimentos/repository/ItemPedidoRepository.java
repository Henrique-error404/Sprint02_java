package com.gestaoestabelecimentos.repository;

import com.gestaoestabelecimentos.model.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoIdPedido(Long idPedido);

    List<ItemPedido> findByProdutoIdProduto(Long idProduto);

    @Query("SELECT COALESCE(MAX(i.idItem), 0) FROM ItemPedido i")
    Long findMaxId();

    @Query("SELECT i FROM ItemPedido i WHERE i.pedido.idPedido = :idPedido AND i.produto.idProduto = :idProduto")
    Optional<ItemPedido> findByPedidoAndProduto(@Param("idPedido") Long idPedido, @Param("idProduto") Long idProduto);

    @Query("SELECT SUM(i.qtItem) FROM ItemPedido i WHERE i.produto.idProduto = :idProduto")
    Long sumQuantidadeVendidaPorProduto(@Param("idProduto") Long idProduto);
}