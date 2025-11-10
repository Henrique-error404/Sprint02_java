package com.gestaoestabelecimentos.controller;

import com.gestaoestabelecimentos.model.dto.PagamentoDTO;
import com.gestaoestabelecimentos.model.entity.Pagamento;
import com.gestaoestabelecimentos.service.PagamentoService;
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
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    // POST - Criar pagamento
    @PostMapping
    public ResponseEntity<EntityModel<Pagamento>> criarPagamento(@Valid @RequestBody PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = pagamentoService.criarPagamento(pagamentoDTO);

        EntityModel<Pagamento> resource = EntityModel.of(pagamento);

        resource.add(linkTo(methodOn(PagamentoController.class)
                .buscarPagamentoPorId(pagamento.getIdPagamento())).withSelfRel());
        resource.add(linkTo(methodOn(PagamentoController.class)
                .listarTodosPagamentos()).withRel("todos-pagamentos"));
        resource.add(linkTo(methodOn(PagamentoController.class)
                .atualizarPagamento(pagamento.getIdPagamento(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(PagamentoController.class)
                .deletarPagamento(pagamento.getIdPagamento())).withRel("deletar"));
        resource.add(linkTo(methodOn(PagamentoController.class)
                .buscarPagamentoPorPedido(pagamento.getPedido().getIdPedido())).withRel("pagamento-pedido"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Todos pagamentos
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pagamento>>> listarTodosPagamentos() {
        List<Pagamento> pagamentos = pagamentoService.listarTodosPagamentos();

        List<EntityModel<Pagamento>> pagamentoResources = pagamentos.stream()
                .map(pagamento -> {
                    EntityModel<Pagamento> resource = EntityModel.of(pagamento);
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .buscarPagamentoPorId(pagamento.getIdPagamento())).withSelfRel());
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .atualizarPagamento(pagamento.getIdPagamento(), null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .deletarPagamento(pagamento.getIdPagamento())).withRel("deletar"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pagamento>> collectionModel = CollectionModel.of(pagamentoResources);

        collectionModel.add(linkTo(methodOn(PagamentoController.class).criarPagamento(null)).withRel("criar-pagamento"));
        collectionModel.add(linkTo(methodOn(PagamentoController.class).listarTodosPagamentos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Pagamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pagamento>> buscarPagamentoPorId(@PathVariable Long id) {
        return pagamentoService.buscarPagamentoPorId(id)
                .map(pagamento -> {
                    EntityModel<Pagamento> resource = EntityModel.of(pagamento);

                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .buscarPagamentoPorId(id)).withSelfRel());
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .listarTodosPagamentos()).withRel("todos-pagamentos"));
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .atualizarPagamento(id, null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .deletarPagamento(id)).withRel("deletar"));
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .buscarPagamentoPorPedido(pagamento.getPedido().getIdPedido())).withRel("pagamento-pedido"));
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidoPorId(pagamento.getPedido().getIdPedido())).withRel("pedido"));

                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Pagamento por Pedido
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<EntityModel<Pagamento>> buscarPagamentoPorPedido(@PathVariable Long idPedido) {
        return pagamentoService.buscarPagamentoPorPedido(idPedido)
                .map(pagamento -> {
                    EntityModel<Pagamento> resource = EntityModel.of(pagamento);
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .buscarPagamentoPorId(pagamento.getIdPagamento())).withSelfRel());
                    resource.add(linkTo(methodOn(PedidoController.class)
                            .buscarPedidoPorId(idPedido)).withRel("pedido"));
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Pagamentos por Status
    @GetMapping("/status/{status}")
    public ResponseEntity<CollectionModel<EntityModel<Pagamento>>> buscarPagamentosPorStatus(@PathVariable Integer status) {
        List<Pagamento> pagamentos = pagamentoService.buscarPagamentosPorStatus(status);

        List<EntityModel<Pagamento>> pagamentoResources = pagamentos.stream()
                .map(pagamento -> {
                    EntityModel<Pagamento> resource = EntityModel.of(pagamento);
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .buscarPagamentoPorId(pagamento.getIdPagamento())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pagamento>> collectionModel = CollectionModel.of(pagamentoResources);
        collectionModel.add(linkTo(methodOn(PagamentoController.class).buscarPagamentosPorStatus(status)).withSelfRel());
        collectionModel.add(linkTo(methodOn(PagamentoController.class).listarTodosPagamentos()).withRel("todos-pagamentos"));

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Pagamentos por Forma
    @GetMapping("/forma/{formaPagamento}")
    public ResponseEntity<CollectionModel<EntityModel<Pagamento>>> buscarPagamentosPorForma(@PathVariable String formaPagamento) {
        List<Pagamento> pagamentos = pagamentoService.buscarPagamentosPorForma(formaPagamento);

        List<EntityModel<Pagamento>> pagamentoResources = pagamentos.stream()
                .map(pagamento -> {
                    EntityModel<Pagamento> resource = EntityModel.of(pagamento);
                    resource.add(linkTo(methodOn(PagamentoController.class)
                            .buscarPagamentoPorId(pagamento.getIdPagamento())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pagamento>> collectionModel = CollectionModel.of(pagamentoResources);
        collectionModel.add(linkTo(methodOn(PagamentoController.class).buscarPagamentosPorForma(formaPagamento)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // PUT - Atualizar pagamento
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pagamento>> atualizarPagamento(
            @PathVariable Long id,
            @Valid @RequestBody PagamentoDTO pagamentoDTO) {

        Pagamento pagamentoAtualizado = pagamentoService.atualizarPagamento(id, pagamentoDTO);

        EntityModel<Pagamento> resource = EntityModel.of(pagamentoAtualizado);

        resource.add(linkTo(methodOn(PagamentoController.class)
                .buscarPagamentoPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(PagamentoController.class)
                .listarTodosPagamentos()).withRel("todos-pagamentos"));
        resource.add(linkTo(methodOn(PagamentoController.class)
                .deletarPagamento(id)).withRel("deletar"));

        return ResponseEntity.ok(resource);
    }

    // DELETE - Deletar pagamento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPagamento(@PathVariable Long id) {
        pagamentoService.deletarPagamento(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH - Marcar como pago
    @PatchMapping("/{id}/pago")
    public ResponseEntity<EntityModel<Pagamento>> marcarComoPago(@PathVariable Long id) {
        Pagamento pagamento = pagamentoService.marcarComoPago(id);

        EntityModel<Pagamento> resource = EntityModel.of(pagamento);
        resource.add(linkTo(methodOn(PagamentoController.class).buscarPagamentoPorId(id)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    // GET - Verificar se pedido foi pago
    @GetMapping("/pedido/{idPedido}/pago")
    public ResponseEntity<EntityModel<Object>> verificarPedidoPago(@PathVariable Long idPedido) {
        boolean foiPago = pagamentoService.pedidoFoiPago(idPedido);

        var response = new Object() {
            public final Long pedidoId = idPedido;
            public final boolean pago = foiPago;
        };

        EntityModel<Object> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(PagamentoController.class).verificarPedidoPago(idPedido)).withSelfRel());
        resource.add(linkTo(methodOn(PedidoController.class).buscarPedidoPorId(idPedido)).withRel("pedido"));

        return ResponseEntity.ok(resource);
    }

    // GET - Estatísticas de pagamentos
    @GetMapping("/estatisticas/efetuados")
    public ResponseEntity<EntityModel<Object>> contarPagamentosEfetuados() {
        Long totalEfetuados = pagamentoService.contarPagamentosEfetuados();

        var response = new Object() {
            public final Long totalPagamentosEfetuados = totalEfetuados;
        };

        EntityModel<Object> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(PagamentoController.class).contarPagamentosEfetuados()).withSelfRel());
        resource.add(linkTo(methodOn(PagamentoController.class).listarTodosPagamentos()).withRel("todos-pagamentos"));

        return ResponseEntity.ok(resource);
    }
}