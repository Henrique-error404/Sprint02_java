package com.gestaoestabelecimentos.service;

import com.gestaoestabelecimentos.model.dto.PedidoDTO;
import com.gestaoestabelecimentos.model.entity.*;
import com.gestaoestabelecimentos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    // CREATE
    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        // Valida usuário
        Usuario usuario = usuarioRepository.findById(pedidoDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + pedidoDTO.getIdUsuario()));

        // Valida estabelecimento
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedidoDTO.getIdEstabelecimento())
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado com ID: " + pedidoDTO.getIdEstabelecimento()));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstabelecimento(estabelecimento);
        pedido.setVlTotal(BigDecimal.ZERO); // Será calculado com os itens
        pedido.setStPedido(0); // Não pronto

        return pedidoRepository.save(pedido);
    }

    // READ - Todos
    @Transactional(readOnly = true)
    public List<Pedido> listarTodosPedidos() {
        return pedidoRepository.findAll();
    }

    // READ - Por ID
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    // READ - Por Usuário
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorUsuario(Long idUsuario) {
        return pedidoRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // READ - Por Estabelecimento
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorEstabelecimento(Long idEstabelecimento) {
        return pedidoRepository.findByEstabelecimentoIdEstabelecimento(idEstabelecimento);
    }

    // READ - Por Status
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorStatus(Integer status) {
        return pedidoRepository.findByStPedido(status);
    }

    // UPDATE
    public Pedido atualizarPedido(Long id, PedidoDTO pedidoDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        // Atualiza status se fornecido
        if (pedidoDTO.getStPedido() != null) {
            pedidoExistente.setStPedido(pedidoDTO.getStPedido());
        }

        // Atualiza valor total se fornecido
        if (pedidoDTO.getVlTotal() != null) {
            pedidoExistente.setVlTotal(pedidoDTO.getVlTotal());
        }

        return pedidoRepository.save(pedidoExistente);
    }

    // DELETE
    public void deletarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado com ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    // Marcar como pronto
    public Pedido marcarComoPronto(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        pedido.marcarComoPronto();
        return pedidoRepository.save(pedido);
    }

    // Adicionar item ao pedido
    public ItemPedido adicionarItemAoPedido(Long idPedido, Long idProduto, Integer quantidade) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + idPedido));

        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + idProduto));

        // Verifica estoque
        if (!produto.temEstoqueSuficiente(quantidade)) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNmProduto());
        }

        ItemPedido item = new ItemPedido(quantidade, pedido, produto);
        ItemPedido itemSalvo = itemPedidoRepository.save(item);

        // Atualiza estoque
        produto.setQtProduto(produto.getQtProduto() - quantidade);
        produtoRepository.save(produto);

        // Atualiza valor total do pedido
        BigDecimal subtotal = item.calcularSubtotal();
        pedido.setVlTotal(pedido.getVlTotal().add(subtotal));
        pedidoRepository.save(pedido);

        return itemSalvo;
    }
}