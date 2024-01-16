package com.jcastillo6.reactivewarehouse.entity;

import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocatorEntity {
    @Id
    private UUID id;
    private String code;
    private String description;
    private List<ProductEntity> products;
}
