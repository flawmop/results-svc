package com.insilicosoft.portal.svc.results.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.service.ResultsService;

import reactor.core.publisher.Flux;

@Configuration
public class ResultsConfig {

  @Bean
  RestClient restClientSubmission(final @Value("${URL_SUBMISSION_SVC:http://submission-svc:9002/}")
                                        String submissionSvcUrl) {
    return RestClient.builder().baseUrl(submissionSvcUrl).build();
  }

  @Bean
  Consumer<Flux<SimulationCreate>> simulationCreate(final ResultsService resultsService) {
    return flux -> flux.doOnNext(simulationCreate -> resultsService.create(simulationCreate))
                       .subscribe();
  }

}