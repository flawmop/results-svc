package com.insilicosoft.portal.svc.results.controller;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.insilicosoft.portal.svc.results.ResultsIdentifiers;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.service.ResultsService;
import com.insilicosoft.portal.svc.results.value.AppManagerDTO;
import com.insilicosoft.portal.svc.results.value.ResultsDTO;

/**
 * Results controller.
 *
 * @author geoff
 */
@RestController
@RequestMapping(ResultsIdentifiers.REQUEST_MAPPING_RESULTS)
public class ResultsController {

  private static final Logger log = LoggerFactory.getLogger(ResultsController.class);

  private final ResultsService resultsService;

  /**
   * Initialising constructor.
   * 
   * @param resultsService Results service.
   */
  public ResultsController(final ResultsService resultsService) {
    this.resultsService = resultsService;
  }

  /**
   * Retrieve results.
   * 
   * @param submissionId Submission identifier.
   */
  @GetMapping(value = "/{id}")
  public Results get(final @PathVariable(name = "id") long simulationId) 
                     throws EntityNotAccessibleException {
    log.debug("~get() : Invoked for '{}'", simulationId);

    return resultsService.retrieve(simulationId);
  }

  @PatchMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void patch(final @PathVariable(name = "id") long simulationId,
                    final @RequestBody ResultsDTO resultsDto) throws EntityNotAccessibleException {
    log.debug("~patch() : Invoked for '{}' with request body '{}'", simulationId, resultsDto);

    resultsService.update(simulationId, resultsDto);
  }

  @PostMapping(value = "/api/collection")
  @ResponseStatus(HttpStatus.OK)
  public void post(final @RequestBody AppManagerDTO appManagerDto) {
    log.debug("~post() : Invoked for '{}'", appManagerDto);

    // Extract UUID from dto
    // Update Results properties for simulation with UUID
  }
}