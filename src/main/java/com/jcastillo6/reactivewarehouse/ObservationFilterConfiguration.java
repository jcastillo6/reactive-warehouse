package com.jcastillo6.reactivewarehouse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import lombok.AllArgsConstructor;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
public class ObservationFilterConfiguration {
    private final ApplicationContext applicationContext;

    @Bean
    public HttpHandler httpHandler(ObservationRegistry registry) {
        return WebHttpHandlerBuilder.applicationContext(this.applicationContext)
            .observationRegistry(registry)
            .build();
    }

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
