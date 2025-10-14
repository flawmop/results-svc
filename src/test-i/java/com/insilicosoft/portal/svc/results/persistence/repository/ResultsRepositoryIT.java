package com.insilicosoft.portal.svc.results.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.insilicosoft.portal.svc.results.persistence.entity.Results;

// TODO: Test passes, but HikariPool generating PSQLException/ConnectException 

// Note: By default, tests annotated with {@code @DataJpaTest} are transactional and roll back at the end of each test
@DataJpaTest
// We're providing the testcontainer as the datasource.... don't replace it with a default embedded!
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
public class ResultsRepositoryIT {

  @Autowired
  private ResultsRepository resultsRepository;

  @Container
  // https://docs.spring.io/spring-boot/reference/testing/testcontainers.html#testing.testcontainers.service-connections
  @ServiceConnection(type = JdbcConnectionDetails.class)
  private static PostgreSQLContainer<?> postgresTc = new PostgreSQLContainer<>("postgres:14.4");

  @Test
  void retrieveResults() {
    final long simulationId = 1l;
    resultsRepository.save(new Results(simulationId));

    assertThat(resultsRepository.findBySimulationId(simulationId)).isNotNull();
  }

}