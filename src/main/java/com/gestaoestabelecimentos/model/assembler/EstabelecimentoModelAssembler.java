package com.gestaoestabelecimentos.model.assembler;

import com.gestaoestabelecimentos.controller.EstabelecimentoController;
import com.gestaoestabelecimentos.controller.ProdutoController;
import com.gestaoestabelecimentos.model.entity.Estabelecimento;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstabelecimentoModelAssembler
        implements RepresentationModelAssembler<Estabelecimento, EntityModel<Estabelecimento>> {

    @Override
    public EntityModel<Estabelecimento> toModel(Estabelecimento estabelecimento) {

        EntityModel<Estabelecimento> resource = EntityModel.of(estabelecimento);

        // Self link
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .buscarEstabelecimentoPorId(estabelecimento.getIdEstabelecimento()))
                .withSelfRel());

        // Link para coleção
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .listarTodosEstabelecimentos())
                .withRel("todos-estabelecimentos"));

        // Link para produtos deste estabelecimento
        resource.add(linkTo(methodOn(ProdutoController.class)
                .buscarProdutosPorEstabelecimento(estabelecimento.getIdEstabelecimento()))
                .withRel("produtos"));

        // Link para atualização
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .atualizarEstabelecimento(estabelecimento.getIdEstabelecimento(), null))
                .withRel("atualizar"));

        // Link para exclusão
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .deletarEstabelecimento(estabelecimento.getIdEstabelecimento()))
                .withRel("deletar"));

        // Link para estabelecimentos do mesmo tipo
        if (estabelecimento.getTpEstabelecimento() != null) {
            resource.add(linkTo(methodOn(EstabelecimentoController.class)
                    .buscarEstabelecimentosPorTipo(estabelecimento.getTpEstabelecimento()))
                    .withRel("mesmo-tipo"));
        }

        return resource;
    }
}