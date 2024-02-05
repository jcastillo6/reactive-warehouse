package com.jcastillo6.reactivewarehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Hooks;

@Slf4j
@SpringBootApplication
public class ReactiveWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveWarehouseApplication.class, args);
        Hooks.enableAutomaticContextPropagation();
    }


    @Bean
    ApplicationListener<ApplicationStartedEvent> doOnStart(ObservationRegistry registry) {
        return event -> generateString(registry);
    }

    public void generateString(ObservationRegistry registry) {
        String something = Observation
            .createNotStarted("server.job", registry)	//1
            .lowCardinalityKeyValue("jobType", "string")	//2
            //3
            .observe(() -> {
                log.info("Generating a String...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "NOTHING";
                }
                return "SOMETHING";
            });

        log.info("Result was: " + something);
    }

}
