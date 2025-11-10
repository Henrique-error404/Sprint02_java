package com.gestaoestabelecimentos.service;

import com.gestaoestabelecimentos.model.dto.PagamentoDTO;
import com.gestaoestabelecimentos.model.entity.Pagamento;
import com.gestaoestabelecimentos.model.entity.Pedido;
import com.gestaoestabelecimentos.repository.PagamentoRepository;
import com.gestaoestabelecimentos.repository.PedidoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public Pagamento criarPagamento(PagamentoDTO pagamentoDTO) {
        // Valida pedido
        Pedido pedido = pedidoRepository.findById(pagamentoDTO.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + pagamentoDTO.getIdPedido()));

        // Verifica se já existe pagamento para este pedido
        if (pagamentoRepository.findByPedidoIdPedido(pedido.getIdPedido()).isPresent()) {
            throw new RuntimeException("Já existe um pagamento para este pedido");
        }

        // Verifica se valor do pagamento confere com o pedido
        if (pagamentoDTO.getVlPagamento().compareTo(pedido.getVlTotal()) != 0) {
            throw new RuntimeException("Valor do pagamento não confere com o valor do pedido");
        }

        Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
        pagamento.setPedido(pedido);

        return pagamentoRepository.save(pagamento);
    }

    // READ - Todos
    @Transactional(readOnly = true)
    public List<Pagamento> listarTodosPagamentos() {
        return pagamentoRepository.findAll();
    }

    // READ - Por ID
    @Transactional(readOnly = true)
    public Optional<Pagamento> buscarPagamentoPorId(Long id) {
        return pagamentoRepository.findById(id);
    }

    // READ - Por Pedido
    @Transactional(readOnly = true)
    public Optional<Pagamento> buscarPagamentoPorPedido(Long idPedido) {
        return pagamentoRepository.findByPedidoIdPedido(idPedido);
    }

    // READ - Por Status
    @Transactional(readOnly = true)
    public List<Pagamento> buscarPagamentosPorStatus(Integer status) {
        return pagamentoRepository.findByStPagamento(status);
    }

    // READ - Por Forma de Pagamento
    @Transactional(readOnly = true)
    public List<Pagamento> buscarPagamentosPorForma(String formaPagamento) {
        return pagamentoRepository.findByFormaPagamento(formaPagamento);
    }

    // UPDATE
    public Pagamento atualizarPagamento(Long id, PagamentoDTO pagamentoDTO) {
        Pagamento pagamentoExistente = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));

        // Atualiza campos se fornecidos
        if (pagamentoDTO.getFormaPagamento() != null) {
            pagamentoExistente.setFormaPagamento(pagamentoDTO.getFormaPagamento());
        }
        if (pagamentoDTO.getVlPagamento() != null) {
            // Verifica se novo valor confere com o pedido
            if (pagamentoDTO.getVlPagamento().compareTo(pagamentoExistente.getPedido().getVlTotal()) != 0) {
                throw new RuntimeException("Valor do pagamento não confere com o valor do pedido");
            }
            pagamentoExistente.setVlPagamento(pagamentoDTO.getVlPagamento());
        }
        if (pagamentoDTO.getStPagamento() != null) {
            pagamentoExistente.setStPagamento(pagamentoDTO.getStPagamento());
        }

        return pagamentoRepository.save(pagamentoExistente);
    }

    // DELETE
    public void deletarPagamento(Long id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new RuntimeException("Pagamento não encontrado com ID: " + id);
        }
        pagamentoRepository.deleteById(id);
    }

    // Marcar como pago
    public Pagamento marcarComoPago(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + id));

        pagamento.marcarComoPago();
        return pagamentoRepository.save(pagamento);
    }

    // Verificar se pedido foi pago
    @Transactional(readOnly = true)
    public boolean pedidoFoiPago(Long idPedido) {
        return pagamentoRepository.findByPedidoIdPedido(idPedido)
                .map(Pagamento::isPago)
                .orElse(false);
    }

    // Buscar pagamentos por usuário
    @Transactional(readOnly = true)
    public List<Pagamento> buscarPagamentosPorUsuario(Long idUsuario) {
        return pagamentoRepository.findByUsuarioId(idUsuario);
    }

    // Estatísticas
    @Transactional(readOnly = true)
    public Long contarPagamentosEfetuados() {
        return pagamentoRepository.countPagamentosEfetuados();
    }
}