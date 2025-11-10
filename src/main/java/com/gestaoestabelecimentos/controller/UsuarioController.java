package com.gestaoestabelecimentos.controller;

import com.gestaoestabelecimentos.model.dto.UsuarioDTO;
import com.gestaoestabelecimentos.model.entity.Usuario;
import com.gestaoestabelecimentos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // POST - Criar usuário
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioService.criarUsuario(usuarioDTO);

        EntityModel<Usuario> resource = EntityModel.of(usuario);

        // Links HATEOAS
        resource.add(linkTo(methodOn(UsuarioController.class)
                .buscarUsuarioPorId(usuario.getIdUsuario())).withSelfRel());
        resource.add(linkTo(methodOn(UsuarioController.class)
                .listarTodosUsuarios()).withRel("todos-usuarios"));
        resource.add(linkTo(methodOn(UsuarioController.class)
                .atualizarUsuario(usuario.getIdUsuario(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(UsuarioController.class)
                .deletarUsuario(usuario.getIdUsuario())).withRel("deletar"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Todos usuários
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();

        List<EntityModel<Usuario>> usuarioResources = usuarios.stream()
                .map(usuario -> {
                    EntityModel<Usuario> resource = EntityModel.of(usuario);
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .buscarUsuarioPorId(usuario.getIdUsuario())).withSelfRel());
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .atualizarUsuario(usuario.getIdUsuario(), null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .deletarUsuario(usuario.getIdUsuario())).withRel("deletar"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(usuarioResources);

        collectionModel.add(linkTo(methodOn(UsuarioController.class).criarUsuario(null)).withRel("criar-usuario"));
        collectionModel.add(linkTo(methodOn(UsuarioController.class).listarTodosUsuarios()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id)
                .map(usuario -> {
                    EntityModel<Usuario> resource = EntityModel.of(usuario);

                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .buscarUsuarioPorId(id)).withSelfRel());
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .listarTodosUsuarios()).withRel("todos-usuarios"));
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .atualizarUsuario(id, null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .deletarUsuario(id)).withRel("deletar"));
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidosPorUsuario(id)).withRel("pedidos"));

                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Usuário por Email
    @GetMapping("/email/{email}")
    public ResponseEntity<EntityModel<Usuario>> buscarUsuarioPorEmail(@PathVariable String email) {
        return usuarioService.buscarUsuarioPorEmail(email)
                .map(usuario -> {
                    EntityModel<Usuario> resource = EntityModel.of(usuario);
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .buscarUsuarioPorId(usuario.getIdUsuario())).withSelfRel());
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Usuários por Nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> buscarUsuariosPorNome(@PathVariable String nome) {
        List<Usuario> usuarios = usuarioService.buscarUsuariosPorNome(nome);

        List<EntityModel<Usuario>> usuarioResources = usuarios.stream()
                .map(usuario -> {
                    EntityModel<Usuario> resource = EntityModel.of(usuario);
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .buscarUsuarioPorId(usuario.getIdUsuario())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(usuarioResources);
        collectionModel.add(linkTo(methodOn(UsuarioController.class).buscarUsuariosPorNome(nome)).withSelfRel());
        collectionModel.add(linkTo(methodOn(UsuarioController.class).listarTodosUsuarios()).withRel("todos-usuarios"));

        return ResponseEntity.ok(collectionModel);
    }

    // PUT - Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);

        EntityModel<Usuario> resource = EntityModel.of(usuarioAtualizado);

        resource.add(linkTo(methodOn(UsuarioController.class)
                .buscarUsuarioPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(UsuarioController.class)
                .listarTodosUsuarios()).withRel("todos-usuarios"));
        resource.add(linkTo(methodOn(UsuarioController.class)
                .deletarUsuario(id)).withRel("deletar"));

        return ResponseEntity.ok(resource);
    }

    // DELETE - Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}