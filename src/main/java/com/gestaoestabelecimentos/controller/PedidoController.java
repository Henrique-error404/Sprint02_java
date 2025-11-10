package com.gestaoestabelecimentos.controller;

import com.gestaoestabelecimentos.model.dto.PedidoDTO;
import com.gestaoestabelecimentos.model.entity.Pedido;
import com.gestaoestabelecimentos.service.PedidoService;
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
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // POST - Criar pedido
    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoService.criarPedido(pedidoDTO);

        EntityModel<Pedido> resource = EntityModel.of(pedido);

        resource.add(linkTo(methodOn(PedidoController.class)
                .buscarPedidoPorId(pedido.getIdPedido())).withSelfRel());
        resource.add(linkTo(methodOn(PedidoController.class)
                .listarTodosPedidos()).withRel("todos-pedidos"));
        resource.add(linkTo(methodOn(PedidoController.class)
                .atualizarPedido(pedido.getIdPedido(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(PedidoController.class)
                .deletarPedido(pedido.getIdPedido())).withRel("deletar"));
        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .buscarItensPorPedido(pedido.getIdPedido())).withRel("itens-pedido"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Todos pedidos
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listarTodosPedidos() {
        List<Pedido> pedidos = pedidoService.listarTodosPedidos();

        List<EntityModel<Pedido>> pedidoResources = pedidos.stream()
                .map(pedido -> {
                    EntityModel<Pedido> resource = EntityModel.of(pedido);
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidoPorId(pedido.getIdPedido())).withSelfRel());
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .atualizarPedido(pedido.getIdPedido(), null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .deletarPedido(pedido.getIdPedido())).withRel("deletar"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidoResources);

        collectionModel.add(linkTo(methodOn(PedidoController.class).criarPedido(null)).withRel("criar-pedido"));
        collectionModel.add(linkTo(methodOn(PedidoController.class).listarTodosPedidos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> buscarPedidoPorId(@PathVariable Long id) {
        return pedidoService.buscarPedidoPorId(id)
                .map(pedido -> {
                    EntityModel<Pedido> resource = EntityModel.of(pedido);

                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidoPorId(id)).withSelfRel());
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .listarTodosPedidos()).withRel("todos-pedidos"));
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .atualizarPedido(id, null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .deletarPedido(id)).withRel("deletar"));
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .buscarItensPorPedido(id)).withRel("itens-pedido"));
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidosPorUsuario(pedido.getUsuario().getIdUsuario())).withRel("pedidos-usuario"));

                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Pedidos por Usuário
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPedidosPorUsuario(@PathVariable Long idUsuario) {
        List<Pedido> pedidos = pedidoService.buscarPedidosPorUsuario(idUsuario);

        List<EntityModel<Pedido>> pedidoResources = pedidos.stream()
                .map(pedido -> {
                    EntityModel<Pedido> resource = EntityModel.of(pedido);
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidoPorId(pedido.getIdPedido())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidoResources);
        collectionModel.add(linkTo(methodOn(PedidoController.class).buscarPedidosPorUsuario(idUsuario)).withSelfRel());
        collectionModel.add(linkTo(methodOn(PedidoController.class).listarTodosPedidos()).withRel("todos-pedidos"));

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Pedidos por Estabelecimento
    @GetMapping("/estabelecimento/{idEstabelecimento}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPedidosPorEstabelecimento(@PathVariable Long idEstabelecimento) {
        List<Pedido> pedidos = pedidoService.buscarPedidosPorEstabelecimento(idEstabelecimento);

        List<EntityModel<Pedido>> pedidoResources = pedidos.stream()
                .map(pedido -> {
                    EntityModel<Pedido> resource = EntityModel.of(pedido);
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidoPorId(pedido.getIdPedido())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidoResources);
        collectionModel.add(linkTo(methodOn(PedidoController.class).buscarPedidosPorEstabelecimento(idEstabelecimento)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Pedidos por Status
    @GetMapping("/status/{status}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPedidosPorStatus(@PathVariable Integer status) {
        List<Pedido> pedidos = pedidoService.buscarPedidosPorStatus(status);

        List<EntityModel<Pedido>> pedidoResources = pedidos.stream()
                .map(pedido -> {
                    EntityModel<Pedido> resource = EntityModel.of(pedido);
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidoPorId(pedido.getIdPedido())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidoResources);
        collectionModel.add(linkTo(methodOn(PedidoController.class).buscarPedidosPorStatus(status)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // PUT - Atualizar pedido
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> atualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoDTO pedidoDTO) {

        Pedido pedidoAtualizado = pedidoService.atualizarPedido(id, pedidoDTO);

        EntityModel<Pedido> resource = EntityModel.of(pedidoAtualizado);

        resource.add(linkTo(methodOn(PedidoController.class)
                .buscarPedidoPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(PedidoController.class)
                .listarTodosPedidos()).withRel("todos-pedidos"));
        resource.add(linkTo(methodOn(PedidoController.class)
                .deletarPedido(id)).withRel("deletar"));

        return ResponseEntity.ok(resource);
    }

    // DELETE - Deletar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPedido(@PathVariable Long id) {
        pedidoService.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH - Marcar como pronto
    @PatchMapping("/{id}/pronto")
    public ResponseEntity<EntityModel<Pedido>> marcarComoPronto(@PathVariable Long id) {
        Pedido pedido = pedidoService.marcarComoPronto(id);

        EntityModel<Pedido> resource = EntityModel.of(pedido);
        resource.add(linkTo(methodOn(PedidoController.class).buscarPedidoPorId(id)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    // POST - Adicionar item ao pedido
    @PostMapping("/{idPedido}/itens")
    public ResponseEntity<EntityModel<Object>> adicionarItemAoPedido(
            @PathVariable Long idPedido,
            @RequestParam Long idProduto,
            @RequestParam Integer quantidade) {

        // Este método será implementado no ItemPedidoController
        return ResponseEntity.ok(EntityModel.of("Funcionalidade disponível no endpoint /itens-pedido"));
    }
}