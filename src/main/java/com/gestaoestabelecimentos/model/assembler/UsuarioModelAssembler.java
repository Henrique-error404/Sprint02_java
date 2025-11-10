package com.gestaoestabelecimentos.model.assembler;

import com.gestaoestabelecimentos.controller.UsuarioController;
import com.gestaoestabelecimentos.controller.PedidoController;
import com.gestaoestabelecimentos.model.entity.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler
        implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {

        EntityModel<Usuario> resource = EntityModel.of(usuario);

        // Self link
        resource.add(linkTo(methodOn(UsuarioController.class)
                .buscarUsuarioPorId(usuario.getIdUsuario()))
                .withSelfRel());

        // Link para coleção
        resource.add(linkTo(methodOn(UsuarioController.class)
                .listarTodosUsuarios())
                .withRel("todos-usuarios"));

        // Link para pedidos deste usuário
        resource.add(linkTo(methodOn(PedidoController.class)
                .buscarPedidosPorUsuario(usuario.getIdUsuario()))
                .withRel("pedidos"));

        // Link para atualização
        resource.add(linkTo(methodOn(UsuarioController.class)
                .atualizarUsuario(usuario.getIdUsuario(), null))
                .withRel("atualizar"));

        // Link para exclusão
        resource.add(linkTo(methodOn(UsuarioController.class)
                .deletarUsuario(usuario.getIdUsuario()))
                .withRel("deletar"));

        return resource;
    }
}