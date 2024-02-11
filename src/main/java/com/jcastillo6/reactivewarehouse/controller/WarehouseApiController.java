package com.jcastillo6.reactivewarehouse.controller;

import static org.springframework.http.ResponseEntity.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.jcastillo.warehouse.api.WarehouseApi;
import com.jcastillo.warehouse.model.Locator;
import com.jcastillo.warehouse.model.Warehouse;
import com.jcastillo6.reactivewarehouse.hateoas.WarehouseRepresentationModelAssembler;
import com.jcastillo6.reactivewarehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@Slf4j
public class WarehouseApiController implements WarehouseApi {
    private static final Logger LOG = LoggerFactory.getLogger(WarehouseApiController.class);
    private WarehouseService warehouseService;
    private WarehouseRepresentationModelAssembler warehouseRepresentationModelAssembler;

    @Override
    public Mono<ResponseEntity<Warehouse>> createWarehouse(Mono<Warehouse> warehouse, ServerWebExchange exchange) {
        LOG.info("createWarehouse hit");

        return warehouseService.save(warehouse.cache())
            .map(entity -> status(HttpStatus.CREATED).body(warehouseRepresentationModelAssembler.entityToModel(entity, exchange)))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<Locator>>> getLocatorsByWarehouseId(String warehouseId, ServerWebExchange exchange) {
        return WarehouseApi.super.getLocatorsByWarehouseId(warehouseId, exchange);
    }

    @Override
    public Mono<ResponseEntity<Flux<Warehouse>>> getWarehouseById(String warehouseId, ServerWebExchange exchange) {
        return WarehouseApi.super.getWarehouseById(warehouseId, exchange);
    }

    @Override
    public Mono<ResponseEntity<Flux<Warehouse>>> getWarehouses(ServerWebExchange exchange) {
        LOG.info("getWarehouses hit");
        return Mono.just(status(HttpStatus.OK)
            .body(warehouseRepresentationModelAssembler.toListModel(warehouseService.findAll(), exchange)));
    }
}
