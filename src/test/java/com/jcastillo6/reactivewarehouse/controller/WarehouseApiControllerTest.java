package com.jcastillo6.reactivewarehouse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.jcastillo.warehouse.model.Warehouse;
import reactor.core.publisher.Mono;

@SpringBootTest(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
@AutoConfigureWebTestClient
class WarehouseApiControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateWarehouse() throws Exception {
        var warehouse = new Warehouse();
        warehouse.name("test");
        warehouse.address("address");

        webTestClient.post().uri("/api/v1/warehouses")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(warehouse), Warehouse.class).exchange()
            .expectStatus().isCreated().expectBody().jsonPath("name").isEqualTo("test");
    }

}