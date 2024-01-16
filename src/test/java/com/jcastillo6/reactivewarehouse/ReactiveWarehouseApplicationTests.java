package com.jcastillo6.reactivewarehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import com.jcastillo.warehouse.model.Warehouse;
import com.jcastillo6.reactivewarehouse.entity.LocatorEntity;
import com.jcastillo6.reactivewarehouse.entity.WarehouseEntity;
import com.jcastillo6.reactivewarehouse.service.WarehouseService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@ActiveProfiles("test")
@SpringBootTest(
    properties = "de.flapdoodle.mongodb.embedded.version=5.0.5",
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@EnableAutoConfiguration
@DirtiesContext
class ReactiveWarehouseApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(ReactiveWarehouseApplicationTests.class);
    @Autowired
    private WarehouseService warehouseService;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        init();
    }

    @Test
    void createNewWarehouse() throws URISyntaxException {
        var warehouse = new Warehouse();
        warehouse.name("test").address("parkzichtlaan");
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(warehouse)
            .when().request("POST", new URI("api/v1/warehouses"))
            .then().statusCode(201).body("name", equalTo("test"));
    }

    @Test
    void getWarehouse() throws URISyntaxException {
        given().accept(ContentType.JSON)
            .when().request("GET", new URI("api/v1/warehouses"))
            .then().statusCode(200).body("[0].name", equalTo("Test Init"));
    }

    private void init() {
        log.info("---> Starting database initialization");
        var locatorEntity = new LocatorEntity();
        locatorEntity.setId(UUID.randomUUID());
        locatorEntity.setCode("123214");
        locatorEntity.setDescription("Broken products");

        var warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(UUID.randomUUID());
        warehouseEntity.setName("Test Init");
        warehouseEntity.setAddress("Av. Bolivar");
        warehouseEntity.setLocatorEntity(List.of(locatorEntity));
        warehouseService.save(warehouseEntity).block();
    }
}
