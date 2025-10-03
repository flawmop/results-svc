package com.insilicosoft.portal.svc.results.service;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;

/**
 * Results service.
 * 
 * @author geoff
 */
public interface ResultsService {

  /**
   * Create a new results entity.
   * <p>
   * A simulation creation event has been recieved so create placeholder data. 
   * 
   * @param simulationCreate Simulation creation event record.
   */
  void create(SimulationCreate simulationCreate);

  /**
   * Retrieve the {@link Results} identified by the {@literal submissionId}.
   * 
   * @param submissionId Submission identifier.
   * @return Results
   * @throws EntityNotAccessibleException If identified Results not accessible.
   */
  Results retrieve(long submissionId) throws EntityNotAccessibleException;

}