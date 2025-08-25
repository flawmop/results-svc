package com.insilicosoft.portal.svc.results.controller;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insilicosoft.portal.svc.results.ResultsIdentifiers;

/**
 * Results controller.
 *
 * @author geoff
 */
@RestController
@RequestMapping(ResultsIdentifiers.REQUEST_MAPPING_RESULTS)
public class ResultsController {

  private static final Logger log = LoggerFactory.getLogger(ResultsController.class);

  /**
   * Initialising constructor.
   */
  public ResultsController() {
  }

  /**
   * Retrieve results.
   * 
   * @param resultsId Results identifier.
   */
  @GetMapping(value = "/{id}")
  public void get(final @PathVariable(name = "id") long resultsId) {
    log.debug("~get() : Invoked for '{}'", resultsId);
  }

}