package com.jcastillo6.reactivewarehouse.entity;

import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseEntity {
    @Id
    private UUID id;
    private String name;
    private String address;
    private List<LocatorEntity> locatorEntity;
}
