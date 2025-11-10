package com.gestaoestabelecimentos.service;

import com.gestaoestabelecimentos.model.dto.CupomDTO;
import com.gestaoestabelecimentos.model.entity.Cupom;
import com.gestaoestabelecimentos.model.entity.Usuario;
import com.gestaoestabelecimentos.repository.CupomRepository;
import com.gestaoestabelecimentos.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public Cupom criarCupom(CupomDTO cupomDTO) {
        // Valida usuário
        Usuario usuario = usuarioRepository.findById(cupomDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + cupomDTO.getIdUsuario()));

        Cupom cupom = modelMapper.map(cupomDTO, Cupom.class);
        cupom.setUsuario(usuario);

        // Define data de criação se não fornecida
        if (cupom.getDtCriacao() == null) {
            cupom.setDtCriacao(LocalDateTime.now());
        }

        // Define validade padrão (30 dias) se não fornecida
        if (cupom.getDtValidade() == null) {
            cupom.setDtValidade(LocalDateTime.now().plusDays(30));
        }

        // Garante que não está utilizado
        cupom.setUtilizado(false);

        return cupomRepository.save(cupom);
    }

    // READ - Todos
    @Transactional(readOnly = true)
    public List<Cupom> listarTodosCupons() {
        return cupomRepository.findAll();
    }

    // READ - Por ID
    @Transactional(readOnly = true)
    public Optional<Cupom> buscarCupomPorId(Long id) {
        return cupomRepository.findById(id);
    }

    // READ - Por Usuário
    @Transactional(readOnly = true)
    public List<Cupom> buscarCuponsPorUsuario(Long idUsuario) {
        return cupomRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // READ - Por Status de Utilização
    @Transactional(readOnly = true)
    public List<Cupom> buscarCuponsPorUtilizacao(Boolean utilizado) {
        return cupomRepository.findByUtilizado(utilizado);
    }

    // READ - Cupons válidos por usuário (não utilizados e não expirados)
    @Transactional(readOnly = true)
    public List<Cupom> buscarCuponsValidosPorUsuario(Long idUsuario) {
        return cupomRepository.findCuponsValidosPorUsuario(idUsuario);
    }

    // UPDATE
    public Cupom atualizarCupom(Long id, CupomDTO cupomDTO) {
        Cupom cupomExistente = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado com ID: " + id));

        // Não permite alterar cupom já utilizado
        if (cupomExistente.getUtilizado()) {
            throw new RuntimeException("Não é possível alterar um cupom já utilizado");
        }

        // Atualiza campos se fornecidos
        if (cupomDTO.getVlCupom() != null) {
            cupomExistente.setVlCupom(cupomDTO.getVlCupom());
        }
        if (cupomDTO.getDtValidade() != null) {
            cupomExistente.setDtValidade(cupomDTO.getDtValidade());
        }
        if (cupomDTO.getUtilizado() != null) {
            cupomExistente.setUtilizado(cupomDTO.getUtilizado());
        }

        return cupomRepository.save(cupomExistente);
    }

    // DELETE
    public void deletarCupom(Long id) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado com ID: " + id));

        // Não permite deletar cupom utilizado
        if (cupom.getUtilizado()) {
            throw new RuntimeException("Não é possível deletar um cupom já utilizado");
        }

        cupomRepository.deleteById(id);
    }

    // Utilizar cupom
    public Cupom utilizarCupom(Long id) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado com ID: " + id));

        if (!cupom.isValido()) {
            throw new RuntimeException("Cupom não é válido para utilização");
        }

        cupom.utilizar();
        return cupomRepository.save(cupom);
    }

    // Verificar validade do cupom
    @Transactional(readOnly = true)
    public boolean cupomEhValido(Long id) {
        return cupomRepository.findById(id)
                .map(Cupom::isValido)
                .orElse(false);
    }

    // Buscar cupons expirados
    @Transactional(readOnly = true)
    public List<Cupom> buscarCuponsExpirados() {
        return cupomRepository.findCuponsExpirados(LocalDateTime.now());
    }

    // Calcular total de cupons disponíveis por usuário
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalCuponsDisponiveisPorUsuario(Long idUsuario) {
        return cupomRepository.sumCuponsDisponiveisPorUsuario(idUsuario);
    }

    // Gerar cupom de bonificação
    public Cupom gerarCupomBonificacao(Long idUsuario, BigDecimal valor) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + idUsuario));

        CupomDTO cupomDTO = new CupomDTO();
        cupomDTO.setVlCupom(valor);
        cupomDTO.setIdUsuario(idUsuario);
        cupomDTO.setDtValidade(LocalDateTime.now().plusDays(60)); // 60 dias de validade

        return criarCupom(cupomDTO);
    }
}