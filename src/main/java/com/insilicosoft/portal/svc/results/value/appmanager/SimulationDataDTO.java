package com.insilicosoft.portal.svc.results.value.appmanager;

/**
 * File holding either
 * <ul>
 *   <li>Simulation results data, e.g. voltage_results, or</li>
 *   <li>StdErr or StdOut output</li>
 * </ul>
 */
public class SimulationDataDTO extends AbstractAppMgrDTO {

  private String filetitle;
  private String contents;

  public SimulationDataDTO(String uuid, String filetitle, String contents) {
    super(uuid);
    this.filetitle = filetitle;
    this.contents = contents;
  }

  /**
   * @return the filetitle
   */
  public String getFiletitle() {
    return filetitle;
  }
  /**
   * @param filetitle the filetitle to set
   */
  public void setFiletitle(String filetitle) {
    this.filetitle = filetitle;
  }

  /**
   * @return the contents
   */
  public String getContents() {
    return contents;
  }

  /**
   * @param contents the contents to set
   */
  public void setContents(String contents) {
    this.contents = contents;
  }

  @Override
  public String toString() {
    return "SimulationDataDTO [filetitle=" + filetitle + ", contents=" + contents + ", getUuid()=" + getUuid() + "]";
  }

}