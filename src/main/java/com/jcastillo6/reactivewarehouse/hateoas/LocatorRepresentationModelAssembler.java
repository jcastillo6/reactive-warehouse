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
import com.jcastillo.warehouse.model.Locator;
import com.jcastillo6.reactivewarehouse.entity.LocatorEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class LocatorRepresentationModelAssembler implements ReactiveRepresentationModelAssembler<LocatorEntity, Locator>, HateoasSupport {
    private static String serverUri = null;

    @Override
    public Mono<Locator> toModel(LocatorEntity entity, ServerWebExchange exchange) {
        return Mono.just(entityToModel(entity, exchange));
    }

    public Locator entityToModel(LocatorEntity entity, ServerWebExchange exchange) {
        var resource = new Locator();
        if (Objects.isNull(entity)) {
            return resource;
        }
        BeanUtils.copyProperties(entity, resource);
        resource.setId(entity.getId());
        String serverUri = getServerUri(exchange);
        resource.add(Link.of(String.format("%s/api/v1/locators/%s/products", serverUri, entity.getId())).withRel("products"));
        resource.add(
            Link.of(String.format("%s/api/v1/locators/%s", serverUri, entity.getId())).withSelfRel());
        return resource;
    }

    private String getServerUri(@Nullable ServerWebExchange exchange) {
        if (Strings.isBlank(serverUri)) {
            serverUri = getUriComponentBuilder(exchange).toUriString();
        }
        return serverUri;
    }

    public Locator getModel(Mono<Locator> m) {
        AtomicReference<Locator> model = new AtomicReference<>();
        m.cache().subscribe(i -> model.set(i));
        return model.get();
    }

    public Flux<Locator> toListModel(Flux<LocatorEntity> entities, ServerWebExchange exchange) {
        if (Objects.isNull(entities)) {
            return Flux.empty();
        }
        return Flux.from(entities.map(e -> entityToModel(e, exchange)));
    }
}
