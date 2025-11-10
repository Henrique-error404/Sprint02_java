package com.gestaoestabelecimentos.service;

import com.gestaoestabelecimentos.model.dto.ProdutoDTO;
import com.gestaoestabelecimentos.model.entity.Estabelecimento;
import com.gestaoestabelecimentos.model.entity.Produto;
import com.gestaoestabelecimentos.repository.EstabelecimentoRepository;
import com.gestaoestabelecimentos.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public Produto criarProduto(ProdutoDTO produtoDTO) {
        // Verifica se estabelecimento existe
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(produtoDTO.getIdEstabelecimento())
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado com ID: " + produtoDTO.getIdEstabelecimento()));

        // Valida data de validade
        if (produtoDTO.getDtValidade() != null && produtoDTO.getDtValidade().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data de validade deve ser futura");
        }

        Produto produto = modelMapper.map(produtoDTO, Produto.class);
        produto.setEstabelecimento(estabelecimento);

        return produtoRepository.save(produto);
    }

    // READ - Todos
    @Transactional(readOnly = true)
    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    // READ - Por ID
    @Transactional(readOnly = true)
    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // READ - Por Estabelecimento
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorEstabelecimento(Long idEstabelecimento) {
        return produtoRepository.findByEstabelecimentoIdEstabelecimento(idEstabelecimento);
    }

    // READ - Por Nome
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorNome(String nome) {
        return produtoRepository.findByNmProdutoContainingIgnoreCase(nome);
    }

    // READ - Produtos válidos (não vencidos)
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosValidosPorEstabelecimento(Long idEstabelecimento) {
        return produtoRepository.findProdutosValidosPorEstabelecimento(idEstabelecimento);
    }

    // READ - Produtos com estoque
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosComEstoquePorEstabelecimento(Long idEstabelecimento) {
        return produtoRepository.findProdutosComEstoquePorEstabelecimento(idEstabelecimento);
    }

    // UPDATE
    public Produto atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

        // Atualiza estabelecimento se fornecido
        if (produtoDTO.getIdEstabelecimento() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(produtoDTO.getIdEstabelecimento())
                    .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado com ID: " + produtoDTO.getIdEstabelecimento()));
            produtoExistente.setEstabelecimento(estabelecimento);
        }

        // Atualiza outros campos
        if (produtoDTO.getNmProduto() != null) {
            produtoExistente.setNmProduto(produtoDTO.getNmProduto());
        }
        if (produtoDTO.getDsProduto() != null) {
            produtoExistente.setDsProduto(produtoDTO.getDsProduto());
        }
        if (produtoDTO.getVlProduto() != null) {
            produtoExistente.setVlProduto(produtoDTO.getVlProduto());
        }
        if (produtoDTO.getDtValidade() != null) {
            if (produtoDTO.getDtValidade().isBefore(LocalDate.now())) {
                throw new RuntimeException("Data de validade deve ser futura");
            }
            produtoExistente.setDtValidade(produtoDTO.getDtValidade());
        }
        if (produtoDTO.getQtProduto() != null) {
            produtoExistente.setQtProduto(produtoDTO.getQtProduto());
        }

        return produtoRepository.save(produtoExistente);
    }

    // DELETE
    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    // Atualizar estoque
    public Produto atualizarEstoque(Long id, Integer quantidade) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

        if (produto.getQtProduto() + quantidade < 0) {
            throw new RuntimeException("Estoque insuficiente");
        }

        produto.setQtProduto(produto.getQtProduto() + quantidade);
        return produtoRepository.save(produto);
    }
}