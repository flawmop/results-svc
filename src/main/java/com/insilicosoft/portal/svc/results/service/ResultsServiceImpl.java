package com.insilicosoft.portal.svc.results.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.persistence.repository.ResultsRepository;
import com.insilicosoft.portal.svc.results.value.ResultsDTO;

/**
 * Results service implementation.
 * 
 * @author geoff
 */
@Service
public class ResultsServiceImpl implements ResultsService {

  private static final Logger log = LoggerFactory.getLogger(ResultsServiceImpl.class);
  private static final String repoEntity = "Results";

  private final ResultsRepository resultsRepository;

  /**
   * Initialising constructor.
   * 
   * @param resultsRepository Results repository.
   */
  ResultsServiceImpl(final ResultsRepository resultsRepository) {
    this.resultsRepository = resultsRepository;
  }

  @Override
  public void create(final SimulationCreate simulationCreate) {
    log.debug("~create() : Invoked for '{}'", simulationCreate);

    Results results = null;
    try {
      results = new Results(simulationCreate.simulationId());
    } catch (IllegalStateException e) {
      log.error("~create() : Cannot create a new results object due to '{}'", e.getMessage());
    }

    if (results != null) {
      resultsRepository.save(results);
      log.debug("~create() : New results saved '{}'", results);
    }
  }

  @Override
  public Results retrieve(final long simulationId) throws EntityNotAccessibleException {
    log.debug("~retrieve() : Invoked for simulation id '{}'", simulationId);
    final Results results = resultsRepository.findBySimulationId(simulationId)
                                             .orElseThrow(() -> new EntityNotAccessibleException(repoEntity,
                                                                                                 String.valueOf(simulationId)));
    log.debug("~retrieve() : Retrieved results '{}'", results);
    return results;
  }

  @Override
  public void update(final long simulationId, final ResultsDTO resultsDto)
                     throws EntityNotAccessibleException, IllegalStateException {
    log.debug("~update() : Invoked for simulation id '{}'", simulationId);

    final Results updated = resultsRepository.findBySimulationId(simulationId)
                                             .map(existingResults -> {
                                               if (resultsDto.getAppManagerId() != null) {
                                                 if (existingResults.getAppManagerId().isPresent()) {
                                                   final String errMsg = "Cannot reassign an AppManager id on a Results object!";
                                                   log.error("~update() : {}", errMsg);
                                                   throw new IllegalStateException(errMsg);
                                                 }
                                                 existingResults.setAppManagerId(resultsDto.getAppManagerId());
                                               }

                                               return resultsRepository.save(existingResults);
                                             }).orElseThrow(
                                               () -> new EntityNotAccessibleException(repoEntity,
                                                                                      String.valueOf(simulationId))
                                             );
    log.debug("~update() : Post-update '{}'", updated);
  }

}