package com.insilicosoft.portal.svc.results.controller;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insilicosoft.portal.svc.results.ResultsIdentifiers;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.service.ResultsService;

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
  public Results get(final @PathVariable(name = "id") long submissionId) 
                     throws EntityNotAccessibleException {
    log.debug("~get() : Invoked for '{}'", submissionId);

    return resultsService.retrieve(submissionId);
  }

}