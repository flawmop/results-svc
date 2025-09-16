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
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

}