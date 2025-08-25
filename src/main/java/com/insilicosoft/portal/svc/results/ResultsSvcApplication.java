package com.insilicosoft.portal.svc.results;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ResultsSvcApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResultsSvcApplication.class, args);
  }

}