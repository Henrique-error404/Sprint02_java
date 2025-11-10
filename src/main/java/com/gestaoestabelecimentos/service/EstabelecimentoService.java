package com.gestaoestabelecimentos.service;

import com.gestaoestabelecimentos.exception.DuplicateResourceException;
import com.gestaoestabelecimentos.model.dto.EstabelecimentoDTO;
import com.gestaoestabelecimentos.model.entity.Estabelecimento;
import com.gestaoestabelecimentos.repository.EstabelecimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstabelecimentoService {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public Estabelecimento criarEstabelecimento(EstabelecimentoDTO estabelecimentoDTO) {
        // Verifica se CNPJ já existe
        if (estabelecimentoRepository.existsByCnpj(estabelecimentoDTO.getCnpj())) {
            throw new DuplicateResourceException("Estabelecimento", "CNPJ", estabelecimentoDTO.getCnpj());
        }

        Estabelecimento estabelecimento = modelMapper.map(estabelecimentoDTO, Estabelecimento.class);
        return estabelecimentoRepository.save(estabelecimento);
    }

    // READ - Todos
    @Transactional(readOnly = true)
    public List<Estabelecimento> listarTodosEstabelecimentos() {
        return estabelecimentoRepository.findAll();
    }

    // READ - Por ID
    @Transactional(readOnly = true)
    public Optional<Estabelecimento> buscarEstabelecimentoPorId(Long id) {
        return estabelecimentoRepository.findById(id);
    }

    // READ - Por CNPJ
    @Transactional(readOnly = true)
    public Optional<Estabelecimento> buscarEstabelecimentoPorCnpj(String cnpj) {
        return estabelecimentoRepository.findByCnpj(cnpj);
    }

    // READ - Por Tipo
    @Transactional(readOnly = true)
    public List<Estabelecimento> buscarEstabelecimentosPorTipo(String tipo) {
        return estabelecimentoRepository.findByTpEstabelecimento(tipo);
    }

    // READ - Por Cidade
    @Transactional(readOnly = true)
    public List<Estabelecimento> buscarEstabelecimentosPorCidade(String cidade) {
        return estabelecimentoRepository.findByCidade(cidade);
    }

    // UPDATE
    public Estabelecimento atualizarEstabelecimento(Long id, EstabelecimentoDTO estabelecimentoDTO) {
        Estabelecimento estabelecimentoExistente = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado com ID: " + id));

        // Verifica se o CNPJ já existe em outro estabelecimento
        if (estabelecimentoDTO.getCnpj() != null &&
                !estabelecimentoDTO.getCnpj().equals(estabelecimentoExistente.getCnpj()) &&
                estabelecimentoRepository.existsByCnpjAndIdNot(estabelecimentoDTO.getCnpj(), id)) {
            throw new RuntimeException("CNPJ já cadastrado em outro estabelecimento: " + estabelecimentoDTO.getCnpj());
        }

        // Atualiza apenas os campos que foram fornecidos no DTO
        if (estabelecimentoDTO.getNmEstabelecimento() != null) {
            estabelecimentoExistente.setNmEstabelecimento(estabelecimentoDTO.getNmEstabelecimento());
        }
        if (estabelecimentoDTO.getCnpj() != null) {
            estabelecimentoExistente.setCnpj(estabelecimentoDTO.getCnpj());
        }
        if (estabelecimentoDTO.getEnderecoEstabelecimento() != null) {
            estabelecimentoExistente.setEnderecoEstabelecimento(estabelecimentoDTO.getEnderecoEstabelecimento());
        }
        if (estabelecimentoDTO.getTelEstabelecimento() != null) {
            estabelecimentoExistente.setTelEstabelecimento(estabelecimentoDTO.getTelEstabelecimento());
        }
        if (estabelecimentoDTO.getTpEstabelecimento() != null) {
            estabelecimentoExistente.setTpEstabelecimento(estabelecimentoDTO.getTpEstabelecimento());
        }

        return estabelecimentoRepository.save(estabelecimentoExistente);
    }

    // DELETE
    public void deletarEstabelecimento(Long id) {
        if (!estabelecimentoRepository.existsById(id)) {
            throw new RuntimeException("Estabelecimento não encontrado com ID: " + id);
        }
        estabelecimentoRepository.deleteById(id);
    }

    // EXISTS
    public boolean existeEstabelecimento(Long id) {
        return estabelecimentoRepository.existsById(id);
    }

    // Buscar por nome
    @Transactional(readOnly = true)
    public List<Estabelecimento> buscarEstabelecimentosPorNome(String nome) {
        return estabelecimentoRepository.findByNmEstabelecimentoContainingIgnoreCase(nome);
    }

    // Buscar com estoque
    @Transactional(readOnly = true)
    public List<Estabelecimento> buscarEstabelecimentosComEstoque() {
        return estabelecimentoRepository.findEstabelecimentosComEstoque();
    }
}