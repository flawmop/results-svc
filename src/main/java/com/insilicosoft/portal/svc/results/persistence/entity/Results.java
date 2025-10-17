package com.insilicosoft.portal.svc.results.persistence.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Results entity.
 * 
 * @author geoff
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "results")
public class Results {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  private Long resultsId;

  // Portal application creates this identity when the simulation is persisted
  @Column(nullable = false, updatable = false)
  private long simulationId;

  // app-manager creates this identity when the simulation is run.
  @Column()
  private String appManagerId;

  @Column(name = "message")
  private String message;

  // See JPA auditing
  @LastModifiedDate
  Instant lastModifiedDate;

  // See JPA auditing
  @LastModifiedBy
  String lastModifiedBy;

  // Optimistic locking concurrency control
  @Version
  private Long lockVersion;

  // Default constructor.
  Results() {}

  /**
   * Initialising <b>and {@code verify()}ing</b> constructor.
   * 
   * @param simulationId Simulation identifier.
   * @throws IllegalStateException On attempting to construct a <code>Results</code> object with
   *                               invalid properties.
   */
  public Results(final long simulationId) throws IllegalStateException {
    this.simulationId = simulationId;

    verify();
  }

  //

  private void verify() {
    if (this.simulationId < 1l)
      throw new IllegalStateException("Results object has an invalid simulation id of '" + this.simulationId + "'");
  }

  // Getters/Setters

  /**
   * Retrieve the App-Manager identifier.
   * 
   * @return The app-manager identifier.
   */
  public Optional<String> getAppManagerId() {
    return Optional.ofNullable(appManagerId);
  }

  /**
   * Assign the app-manager identifier.
   * 
   * @param The app-manager identifier.
   */
  public void setAppManagerId(final String appManagerId) {
    if (this.appManagerId != null)
      throw new IllegalArgumentException("Cannot reassign an app-manager id on a Results object");

    this.appManagerId = appManagerId;
  }

  /**
   * Retrieve the results identifier.
   * 
   * @return Results identifier (or empty {@code Optional} if not yet persisted).
   */
  public Optional<Long> getResultsId() {
    return Optional.ofNullable(resultsId);
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  // Boilerplate implementations

  @Override
  public String toString() {
    return "Results [resultsId=" + resultsId + ", simulationId=" + simulationId + ", appManagerId=" + appManagerId
        + ", message=" + message + ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedBy=" + lastModifiedBy
        + ", lockVersion=" + lockVersion + "]";
  }

}