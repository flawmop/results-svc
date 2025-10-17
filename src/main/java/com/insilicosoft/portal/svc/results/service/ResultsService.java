package com.insilicosoft.portal.svc.results.service;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.value.ResultsDTO;

/**
 * Results service.
 * 
 * @author geoff
 */
public interface ResultsService {

  /**
   * Create a new results entity.
   * <p>
   * A simulation creation event has been received so create placeholder data. 
   * 
   * @param simulationCreate Simulation creation event record.
   */
  void create(SimulationCreate simulationCreate);

  /**
   * Retrieve the {@link Results} identified by the {@literal simulationId}.
   * 
   * @param simulationId Simulation identifier.
   * @return Results
   * @throws EntityNotAccessibleException If identified Results not accessible.
   */
  Results retrieve(long simulationId) throws EntityNotAccessibleException;

  /**
   * Update the Results object with new values.
   * 
   * @param simulationId Simulation identifier.
   * @param resultsDto Results data transfer object.
   * @throws EntityNotAccessibleException If identified Results not accessible.
   * @throws IllegalStateException If attempting an invalid operation.
   */
  void update(long simulationId, ResultsDTO resultsDto) throws EntityNotAccessibleException,
                                                               IllegalStateException;

}