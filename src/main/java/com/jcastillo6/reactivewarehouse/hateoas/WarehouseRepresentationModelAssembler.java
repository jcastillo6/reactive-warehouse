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
import com.jcastillo.warehouse.model.Warehouse;
import com.jcastillo6.reactivewarehouse.entity.WarehouseEntity;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Observed
@Slf4j
public class WarehouseRepresentationModelAssembler implements ReactiveRepresentationModelAssembler<WarehouseEntity, Warehouse>, HateoasSupport {
    private static String serverUri = null;

    @Override
    public Mono<Warehouse> toModel(WarehouseEntity entity, ServerWebExchange exchange) {
        return Mono.just(entityToModel(entity, exchange));
    }

    public Warehouse entityToModel(WarehouseEntity entity, ServerWebExchange exchange) {
        log.info("to entity model");
        var resource = new Warehouse(entity.getName());
        if (Objects.isNull(entity)) {
            return resource;
        }
        BeanUtils.copyProperties(entity, resource);
        resource.setId(entity.getId());
        String serverUri = getServerUri(exchange);
        resource.add(Link.of(String.format("%s/api/v1/warehouses/%s/locators", serverUri, entity.getId())).withRel("locators"));
        resource.add(
            Link.of(String.format("%s/api/v1/warehouses/%s", serverUri, entity.getId())).withSelfRel());
        return resource;
    }

    private String getServerUri(@Nullable ServerWebExchange exchange) {
        if (Strings.isBlank(serverUri)) {
            serverUri = getUriComponentBuilder(exchange).toUriString();
        }
        return serverUri;
    }

    public Warehouse getModel(Mono<Warehouse> m) {
        AtomicReference<Warehouse> model = new AtomicReference<>();
        m.cache().subscribe(model::set);
        return model.get();
    }

    public Flux<Warehouse> toListModel(Flux<WarehouseEntity> entities, ServerWebExchange exchange) {
        if (Objects.isNull(entities)) {
            return Flux.empty();
        }
        return Flux.from(entities.map(e -> entityToModel(e, exchange)));
    }
}
