package com.jcastillo6.reactivewarehouse.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.jcastillo.warehouse.model.Warehouse;
import com.jcastillo6.reactivewarehouse.entity.WarehouseEntity;
import com.jcastillo6.reactivewarehouse.repository.WarehouseRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WarehouseService implements AbstractService<WarehouseEntity>
{
    private final WarehouseRepo warehouseRepo;

    public WarehouseService(WarehouseRepo warehouseRepo) {
        this.warehouseRepo = warehouseRepo;
    }
    @Override
    public Flux<WarehouseEntity> findAll() {
        return warehouseRepo.findAll();
    }

    @Override
    public Mono<Long> count() {
        return warehouseRepo.count();
    }

    @Override
    public Mono<WarehouseEntity> findById(UUID id) {
        return warehouseRepo.findById(id);
    }

    @Override
    public Mono<WarehouseEntity> save(WarehouseEntity entity) {
        return warehouseRepo.save(entity);
    }

    public Mono<WarehouseEntity> save(Mono<Warehouse> warehouse) {
        return warehouse.map(this::toWarehouseEntity).flatMap(this::save);
    }

    private WarehouseEntity toWarehouseEntity(Warehouse warehouse) {
        var warehouseEntity = new WarehouseEntity();
        warehouseEntity.setName(warehouse.getName());
        warehouseEntity.setId(UUID.randomUUID());
        //warehouseEntity.setLocatorEntity(warehouse.getLocators());
        warehouseEntity.setAddress(warehouse.getAddress());
        return warehouseEntity;
    }

    @Override
    public Mono<Void> delete(WarehouseEntity entity) {
        return warehouseRepo.delete(entity);
    }
}
