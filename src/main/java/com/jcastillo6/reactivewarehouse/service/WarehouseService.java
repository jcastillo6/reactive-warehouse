package com.jcastillo6.reactivewarehouse.service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;
import java.util.function.LongSupplier;
import org.springframework.stereotype.Service;
import com.jcastillo.warehouse.model.Warehouse;
import com.jcastillo6.reactivewarehouse.entity.WarehouseEntity;
import com.jcastillo6.reactivewarehouse.repository.WarehouseRepo;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WarehouseService implements AbstractService<WarehouseEntity>
{
    private static final Random random = new Random();
    private static final LongSupplier latency = () -> random.nextLong(500);
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

    @Observed(name = "save.warehouse",
        contextualName = "saving-warehouse",
        lowCardinalityKeyValues = {"type", "address"})
    public Mono<WarehouseEntity> save(Mono<Warehouse> warehouse) {
        log.info("Validating warehouse");

        return warehouse.map(this::toWarehouseEntity)
            .flatMap(this::save)
            .delayElement(Duration.of(latency.getAsLong(), ChronoUnit.MILLIS));
    }

    @Observed(name = "mapping.warehouse",
        contextualName = "mapping-warehouse",
        lowCardinalityKeyValues = {"type", "address"})
    public WarehouseEntity toWarehouseEntity(Warehouse warehouse) {
        log.info("mapping warehouse");
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
