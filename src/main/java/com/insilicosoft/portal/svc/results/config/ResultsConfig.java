package com.insilicosoft.portal.svc.results.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.service.ResultsService;

import reactor.core.publisher.Flux;

@Configuration
public class ResultsConfig {

  @Bean
  Consumer<Flux<SimulationCreate>> simulationCreate(final ResultsService resultsService) {
    return flux -> flux.doOnNext(simulationCreate -> resultsService.create(simulationCreate))
                       .subscribe();
  }

}