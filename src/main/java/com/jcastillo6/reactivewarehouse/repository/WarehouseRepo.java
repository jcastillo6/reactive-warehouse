package com.jcastillo6.reactivewarehouse.repository;

import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.jcastillo6.reactivewarehouse.entity.WarehouseEntity;

public interface WarehouseRepo extends ReactiveMongoRepository<WarehouseEntity, UUID> {
}
