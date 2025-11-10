package com.gestaoestabelecimentos.controller;

import com.gestaoestabelecimentos.model.dto.EstabelecimentoDTO;
import com.gestaoestabelecimentos.model.entity.Estabelecimento;
import com.gestaoestabelecimentos.service.EstabelecimentoService;
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
@RequestMapping("/estabelecimentos")
public class EstabelecimentoController {

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    // POST - Criar estabelecimento
    @PostMapping
    public ResponseEntity<EntityModel<Estabelecimento>> criarEstabelecimento(
            @Valid @RequestBody EstabelecimentoDTO estabelecimentoDTO) {

        Estabelecimento estabelecimento = estabelecimentoService.criarEstabelecimento(estabelecimentoDTO);

        EntityModel<Estabelecimento> resource = EntityModel.of(estabelecimento);

        // Links HATEOAS
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .buscarEstabelecimentoPorId(estabelecimento.getIdEstabelecimento())).withSelfRel());
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .listarTodosEstabelecimentos()).withRel("todos-estabelecimentos"));
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .atualizarEstabelecimento(estabelecimento.getIdEstabelecimento(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .deletarEstabelecimento(estabelecimento.getIdEstabelecimento())).withRel("deletar"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Todos estabelecimentos
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Estabelecimento>>> listarTodosEstabelecimentos() {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodosEstabelecimentos();

        List<EntityModel<Estabelecimento>> estabelecimentoResources = estabelecimentos.stream()
                .map(estabelecimento -> {
                    EntityModel<Estabelecimento> resource = EntityModel.of(estabelecimento);
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .buscarEstabelecimentoPorId(estabelecimento.getIdEstabelecimento())).withSelfRel());
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .atualizarEstabelecimento(estabelecimento.getIdEstabelecimento(), null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .deletarEstabelecimento(estabelecimento.getIdEstabelecimento())).withRel("deletar"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Estabelecimento>> collectionModel = CollectionModel.of(estabelecimentoResources);

        // Link para criar novo estabelecimento
        collectionModel.add(linkTo(methodOn(EstabelecimentoController.class).criarEstabelecimento(null)).withRel("criar-estabelecimento"));
        collectionModel.add(linkTo(methodOn(EstabelecimentoController.class).listarTodosEstabelecimentos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Estabelecimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Estabelecimento>> buscarEstabelecimentoPorId(@PathVariable Long id) {
        return estabelecimentoService.buscarEstabelecimentoPorId(id)
                .map(estabelecimento -> {
                    EntityModel<Estabelecimento> resource = EntityModel.of(estabelecimento);

                    // Links HATEOAS
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .buscarEstabelecimentoPorId(id)).withSelfRel());
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .listarTodosEstabelecimentos()).withRel("todos-estabelecimentos"));
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .atualizarEstabelecimento(id, null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .deletarEstabelecimento(id)).withRel("deletar"));
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .buscarEstabelecimentosPorTipo(estabelecimento.getTpEstabelecimento())).withRel("mesmo-tipo"));

                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Estabelecimentos por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<CollectionModel<EntityModel<Estabelecimento>>> buscarEstabelecimentosPorTipo(
            @PathVariable String tipo) {

        List<Estabelecimento> estabelecimentos = estabelecimentoService.buscarEstabelecimentosPorTipo(tipo);

        List<EntityModel<Estabelecimento>> estabelecimentoResources = estabelecimentos.stream()
                .map(estabelecimento -> {
                    EntityModel<Estabelecimento> resource = EntityModel.of(estabelecimento);
                    resource.add(linkTo(methodOn(EstabelecimentoController.class)
                            .buscarEstabelecimentoPorId(estabelecimento.getIdEstabelecimento())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Estabelecimento>> collectionModel = CollectionModel.of(estabelecimentoResources);
        collectionModel.add(linkTo(methodOn(EstabelecimentoController.class).buscarEstabelecimentosPorTipo(tipo)).withSelfRel());
        collectionModel.add(linkTo(methodOn(EstabelecimentoController.class).listarTodosEstabelecimentos()).withRel("todos-estabelecimentos"));

        return ResponseEntity.ok(collectionModel);
    }

    // PUT - Atualizar estabelecimento
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Estabelecimento>> atualizarEstabelecimento(
            @PathVariable Long id,
            @Valid @RequestBody EstabelecimentoDTO estabelecimentoDTO) {

        Estabelecimento estabelecimentoAtualizado = estabelecimentoService.atualizarEstabelecimento(id, estabelecimentoDTO);

        EntityModel<Estabelecimento> resource = EntityModel.of(estabelecimentoAtualizado);

        // Links HATEOAS
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .buscarEstabelecimentoPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .listarTodosEstabelecimentos()).withRel("todos-estabelecimentos"));
        resource.add(linkTo(methodOn(EstabelecimentoController.class)
                .deletarEstabelecimento(id)).withRel("deletar"));

        return ResponseEntity.ok(resource);
    }

    // DELETE - Deletar estabelecimento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEstabelecimento(@PathVariable Long id) {
        estabelecimentoService.deletarEstabelecimento(id);
        return ResponseEntity.noContent().build();
    }
}