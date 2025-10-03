package com.insilicosoft.portal.svc.results.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.persistence.repository.ResultsRepository;

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
  }

  @Override
  public Results retrieve(final long submissionId) throws EntityNotAccessibleException {
    log.debug("~retrieve() : Invoked for submission id '{}'", submissionId);
    final Results results = resultsRepository.findBySubmissionId(submissionId)
                                             .orElseThrow(() -> new EntityNotAccessibleException(repoEntity,
                                                                                                 String.valueOf(submissionId)));
    log.debug("~retrieve() : Retrieved : " + results);
    return results;
  }

}