package com.insilicosoft.portal.svc.results.service;

import static com.insilicosoft.portal.svc.results.controller.ResultsController.FILE_TITLE_STDERR;
import static com.insilicosoft.portal.svc.results.controller.ResultsController.FILE_TITLE_STDOUT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.insilicosoft.portal.svc.results.event.SimulationCreate;
import com.insilicosoft.portal.svc.results.exception.EntityNotAccessibleException;
import com.insilicosoft.portal.svc.results.persistence.entity.Results;
import com.insilicosoft.portal.svc.results.persistence.repository.ResultsRepository;
import com.insilicosoft.portal.svc.results.value.ResultsDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.NewRecordDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.SimulationDataDTO;
import com.insilicosoft.portal.svc.results.value.appmanager.StopDTO;

/**
 * Results service implementation.
 * 
 * @author geoff
 */
@Service
public class ResultsServiceImpl implements ResultsService {

  private static final Logger log = LoggerFactory.getLogger(ResultsServiceImpl.class);

  private static final Pattern patternConcVoltageTrace = Pattern.compile("^conc_(.*)_voltage_trace$");

  private static final String fileTitleProgressStatus = "progress_status";
  private static final String fileTitleVoltageResults = "voltage_results";
  private static final String fileTitleVoltageTraces = "voltage_traces";
  private static final String fileTitleQNet = "q_net";
  private static final String fileTitleMessages = "messages";

  private static final String regexfileTitle = "^conc_.*_voltage_trace$";
  private static final String repoEntity = "Results";

  private final ResultsRepository resultsRepository;

  /**
   * Initialising constructor.
   * 
   * @param resultsRepository Results repository.
   */
  ResultsServiceImpl(final ResultsRepository resultsRepository) {
    this.resultsRepository = resultsRepository;
  }

  @Override
  public void create(final SimulationCreate simulationCreate) {
    log.debug("~create() : Invoked for '{}'", simulationCreate);

    Results results = null;
    try {
      results = new Results(simulationCreate.simulationId());
    } catch (IllegalStateException e) {
      log.error("~create() : Cannot create a new results object due to '{}'", e.getMessage());
    }

    if (results != null) {
      resultsRepository.save(results);
      log.debug("~create() : New results saved '{}'", results);
    }
  }

  @Override
  public void processNewRecord(NewRecordDTO newRecordDto) throws EntityNotAccessibleException {
    final String uuid = newRecordDto.getUuid();
    log.debug("~processNewRecord() : Invoked for AppManager id '{}'", uuid);

    final Results updated = resultsRepository.findByAppManagerId(uuid)
                                             .map(existingResults -> {
                                               existingResults.addMessage("Simulation started");
                                               return resultsRepository.save(existingResults);
                                             }).orElseThrow(
                                               () -> new EntityNotAccessibleException(repoEntity,
                                                                                      String.valueOf(uuid))
                                             );
    log.debug("~processNewRecord() : Post-update '{}'", updated);
  }

  @Override
  public void processSimulationData(SimulationDataDTO simulationDataDto) throws EntityNotAccessibleException {
    final String uuid = simulationDataDto.getUuid();
    log.debug("~processSimulationData() : Invoked for AppManager id '{}'", uuid);

    final String fileTitle = simulationDataDto.getFiletitle();
    final String contents = simulationDataDto.getContents();

    final Results found = resultsRepository.findByAppManagerId(uuid)
                                           .orElseThrow(
                                             () -> new EntityNotAccessibleException(repoEntity,
                                                                                    String.valueOf(uuid)));

    final int contentLen = contents.length();

    if (fileTitleProgressStatus.equalsIgnoreCase(fileTitle)) {
      log.debug("~processSimulationData() : progress_status '{}'", contentLen);
      found.setProgressStatus(contents);
      found.addMessage("Progress status updated (" + contentLen + " chars)");
    } else if (fileTitleVoltageResults.equalsIgnoreCase(fileTitle)) {
      log.debug("~processSimulationData() : voltage_results '{}'", contentLen);
      found.setVoltageResults(contents);
      found.addMessage("Voltage results updated (" + contentLen + " chars)");
    } else if (fileTitleVoltageTraces.equalsIgnoreCase(fileTitle)) {
      log.debug("~processSimulationData() : voltage_traces '{}'", contentLen);
      found.setVoltageTraces(contents);
      found.addMessage("Voltage traces updated (" + contentLen + " chars)");
    } else if (fileTitleQNet.equalsIgnoreCase(fileTitle)) {
      log.debug("~processSimulationData() : q_net '{}'", contentLen);
      found.setqNet(contents);
      found.addMessage("qNet updated (" + contentLen + " chars)");
    } else if (fileTitleMessages.equalsIgnoreCase(fileTitle)) {
      log.debug("~processSimulationData() : messages '{}'", contentLen);
      found.setApMessages(contents);
      found.addMessage("ApPredict messages updated (" + contentLen + " chars)");
    } else if (fileTitle.matches(regexfileTitle)) {
      log.debug("~processSimulationData() : '{}' '{}'", fileTitle, contentLen);
      final Matcher matcher = patternConcVoltageTrace.matcher(fileTitle);
      if (matcher.find()) {
        final String conc = matcher.group(1);
        found.getConcVoltageTraces().put(conc, contents);
        found.addMessage("Added conc '" + conc + "' voltage trace (" + contentLen + " chars)");
      } else {
        final String errMsg = "Could not process the '" + fileTitle + "' voltage trace file name !";
        log.error("~processSimulationData() : {}", errMsg);
        throw new IllegalStateException(errMsg);
      }
    }

    final Results updated = resultsRepository.save(found);
    log.trace("~processSimulationData() : Post-processed '{}'", updated);
  }

