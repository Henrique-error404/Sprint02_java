package com.gestaoestabelecimentos.controller;

import com.gestaoestabelecimentos.model.dto.ProdutoDTO;
import com.gestaoestabelecimentos.model.entity.Produto;
import com.gestaoestabelecimentos.service.ProdutoService;
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
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // POST - Criar produto
    @PostMapping
    public ResponseEntity<EntityModel<Produto>> criarProduto(@Valid @RequestBody ProdutoDTO produtoDTO) {
        Produto produto = produtoService.criarProduto(produtoDTO);

        EntityModel<Produto> resource = EntityModel.of(produto);

        resource.add(linkTo(methodOn(ProdutoController.class)
                .buscarProdutoPorId(produto.getIdProduto())).withSelfRel());
        resource.add(linkTo(methodOn(ProdutoController.class)
                .listarTodosProdutos()).withRel("todos-produtos"));
        resource.add(linkTo(methodOn(ProdutoController.class)
                .atualizarProduto(produto.getIdProduto(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(ProdutoController.class)
                .deletarProduto(produto.getIdProduto())).withRel("deletar"));
        resource.add(linkTo(methodOn(ProdutoController.class)
                .buscarProdutosPorEstabelecimento(produto.getEstabelecimento().getIdEstabelecimento())).withRel("produtos-estabelecimento"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Todos produtos
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Produto>>> listarTodosProdutos() {
        List<Produto> produtos = produtoService.listarTodosProdutos();

        List<EntityModel<Produto>> produtoResources = produtos.stream()
                .map(produto -> {
                    EntityModel<Produto> resource = EntityModel.of(produto);
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .buscarProdutoPorId(produto.getIdProduto())).withSelfRel());
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .atualizarProduto(produto.getIdProduto(), null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .deletarProduto(produto.getIdProduto())).withRel("deletar"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Produto>> collectionModel = CollectionModel.of(produtoResources);

        collectionModel.add(linkTo(methodOn(ProdutoController.class).criarProduto(null)).withRel("criar-produto"));
        collectionModel.add(linkTo(methodOn(ProdutoController.class).listarTodosProdutos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Produto>> buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id)
                .map(produto -> {
                    EntityModel<Produto> resource = EntityModel.of(produto);

                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .buscarProdutoPorId(id)).withSelfRel());
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .listarTodosProdutos()).withRel("todos-produtos"));
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .atualizarProduto(id, null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .deletarProduto(id)).withRel("deletar"));
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .buscarProdutosPorEstabelecimento(produto.getEstabelecimento().getIdEstabelecimento())).withRel("produtos-estabelecimento"));

                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Produtos por Estabelecimento
    @GetMapping("/estabelecimento/{idEstabelecimento}")
    public ResponseEntity<CollectionModel<EntityModel<Produto>>> buscarProdutosPorEstabelecimento(
            @PathVariable Long idEstabelecimento) {

        List<Produto> produtos = produtoService.buscarProdutosPorEstabelecimento(idEstabelecimento);

        List<EntityModel<Produto>> produtoResources = produtos.stream()
                .map(produto -> {
                    EntityModel<Produto> resource = EntityModel.of(produto);
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .buscarProdutoPorId(produto.getIdProduto())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Produto>> collectionModel = CollectionModel.of(produtoResources);
        collectionModel.add(linkTo(methodOn(ProdutoController.class).buscarProdutosPorEstabelecimento(idEstabelecimento)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ProdutoController.class).listarTodosProdutos()).withRel("todos-produtos"));

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Produtos por Nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<CollectionModel<EntityModel<Produto>>> buscarProdutosPorNome(@PathVariable String nome) {
        List<Produto> produtos = produtoService.buscarProdutosPorNome(nome);

        List<EntityModel<Produto>> produtoResources = produtos.stream()
                .map(produto -> {
                    EntityModel<Produto> resource = EntityModel.of(produto);
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .buscarProdutoPorId(produto.getIdProduto())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Produto>> collectionModel = CollectionModel.of(produtoResources);
        collectionModel.add(linkTo(methodOn(ProdutoController.class).buscarProdutosPorNome(nome)).withSelfRel());
        collectionModel.add(linkTo(methodOn(ProdutoController.class).listarTodosProdutos()).withRel("todos-produtos"));

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Produtos válidos por Estabelecimento
    @GetMapping("/estabelecimento/{idEstabelecimento}/validos")
    public ResponseEntity<CollectionModel<EntityModel<Produto>>> buscarProdutosValidosPorEstabelecimento(
            @PathVariable Long idEstabelecimento) {

        List<Produto> produtos = produtoService.buscarProdutosValidosPorEstabelecimento(idEstabelecimento);

        List<EntityModel<Produto>> produtoResources = produtos.stream()
                .map(produto -> {
                    EntityModel<Produto> resource = EntityModel.of(produto);
                    resource.add(linkTo(methodOn(ProdutoController.class)
                            .buscarProdutoPorId(produto.getIdProduto())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Produto>> collectionModel = CollectionModel.of(produtoResources);
        collectionModel.add(linkTo(methodOn(ProdutoController.class).buscarProdutosValidosPorEstabelecimento(idEstabelecimento)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // PUT - Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Produto>> atualizarProduto(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoDTO produtoDTO) {

        Produto produtoAtualizado = produtoService.atualizarProduto(id, produtoDTO);

        EntityModel<Produto> resource = EntityModel.of(produtoAtualizado);

        resource.add(linkTo(methodOn(ProdutoController.class)
                .buscarProdutoPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(ProdutoController.class)
                .listarTodosProdutos()).withRel("todos-produtos"));
        resource.add(linkTo(methodOn(ProdutoController.class)
                .deletarProduto(id)).withRel("deletar"));

        return ResponseEntity.ok(resource);
    }

    // DELETE - Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH - Atualizar estoque
    @PatchMapping("/{id}/estoque")
    public ResponseEntity<EntityModel<Produto>> atualizarEstoque(
            @PathVariable Long id,
            @RequestParam Integer quantidade) {

        Produto produtoAtualizado = produtoService.atualizarEstoque(id, quantidade);

        EntityModel<Produto> resource = EntityModel.of(produtoAtualizado);
        resource.add(linkTo(methodOn(ProdutoController.class).buscarProdutoPorId(id)).withSelfRel());

        return ResponseEntity.ok(resource);
    }
}