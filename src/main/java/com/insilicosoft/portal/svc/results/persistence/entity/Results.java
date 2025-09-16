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
  private Long entityId;

  @Column(nullable = false, updatable = false)
  private long submissionId;

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

  // Getters/Setters

  /**
   * Retrieve the entity identifier.
   * 
   * @return Entity identifier (or empty {@code Optional} if not yet persisted).
   */
  public Optional<Long> getEntityId() {
    return Optional.ofNullable(entityId);
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
    return "Results [entityId=" + entityId + ", submissionId=" + submissionId + ", message=" + message
        + ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedBy=" + lastModifiedBy + ", lockVersion="
        + lockVersion + "]";
  }

}