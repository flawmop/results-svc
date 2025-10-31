package com.insilicosoft.portal.svc.results.persistence.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapKeyColumn;
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
  @Column(nullable = false, unique = true, updatable = false)
  private long simulationId;

  // app-manager creates this identity when the simulation is run.
  @Column(insertable = false, unique = true)
  private String appManagerId;

  // Non-i18n freetext informational messages from results-svc recording incoming requests outcomes
  @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "messages", joinColumns = @JoinColumn(name = "resultsId"))
  @Column(name = "messages", nullable = false)
  @Lob
  private List<String> messages = new ArrayList<>();

  @Column(insertable = false, length = 65536)
  private String apMessages;

  // "Initialising..","0% completed", etc
  @Column(insertable = false)
  private String progressStatus;

  @Column(insertable = false, length = 2000)
  private String qNet;

  @Lob
  @Column(insertable = false, length = 512000)
  private String stderr;

  // "Python 3.9.2\ncellmlmanip==0.3.6\nchaste-codegen==0.10.2", etc.
  @Lob
  @Column(insertable = false, length = 512000)
  private String stdout;

  @Column(insertable = false, length = 32768)
  private String voltageResults;

  @ElementCollection
  @CollectionTable(name = "conc_voltage_traces", joinColumns = @JoinColumn(name = "resultsId"))
  @MapKeyColumn(name = "concentration")
  @Column(name = "concVoltageTraces", length = 131072)
  private Map<String, String> concVoltageTraces = new HashMap<>();

  @Column(insertable = false, length = 131072)
  private String voltageTraces;

  // See JPA auditing
  @LastModifiedDate
  Instant lastModifiedDate;

  // See JPA auditing
  @LastModifiedBy
  String lastModifiedBy;

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

    addMessage("Simulation prepared. Waiting to invoke");
  }

  //

  private void verify() {
    if (this.simulationId < 1l)
      throw new IllegalStateException("Results object has an invalid simulation id of '" + this.simulationId + "'");
  }

  /**
   * Add a new message.
   *
   * @param newMessage New message to add.
   */
  public void addMessage(String newMessage) {
    this.messages.add(newMessage);
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
   * @return the apMessages
   */
  public String getApMessages() {
    return apMessages;
  }

  /**
   * @param apMessages the apMessages to set
   */
  public void setApMessages(String apMessages) {
    this.apMessages = apMessages;
  }

  /**
   * @return the progressStatus
   */
  public String getProgressStatus() {
    return progressStatus;
  }

  /**
   * @param progressStatus the progressStatus to set
   */
  public void setProgressStatus(String progressStatus) {
    this.progressStatus = progressStatus;
  }

  /**
   * @return the qNet
   */
  public String getqNet() {
    return qNet;
  }

  /**
   * @param qNet the qNet to set
   */
  public void setqNet(String qNet) {
    this.qNet = qNet;
  }

  /**
   * @return the stderr
   */
  public String getStderr() {
    return stderr;
  }

  /**
   * @param stderr the stderr to set
   */
  public void setStderr(String stderr) {
    this.stderr = stderr;
  }

  /**
   * @return the stdout
   */
  public String getStdout() {
    return stdout;
  }

  /**
   * @param stdout the stdout to set
   */
  public void setStdout(String stdout) {
    this.stdout = stdout;
  }

  /**
   * @return the voltageResults
   */
  public String getVoltageResults() {
    return voltageResults;
  }

  /**
   * @param voltageResults the voltageResults to set
   */
  public void setVoltageResults(String voltageResults) {
    this.voltageResults = voltageResults;
  }

  /**
   * @return the concVoltageTrace
   */
  public Map<String, String> getConcVoltageTraces() {
    return concVoltageTraces;
  }

  /**
   * @return the voltageTraces
   */
  public String getVoltageTraces() {
    return voltageTraces;
  }

  /**
   * @param voltageTraces the voltageTraces to set
   */
  public void setVoltageTraces(String voltageTraces) {
    this.voltageTraces = voltageTraces;
  }

  /**
   * @return the simulationId
   */
  public long getSimulationId() {
    return simulationId;
  }

  /**
   * @return the messages
   */
  public List<String> getMessages() {
    return messages;
  }

  // Boilerplate implementations


  @Override
  public String toString() {
    return "Results [resultsId=" + resultsId + ", simulationId=" + simulationId + ", appManagerId=" + appManagerId
        + ", messages=" + messages + ", progressStatus=" + progressStatus + ", stderr=" + stderr + ", stdout=" + stdout
        + ", voltageResults=" + voltageResults + ", concVoltageTraces=" + concVoltageTraces + ", voltageTraces="
        + voltageTraces + ", lastModifiedDate=" + lastModifiedDate + ", lastModifiedBy=" + lastModifiedBy
        + "]";
  }

}