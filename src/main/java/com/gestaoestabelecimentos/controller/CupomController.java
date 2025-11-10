package com.gestaoestabelecimentos.controller;

import com.gestaoestabelecimentos.model.dto.CupomDTO;
import com.gestaoestabelecimentos.model.entity.Cupom;
import com.gestaoestabelecimentos.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cupons")
public class CupomController {

    @Autowired
    private CupomService cupomService;

    // POST - Criar cupom
    @PostMapping
    public ResponseEntity<EntityModel<Cupom>> criarCupom(@Valid @RequestBody CupomDTO cupomDTO) {
        Cupom cupom = cupomService.criarCupom(cupomDTO);

        EntityModel<Cupom> resource = EntityModel.of(cupom);

        resource.add(linkTo(methodOn(CupomController.class)
                .buscarCupomPorId(cupom.getIdCupom())).withSelfRel());
        resource.add(linkTo(methodOn(CupomController.class)
                .listarTodosCupons()).withRel("todos-cupons"));
        resource.add(linkTo(methodOn(CupomController.class)
                .atualizarCupom(cupom.getIdCupom(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(CupomController.class)
                .deletarCupom(cupom.getIdCupom())).withRel("deletar"));
        resource.add(linkTo(methodOn(CupomController.class)
                .buscarCuponsPorUsuario(cupom.getUsuario().getIdUsuario())).withRel("cupons-usuario"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Todos cupons
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cupom>>> listarTodosCupons() {
        List<Cupom> cupons = cupomService.listarTodosCupons();

        List<EntityModel<Cupom>> cupomResources = cupons.stream()
                .map(cupom -> {
                    EntityModel<Cupom> resource = EntityModel.of(cupom);
                    resource.add(linkTo(methodOn(CupomController.class)
                            .buscarCupomPorId(cupom.getIdCupom())).withSelfRel());
                    resource.add(linkTo(methodOn(CupomController.class)
                            .atualizarCupom(cupom.getIdCupom(), null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(CupomController.class)
                            .deletarCupom(cupom.getIdCupom())).withRel("deletar"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Cupom>> collectionModel = CollectionModel.of(cupomResources);

        collectionModel.add(linkTo(methodOn(CupomController.class).criarCupom(null)).withRel("criar-cupom"));
        collectionModel.add(linkTo(methodOn(CupomController.class).listarTodosCupons()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Cupom por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cupom>> buscarCupomPorId(@PathVariable Long id) {
        return cupomService.buscarCupomPorId(id)
                .map(cupom -> {
                    EntityModel<Cupom> resource = EntityModel.of(cupom);

                    resource.add(linkTo(methodOn(CupomController.class)
                            .buscarCupomPorId(id)).withSelfRel());
                    resource.add(linkTo(methodOn(CupomController.class)
                            .listarTodosCupons()).withRel("todos-cupons"));
                    resource.add(linkTo(methodOn(CupomController.class)
                            .atualizarCupom(id, null)).withRel("atualizar"));
                    resource.add(linkTo(methodOn(CupomController.class)
                            .deletarCupom(id)).withRel("deletar"));
                    resource.add(linkTo(methodOn(CupomController.class)
                            .buscarCuponsPorUsuario(cupom.getUsuario().getIdUsuario())).withRel("cupons-usuario"));
                    resource.add(linkTo(methodOn(UsuarioController.class)
                            .buscarUsuarioPorId(cupom.getUsuario().getIdUsuario())).withRel("usuario"));

                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Cupons por Usuário
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CollectionModel<EntityModel<Cupom>>> buscarCuponsPorUsuario(@PathVariable Long idUsuario) {
        List<Cupom> cupons = cupomService.buscarCuponsPorUsuario(idUsuario);

        List<EntityModel<Cupom>> cupomResources = cupons.stream()
                .map(cupom -> {
                    EntityModel<Cupom> resource = EntityModel.of(cupom);
                    resource.add(linkTo(methodOn(CupomController.class)
                            .buscarCupomPorId(cupom.getIdCupom())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Cupom>> collectionModel = CollectionModel.of(cupomResources);
        collectionModel.add(linkTo(methodOn(CupomController.class).buscarCuponsPorUsuario(idUsuario)).withSelfRel());
        collectionModel.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(idUsuario)).withRel("usuario"));

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Cupons por Utilização
    @GetMapping("/utilizado/{utilizado}")
    public ResponseEntity<CollectionModel<EntityModel<Cupom>>> buscarCuponsPorUtilizacao(@PathVariable Boolean utilizado) {
        List<Cupom> cupons = cupomService.buscarCuponsPorUtilizacao(utilizado);

        List<EntityModel<Cupom>> cupomResources = cupons.stream()
                .map(cupom -> {
                    EntityModel<Cupom> resource = EntityModel.of(cupom);
                    resource.add(linkTo(methodOn(CupomController.class)
                            .buscarCupomPorId(cupom.getIdCupom())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Cupom>> collectionModel = CollectionModel.of(cupomResources);
        collectionModel.add(linkTo(methodOn(CupomController.class).buscarCuponsPorUtilizacao(utilizado)).withSelfRel());
        collectionModel.add(linkTo(methodOn(CupomController.class).listarTodosCupons()).withRel("todos-cupons"));

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Cupons válidos por Usuário
    @GetMapping("/usuario/{idUsuario}/validos")
    public ResponseEntity<CollectionModel<EntityModel<Cupom>>> buscarCuponsValidosPorUsuario(@PathVariable Long idUsuario) {
        List<Cupom> cupons = cupomService.buscarCuponsValidosPorUsuario(idUsuario);

        List<EntityModel<Cupom>> cupomResources = cupons.stream()
                .map(cupom -> {
                    EntityModel<Cupom> resource = EntityModel.of(cupom);
                    resource.add(linkTo(methodOn(CupomController.class)
                            .buscarCupomPorId(cupom.getIdCupom())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Cupom>> collectionModel = CollectionModel.of(cupomResources);
        collectionModel.add(linkTo(methodOn(CupomController.class).buscarCuponsValidosPorUsuario(idUsuario)).withSelfRel());
        collectionModel.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(idUsuario)).withRel("usuario"));

        return ResponseEntity.ok(collectionModel);
    }

    // PUT - Atualizar cupom
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cupom>> atualizarCupom(
            @PathVariable Long id,
            @Valid @RequestBody CupomDTO cupomDTO) {

        Cupom cupomAtualizado = cupomService.atualizarCupom(id, cupomDTO);

        EntityModel<Cupom> resource = EntityModel.of(cupomAtualizado);

        resource.add(linkTo(methodOn(CupomController.class)
                .buscarCupomPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(CupomController.class)
                .listarTodosCupons()).withRel("todos-cupons"));
        resource.add(linkTo(methodOn(CupomController.class)
                .deletarCupom(id)).withRel("deletar"));

        return ResponseEntity.ok(resource);
    }

    // DELETE - Deletar cupom
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCupom(@PathVariable Long id) {
        cupomService.deletarCupom(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH - Utilizar cupom
    @PatchMapping("/{id}/utilizar")
    public ResponseEntity<EntityModel<Cupom>> utilizarCupom(@PathVariable Long id) {
        Cupom cupom = cupomService.utilizarCupom(id);

        EntityModel<Cupom> resource = EntityModel.of(cupom);
        resource.add(linkTo(methodOn(CupomController.class).buscarCupomPorId(id)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    // GET - Verificar validade do cupom
    @GetMapping("/{id}/valido")
    public ResponseEntity<EntityModel<Object>> verificarCupomValido(@PathVariable Long id) {
        boolean ehValido = cupomService.cupomEhValido(id);

        var response = new Object() {
            public final Long cupomId = id;
            public final boolean valido = ehValido;
        };

        EntityModel<Object> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(CupomController.class).verificarCupomValido(id)).withSelfRel());
        resource.add(linkTo(methodOn(CupomController.class).buscarCupomPorId(id)).withRel("cupom"));

        return ResponseEntity.ok(resource);
    }

    // POST - Gerar cupom de bonificação
    @PostMapping("/bonificacao")
    public ResponseEntity<EntityModel<Cupom>> gerarCupomBonificacao(
            @RequestParam Long idUsuario,
            @RequestParam BigDecimal valor) {

        Cupom cupom = cupomService.gerarCupomBonificacao(idUsuario, valor);

        EntityModel<Cupom> resource = EntityModel.of(cupom);
        resource.add(linkTo(methodOn(CupomController.class).buscarCupomPorId(cupom.getIdCupom())).withSelfRel());
        resource.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(idUsuario)).withRel("usuario"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // GET - Total de cupons disponíveis por usuário
    @GetMapping("/usuario/{idUsuario}/total-disponivel")
    public ResponseEntity<EntityModel<Object>> calcularTotalCuponsDisponiveis(@PathVariable Long idUsuario) {
        BigDecimal total = cupomService.calcularTotalCuponsDisponiveisPorUsuario(idUsuario);

        var response = new Object() {
            public final Long usuarioId = idUsuario;
            public final BigDecimal totalCuponsDisponiveis = total;
        };

        EntityModel<Object> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(CupomController.class).calcularTotalCuponsDisponiveis(idUsuario)).withSelfRel());
        resource.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(idUsuario)).withRel("usuario"));

        return ResponseEntity.ok(resource);
    }
}