package com.insilicosoft.portal.svc.results.controller;

import java.util.Map;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.insilicosoft.portal.svc.results.ResultsIdentifiers;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.service.ResultsService;
import com.insilicosoft.portal.svc.results.value.ResultsDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.NewRecordDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.SimulationDataDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.StopDTO;

/**
 * Results controller.
 *
 * @author geoff
 */
@RestController
@RequestMapping(ResultsIdentifiers.REQUEST_MAPPING_RESULTS)
public class ResultsController {

  public static final String FILE_TITLE_STDERR = "STDERR";
  public static final String FILE_TITLE_STDOUT = "STDOUT";

  private static final String jsonFieldContents = "contents";
  private static final String jsonFieldFileTitle = "filetitle";
  private static final String jsonFieldStop = "stop";
  private static final String jsonFieldUUID = "uuid";

  private final ResultsService resultsService;

  private static final Logger log = LoggerFactory.getLogger(ResultsController.class);

  /**
   * Initialising constructor.
   * 
   * @param resultsService Results service.
   */
  public ResultsController(final ResultsService resultsService) {
    this.resultsService = resultsService;
  }

  /**
   * Retrieve results for a particular Simulation.
   * 
   * @param simulationId Simulation identifier.
   */
  @GetMapping(value = "/simulation/{simulationId}")
  public Results getBySimulationId(final @PathVariable(name = "simulationId") long simulationId) 
                                   throws EntityNotAccessibleException {
    log.debug("~getBySimulationId() : Invoked for '{}'", simulationId);

    return resultsService.retrieveBySimulationId(simulationId);
  }


  @GetMapping(value = "/submission/{submissionId}")
  public Map<Long, Results> getBySubmissionId(final @PathVariable(name = "submissionId")
                                                    long submissionId)
                                              throws EntityNotAccessibleException {
    log.debug("~getBySubmissionId() : Invoked for '{}'", submissionId);

    return resultsService.retrieveBySubmissionId(submissionId);
  }

  /**
   * Process the request (probably from simulation-invoke-svc) to update the Results with the
   * app-manager uuid.
   *
   * @param simulationId Simulation identifier.
   * @param resultsDto DTO containing app manager uuid.
   * @throws EntityNotAccessibleException If can't find by simulation identifier.
   */
  @PatchMapping(value = "/{id}")
  public void patch(final @PathVariable(name = "id") long simulationId,
                    final @RequestBody ResultsDTO resultsDto) throws EntityNotAccessibleException {
    log.debug("~patch() : Invoked for '{}' with request body '{}'", simulationId, resultsDto);

    resultsService.update(simulationId, resultsDto);
  }

  /**
   * Handle AppManager data in one of three scenarios.
   * <ul>
   *   <li>
   *     A <code>STOP</stop> file has been written by the simulation, indicating that the
   *     simulation has finished.<p>
   *     It's <b>**important**</b> to note that due to datasets potentially being large, there will
   *     still be a couple of additional file contents received after STOP was received!
   *     <a href="https://github.com/CardiacModelling/ap-nimbus/blob/779881013a3cb2faaf7e03a3ce7848cf5439c8fb/app-manager/server.js#L656">GitHub</a>
   *   </li>
   *   <li>
   *     A progress_status, voltage_results, conc_.*_voltage_trace, voltage_traces, q_net or
   *     messages file has been changed.<p>
   *     <a href="https://github.com/CardiacModelling/ap-nimbus/blob/779881013a3cb2faaf7e03a3ce7848cf5439c8fb/app-manager/server.js#L695">GitHub</a>
   *   </li>
   *   <li>
   *     A STDERR or STDOUT file had been changed.</p>
   *     <a href="https://github.com/CardiacModelling/ap-nimbus/blob/779881013a3cb2faaf7e03a3ce7848cf5439c8fb/app-manager/server.js#L742">GitHub</a>
   *   </li>
   * </ul>
   *
   * @param bodyNode Request body.
   */
  @PostMapping(value = "/api/collection")
  public void postData(final @RequestBody ObjectNode bodyNode) throws EntityNotAccessibleException {
    log.trace("~postData() : Invoked for '{}'", bodyNode.toString());

    final JsonNode uuidNode = bodyNode.get(jsonFieldUUID);
    final JsonNode stopNode = bodyNode.get(jsonFieldStop);

    if (stopNode != null) {
      final StopDTO stopDto = new StopDTO(uuidNode.asText(), stopNode.asText());

      log.debug("~postData() : It's a StopDTO '{}'", stopDto);
      resultsService.processStop(stopDto);
      return;
    }

    // Simulation results filedata (e.g. voltage_traces, progress_status) or invocation STDERR/STDOUT
    final String fileTitle = bodyNode.get(jsonFieldFileTitle).asText();
    final String contents = bodyNode.get(jsonFieldContents).toString();

    final SimulationDataDTO simulationDataDto = new SimulationDataDTO(uuidNode.asText(), fileTitle,
                                                                      contents);
    log.trace("~postData() : It's a SimulationDataDTO '{}'", simulationDataDto);
    if (FILE_TITLE_STDERR.equals(fileTitle) || FILE_TITLE_STDOUT.equals(fileTitle)) {
      resultsService.processStdData(simulationDataDto);
    } else {
      resultsService.processSimulationData(simulationDataDto);
    }
  }

  /**
   * AppManager has just created a new directory to hold results, indicating the simulation's been
   * started.
   * <p>
   * <a href="https://github.com/CardiacModelling/ap-nimbus/blob/779881013a3cb2faaf7e03a3ce7848cf5439c8fb/app-manager/server.js#L626">GitHub</a>
   * 
   * @param newRecordDto New record DTO.
   */
  @PostMapping(value = "/api/collections")
  public void postNewRecord(final @RequestBody NewRecordDTO newRecordDto)
                            throws EntityNotAccessibleException {
    log.debug("~postNewRecord() : Invoked for '{}'", newRecordDto);

    resultsService.processNewRecord(newRecordDto);
  }

}