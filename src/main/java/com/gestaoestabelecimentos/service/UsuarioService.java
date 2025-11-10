package com.gestaoestabelecimentos.service;

import com.gestaoestabelecimentos.model.dto.UsuarioDTO;
import com.gestaoestabelecimentos.model.entity.Usuario;
import com.gestaoestabelecimentos.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {
        // Verifica se email já existe
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + usuarioDTO.getEmail());
        }

        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        return usuarioRepository.save(usuario);
    }

    // READ - Todos
    @Transactional(readOnly = true)
    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    // READ - Por ID
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // READ - Por Email
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // READ - Por Nome
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosPorNome(String nome) {
        return usuarioRepository.findByNmUsuarioContainingIgnoreCase(nome);
    }

    // UPDATE
    public Usuario atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        // Verifica se o email já existe em outro usuário
        if (usuarioDTO.getEmail() != null &&
                !usuarioDTO.getEmail().equals(usuarioExistente.getEmail()) &&
                usuarioRepository.existsByEmailAndIdNot(usuarioDTO.getEmail(), id)) {
            throw new RuntimeException("Email já cadastrado em outro usuário: " + usuarioDTO.getEmail());
        }

        // Atualiza apenas os campos que foram fornecidos no DTO
        if (usuarioDTO.getNmUsuario() != null) {
            usuarioExistente.setNmUsuario(usuarioDTO.getNmUsuario());
        }
        if (usuarioDTO.getEmail() != null) {
            usuarioExistente.setEmail(usuarioDTO.getEmail());
        }
        if (usuarioDTO.getSenha() != null) {
            usuarioExistente.setSenha(usuarioDTO.getSenha());
        }
        if (usuarioDTO.getEnderecoUsuario() != null) {
            usuarioExistente.setEnderecoUsuario(usuarioDTO.getEnderecoUsuario());
        }
        if (usuarioDTO.getTelUsuario() != null) {
            usuarioExistente.setTelUsuario(usuarioDTO.getTelUsuario());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // DELETE
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // EXISTS
    public boolean existeUsuario(Long id) {
        return usuarioRepository.existsById(id);
    }

    // Validação de email
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}