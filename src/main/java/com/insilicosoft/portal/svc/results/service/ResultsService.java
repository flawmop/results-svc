package com.insilicosoft.portal.svc.results.service;

import java.util.Map;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.value.ResultsDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.NewRecordDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.SimulationDataDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.StopDTO;

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
   * AppManager has created a new simulation
   * @param newRecordDTO
   * @throws EntityNotAccessibleException
   */
  void processNewRecord(NewRecordDTO newRecordDTO) throws EntityNotAccessibleException;

  /**
   * Process simulation output, e.g. voltage_results.
   *
   * @param simulationDataDTO Simulation results data DTO.
   * @throws EntityNotAccessibleException If identified Results not accessible.
   */
  void processSimulationData(SimulationDataDTO simulationDataDTO) throws EntityNotAccessibleException;

  /**
   * Process STDERR and/or STDOUT data.
   *
   * @param simulationDataDTO Simulation data DTO.
   * @throws EntityNotAccessibleException If identified Results not accessible.
   */
  void processStdData(SimulationDataDTO simulationDataDTO) throws EntityNotAccessibleException;

  /**
   * Process a notification that the simulation has stopped.
   * 
   * @param stopDto Simulation stopped notification;
   * @throws EntityNotAccessibleException If identified Results not accessible;
   */
  void processStop(StopDTO stopDto) throws EntityNotAccessibleException;

  /**
   * Retrieve the {@link Results} identified by the {@literal simulationId}.
   * 
   * @param simulationId Simulation identifier.
   * @return Results
   * @throws EntityNotAccessibleException If identified Results not accessible.
   */
  Results retrieveBySimulationId(long simulationId) throws EntityNotAccessibleException;

  /**
   * Retrieve all the {@link Results} identified by the {@literal submissionId}.
   * 
   * @param submissionId Submission identifier.
   * @return All Results for the Submission, returned map keyed by Simulation identifier.
   * @throws EntityNotAccessibleException If identified Submission not accessible.
   */
  Map<Long, Results> retrieveBySubmissionId(long submissionId) throws EntityNotAccessibleException;

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