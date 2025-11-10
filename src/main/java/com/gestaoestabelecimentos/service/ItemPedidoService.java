package com.gestaoestabelecimentos.service;

import com.gestaoestabelecimentos.model.dto.ItemPedidoDTO;
import com.gestaoestabelecimentos.model.entity.ItemPedido;
import com.gestaoestabelecimentos.model.entity.Pedido;
import com.gestaoestabelecimentos.model.entity.Produto;
import com.gestaoestabelecimentos.repository.ItemPedidoRepository;
import com.gestaoestabelecimentos.repository.PedidoRepository;
import com.gestaoestabelecimentos.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public ItemPedido criarItemPedido(ItemPedidoDTO itemPedidoDTO) {
        // Valida pedido
        Pedido pedido = pedidoRepository.findById(itemPedidoDTO.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + itemPedidoDTO.getIdPedido()));

        // Valida produto
        Produto produto = produtoRepository.findById(itemPedidoDTO.getIdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemPedidoDTO.getIdProduto()));

        // Verifica estoque
        if (!produto.temEstoqueSuficiente(itemPedidoDTO.getQtItem())) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNmProduto());
        }

        ItemPedido itemPedido = modelMapper.map(itemPedidoDTO, ItemPedido.class);
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);

        ItemPedido itemSalvo = itemPedidoRepository.save(itemPedido);

        // Atualiza estoque do produto
        produto.setQtProduto(produto.getQtProduto() - itemPedidoDTO.getQtItem());
        produtoRepository.save(produto);

        // Atualiza valor total do pedido
        atualizarValorTotalPedido(pedido.getIdPedido());

        return itemSalvo;
    }

    // READ - Todos
    @Transactional(readOnly = true)
    public List<ItemPedido> listarTodosItensPedido() {
        return itemPedidoRepository.findAll();
    }

    // READ - Por ID
    @Transactional(readOnly = true)
    public Optional<ItemPedido> buscarItemPedidoPorId(Long id) {
        return itemPedidoRepository.findById(id);
    }

    // READ - Por Pedido
    @Transactional(readOnly = true)
    public List<ItemPedido> buscarItensPorPedido(Long idPedido) {
        return itemPedidoRepository.findByPedidoIdPedido(idPedido);
    }

    // READ - Por Produto
    @Transactional(readOnly = true)
    public List<ItemPedido> buscarItensPorProduto(Long idProduto) {
        return itemPedidoRepository.findByProdutoIdProduto(idProduto);
    }

    // UPDATE
    public ItemPedido atualizarItemPedido(Long id, ItemPedidoDTO itemPedidoDTO) {
        ItemPedido itemExistente = itemPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item do pedido não encontrado com ID: " + id));

        // Guarda quantidade antiga para ajuste de estoque
        Integer quantidadeAntiga = itemExistente.getQtItem();

        // Atualiza quantidade se fornecida
        if (itemPedidoDTO.getQtItem() != null) {
            // Valida novo estoque
            Produto produto = itemExistente.getProduto();
            int diferenca = itemPedidoDTO.getQtItem() - quantidadeAntiga;

            if (produto.getQtProduto() < diferenca) {
                throw new RuntimeException("Estoque insuficiente para atualização");
            }

            itemExistente.setQtItem(itemPedidoDTO.getQtItem());

            // Atualiza estoque
            produto.setQtProduto(produto.getQtProduto() - diferenca);
            produtoRepository.save(produto);
        }

        ItemPedido itemAtualizado = itemPedidoRepository.save(itemExistente);

        // Atualiza valor total do pedido
        atualizarValorTotalPedido(itemExistente.getPedido().getIdPedido());

        return itemAtualizado;
    }

    // DELETE
    public void deletarItemPedido(Long id) {
        ItemPedido item = itemPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item do pedido não encontrado com ID: " + id));

        // Restaura estoque
        Produto produto = item.getProduto();
        produto.setQtProduto(produto.getQtProduto() + item.getQtItem());
        produtoRepository.save(produto);

        Long idPedido = item.getPedido().getIdPedido();

        itemPedidoRepository.deleteById(id);

        // Atualiza valor total do pedido
        atualizarValorTotalPedido(idPedido);
    }

    // Método auxiliar para atualizar valor total do pedido
    private void atualizarValorTotalPedido(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        List<ItemPedido> itens = itemPedidoRepository.findByPedidoIdPedido(idPedido);

        BigDecimal valorTotal = itens.stream()
                .map(ItemPedido::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setVlTotal(valorTotal);
        pedidoRepository.save(pedido);
    }

    // Calcular total vendido por produto
    @Transactional(readOnly = true)
    public Long calcularTotalVendidoPorProduto(Long idProduto) {
        return itemPedidoRepository.sumQuantidadeVendidaPorProduto(idProduto);
    }
}