  @Override
  public void processStdData(SimulationDataDTO simulationDataDto) throws EntityNotAccessibleException {
    final String uuid = simulationDataDto.getUuid();
    log.debug("~processStdData() : Invoked for AppManager id '{}'", uuid);

    final Results found = resultsRepository.findByAppManagerId(uuid)
                                           .orElseThrow(
                                             () -> new EntityNotAccessibleException(repoEntity,
                                                                                    String.valueOf(uuid)));

    final String fileTitle = simulationDataDto.getFiletitle();
    final String contents = simulationDataDto.getContents();
    final int contentLen = contents.length();

    if (FILE_TITLE_STDERR.equalsIgnoreCase(fileTitle)) {
      found.setStderr(contents);
      found.addMessage("StdErr added (" + contentLen + " chars)");
    } else if (FILE_TITLE_STDOUT.equalsIgnoreCase(fileTitle)) {
      found.setStdout(contents);
      found.addMessage("StdOut added (" + contentLen + " chars)");
    }

    resultsRepository.save(found);
  }

  @Override
  public void processStop(StopDTO stopDto) throws EntityNotAccessibleException {
    final String uuid = stopDto.getUuid();
    log.debug("~processStop() : Invoked for AppManager id '{}'", uuid);

    final Results updated = resultsRepository.findByAppManagerId(uuid)
                                             .map(existingResults -> {
                                               existingResults.addMessage("STOP received");
                                               return resultsRepository.save(existingResults);
                                             }).orElseThrow(
                                               () -> new EntityNotAccessibleException(repoEntity,
                                                                                      String.valueOf(uuid))
                                             );
    log.debug("~processStop() : Post-update '{}'", updated);
  }

  @Override
  public Results retrieve(final long simulationId) throws EntityNotAccessibleException {
    log.debug("~retrieve() : Invoked for simulation id '{}'", simulationId);
    final Results results = resultsRepository.findBySimulationId(simulationId)
                                             .orElseThrow(() -> new EntityNotAccessibleException(repoEntity,
                                                                                                 String.valueOf(simulationId)));
    log.debug("~retrieve() : Retrieved results '{}'", results);
    return results;
  }

  @Override
  public void update(final long simulationId, final ResultsDTO resultsDto)
                     throws EntityNotAccessibleException, IllegalStateException {
    log.debug("~update() : Invoked for simulation id '{}'", simulationId);

    final Results updated = resultsRepository.findBySimulationId(simulationId)
                                             .map(existingResults -> {
                                               final String appManagerId = resultsDto.getAppManagerId();
                                               if (appManagerId != null) {
                                                 if (existingResults.getAppManagerId().isPresent()) {
                                                   final String errMsg = "Cannot reassign an AppManager id on a Results object!";
                                                   log.error("~update() : {}", errMsg);
                                                   throw new IllegalStateException(errMsg);
                                                 }
                                                 existingResults.addMessage("Updated with AppManager id of '" + appManagerId + "'");
                                                 existingResults.setAppManagerId(appManagerId);
                                               }

                                               return resultsRepository.save(existingResults);
                                             }).orElseThrow(
                                               () -> new EntityNotAccessibleException(repoEntity,
                                                                                      String.valueOf(simulationId))
                                             );
    log.debug("~update() : Post-update '{}'", updated);
  }

}