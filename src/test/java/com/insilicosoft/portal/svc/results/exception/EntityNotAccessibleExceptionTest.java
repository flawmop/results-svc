package com.insilicosoft.portal.svc.results.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityNotAccessibleExceptionTest {

  @DisplayName("Test the initialising constructor")
  @Test
  void testConstructor() {
    final String entity = "entity";
    final String id = "id";
    final EntityNotAccessibleException e = new EntityNotAccessibleException(entity, id);
    final String message = String.format("%s with/using identifier '%s' was not found", entity, id);
    assertEquals(message, e.getMessage());
  }

}