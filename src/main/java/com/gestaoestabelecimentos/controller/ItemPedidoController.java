package com.gestaoestabelecimentos.controller;

import com.gestaoestabelecimentos.model.dto.ItemPedidoDTO;
import com.gestaoestabelecimentos.model.entity.ItemPedido;
import com.gestaoestabelecimentos.service.ItemPedidoService;
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
@RequestMapping("/itens-pedido")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoService itemPedidoService;

    // POST - Criar item pedido
    @PostMapping
    public ResponseEntity<EntityModel<ItemPedido>> criarItemPedido(@Valid @RequestBody ItemPedidoDTO itemPedidoDTO) {
        ItemPedido itemPedido = itemPedidoService.criarItemPedido(itemPedidoDTO);

        EntityModel<ItemPedido> resource = EntityModel.of(itemPedido);

        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .buscarItemPedidoPorId(itemPedido.getIdItem())).withSelfRel());
        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .listarTodosItensPedido()).withRel("todos-itens"));
        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .atualizarItemPedido(itemPedido.getIdItem(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .deletarItemPedido(itemPedido.getIdItem())).withRel("deletar"));
        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .buscarItensPorPedido(itemPedido.getPedido().getIdPedido())).withRel("itens-pedido"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Todos itens pedido
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ItemPedido>>> listarTodosItensPedido() {
        List<ItemPedido> itens = itemPedidoService.listarTodosItensPedido();

        List<EntityModel<ItemPedido>> itemResources = itens.stream()
                .map(item -> {
                    EntityModel<ItemPedido> resource = EntityModel.of(item);
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .buscarItemPedidoPorId(item.getIdItem())).withSelfRel());
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .atualizarItemPedido(item.getIdItem(), null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .deletarItemPedido(item.getIdItem())).withRel("deletar"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ItemPedido>> collectionModel = CollectionModel.of(itemResources);

        collectionModel.add(linkTo(methodOn(ItemPedidoController.class).criarItemPedido(null)).withRel("criar-item"));
        collectionModel.add(linkTo(methodOn(ItemPedidoController.class).listarTodosItensPedido()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Item pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ItemPedido>> buscarItemPedidoPorId(@PathVariable Long id) {
        return itemPedidoService.buscarItemPedidoPorId(id)
                .map(item -> {
                    EntityModel<ItemPedido> resource = EntityModel.of(item);

                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .buscarItemPedidoPorId(id)).withSelfRel());
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .listarTodosItensPedido()).withRel("todos-itens"));
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .atualizarItemPedido(id, null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .deletarItemPedido(id)).withRel("deletar"));
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .buscarItensPorPedido(item.getPedido().getIdPedido())).withRel("itens-pedido"));
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .buscarItensPorProduto(item.getProduto().getIdProduto())).withRel("itens-produto"));

                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Itens por Pedido
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<CollectionModel<EntityModel<ItemPedido>>> buscarItensPorPedido(@PathVariable Long idPedido) {
        List<ItemPedido> itens = itemPedidoService.buscarItensPorPedido(idPedido);

        List<EntityModel<ItemPedido>> itemResources = itens.stream()
                .map(item -> {
                    EntityModel<ItemPedido> resource = EntityModel.of(item);
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .buscarItemPedidoPorId(item.getIdItem())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ItemPedido>> collectionModel = CollectionModel.of(itemResources);
        collectionModel.add(linkTo(methodOn(ItemPedidoController.class).buscarItensPorPedido(idPedido)).withSelfRel());
        collectionModel.add(linkTo(methodOn(PedidoController.class).buscarPedidoPorId(idPedido)).withRel("pedido"));

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Itens por Produto
    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<CollectionModel<EntityModel<ItemPedido>>> buscarItensPorProduto(@PathVariable Long idProduto) {
        List<ItemPedido> itens = itemPedidoService.buscarItensPorProduto(idProduto);

        List<EntityModel<ItemPedido>> itemResources = itens.stream()
                .map(item -> {
                    EntityModel<ItemPedido> resource = EntityModel.of(item);
                    resource.add(linkTo(methodOn(ItemPedidoController.class)
                            .buscarItemPedidoPorId(item.getIdItem())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ItemPedido>> collectionModel = CollectionModel.of(itemResources);
        collectionModel.add(linkTo(methodOn(ItemPedidoController.class).buscarItensPorProduto(idProduto)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ProdutoController.class).buscarProdutoPorId(idProduto)).withRel("produto"));

        return ResponseEntity.ok(collectionModel);
    }

    // PUT - Atualizar item pedido
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ItemPedido>> atualizarItemPedido(
            @PathVariable Long id,
            @Valid @RequestBody ItemPedidoDTO itemPedidoDTO) {

        ItemPedido itemAtualizado = itemPedidoService.atualizarItemPedido(id, itemPedidoDTO);

        EntityModel<ItemPedido> resource = EntityModel.of(itemAtualizado);

        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .buscarItemPedidoPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .listarTodosItensPedido()).withRel("todos-itens"));
        resource.add(linkTo(methodOn(ItemPedidoController.class)
                .deletarItemPedido(id)).withRel("deletar"));

        return ResponseEntity.ok(resource);
    }

    // DELETE - Deletar item pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarItemPedido(@PathVariable Long id) {
        itemPedidoService.deletarItemPedido(id);
        return ResponseEntity.noContent().build();
    }

    // GET - Total vendido por produto
    @GetMapping("/produto/{idProduto}/total-vendido")
    public ResponseEntity<EntityModel<Object>> calcularTotalVendidoPorProduto(@PathVariable Long idProduto) {
        Long totalVendido = itemPedidoService.calcularTotalVendidoPorProduto(idProduto);

        var response = new Object() {
            public final Long produtoId = idProduto;
            public Long totalVendido;

            {
                totalVendido = totalVendido;
            }
        };

        EntityModel<Object> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(ItemPedidoController.class).calcularTotalVendidoPorProduto(idProduto)).withSelfRel());
        resource.add(linkTo(methodOn(ProdutoController.class).buscarProdutoPorId(idProduto)).withRel("produto"));

        return ResponseEntity.ok(resource);
    }
}