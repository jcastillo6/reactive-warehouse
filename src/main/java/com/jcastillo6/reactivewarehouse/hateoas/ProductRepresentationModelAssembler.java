package com.jcastillo6.reactivewarehouse.hateoas;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.reactive.ReactiveRepresentationModelAssembler;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.jcastillo.warehouse.model.Product;
import com.jcastillo6.reactivewarehouse.entity.ProductEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductRepresentationModelAssembler implements ReactiveRepresentationModelAssembler<ProductEntity, Product>, HateoasSupport {
    private static String serverUri = null;

    @Override
    public Mono<Product> toModel(ProductEntity entity, ServerWebExchange exchange) {
        return Mono.just(entityToModel(entity, exchange));
    }

    private Product entityToModel(ProductEntity entity, ServerWebExchange exchange) {
        var resource = new Product();
        if (Objects.isNull(entity)) {
            return resource;
        }
        BeanUtils.copyProperties(entity, resource);
        String serverUri = getServerUri(exchange);
        resource.add(
            Link.of(String.format("%s/api/v1/products/%s", serverUri, entity.getName())).withSelfRel());
        return resource;
    }

    private String getServerUri(@Nullable ServerWebExchange exchange) {
        if (Strings.isBlank(serverUri)) {
            serverUri = getUriComponentBuilder(exchange).toUriString();
        }
        return serverUri;
    }

    public Product getModel(Mono<Product> m) {
        AtomicReference<Product> model = new AtomicReference<>();
        m.cache().subscribe(i -> model.set(i));
        return model.get();
    }

    public Flux<Product> toListModel(Flux<ProductEntity> entities, ServerWebExchange exchange) {
        if (Objects.isNull(entities)) {
            return Flux.empty();
        }
        return Flux.from(entities.map(e -> entityToModel(e, exchange)));
    }
}
