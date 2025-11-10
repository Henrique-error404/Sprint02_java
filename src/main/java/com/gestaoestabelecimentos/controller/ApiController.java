package com.gestaoestabelecimentos.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping
    public ResponseEntity<EntityModel<Map<String, Object>>> getApiRoot() {
        Map<String, Object> resources = new HashMap<>();
        resources.put("message", "API de Gestão de Estabelecimentos - FIAP");
        resources.put("version", "1.0.0");
        resources.put("timestamp", Instant.now());
        resources.put("developer", "RM559607");

        EntityModel<Map<String, Object>> model = EntityModel.of(resources);

        // Links HATEOAS para descobrir a API
        model.add(linkTo(methodOn(ApiController.class).getApiRoot()).withSelfRel());
        model.add(linkTo(methodOn(EstabelecimentoController.class).listarTodosEstabelecimentos()).withRel("estabelecimentos"));
        model.add(linkTo(methodOn(UsuarioController.class).listarTodosUsuarios()).withRel("usuarios"));
        model.add(linkTo(methodOn(ProdutoController.class).listarTodosProdutos()).withRel("produtos"));

        // Links para documentação
        model.add(linkTo(methodOn(ApiController.class).getApiDocs()).withRel("documentacao"));
        model.add(linkTo(methodOn(ApiController.class).getHealthCheck()).withRel("health-check"));

        return ResponseEntity.ok(model);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getHealthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        status.put("service", "API Estabelecimentos");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/docs")
    public ResponseEntity<EntityModel<Map<String, String>>> getApiDocs() {
        Map<String, String> docs = new HashMap<>();
        docs.put("swagger-ui", "http://localhost:8080/api/swagger-ui.html");
        docs.put("api-docs", "http://localhost:8080/api/api-docs");
        docs.put("health-check", "http://localhost:8080/api/health");

        EntityModel<Map<String, String>> model = EntityModel.of(docs);
        model.add(linkTo(methodOn(ApiController.class).getApiDocs()).withSelfRel());
        model.add(linkTo(methodOn(ApiController.class).getApiRoot()).withRel("api-root"));

        return ResponseEntity.ok(model);
    }
}