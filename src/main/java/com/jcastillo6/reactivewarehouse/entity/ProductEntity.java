package com.jcastillo6.reactivewarehouse.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductEntity {
    private String name;
    private String description;
    private double price;
    private int quantity;
}
