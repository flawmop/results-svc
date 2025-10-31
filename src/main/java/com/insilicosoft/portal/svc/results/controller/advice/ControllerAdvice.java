package com.insilicosoft.portal.svc.results.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;

/**
 * REST controller advice.
 *
 * @author geoff
 */
@RestControllerAdvice
public class ControllerAdvice {

  /**
   * Something we're going to handle.
   * 
   * @param e Entity not accessible exception.
   * @return Response entity.
   */
  @ExceptionHandler(EntityNotAccessibleException.class)
  public ResponseEntity<String> handleEntityNotAccessible(EntityNotAccessibleException e) {
    // TODO: Revert to NOT_FOUND - https://github.com/flawmop/results-svc/issues/2!
    return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
  }

}