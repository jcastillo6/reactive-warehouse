package com.jcastillo6.reactivewarehouse.service;

import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AbstractService<T> {
    Flux<T> findAll();
    Mono<Long> count();
    Mono<T> findById(UUID id);
    Mono<T> save(T entity);
    Mono<Void> delete(T entity);
}
