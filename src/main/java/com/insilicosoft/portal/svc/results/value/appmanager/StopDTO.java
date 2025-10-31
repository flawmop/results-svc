package com.insilicosoft.portal.svc.results.value.appmanager;

public class StopDTO extends AbstractAppMgrDTO {

  private String stop;

  public StopDTO(String uuid, String stop) {
    super(uuid);
    this.stop = stop;
  }

  /**
   * @return the stop
   */
  public String getStop() {
    return stop;
  }

  /**
   * @param stop the stop to set
   */
  public void setStop(String stop) {
    this.stop = stop;
  }

  @Override
  public String toString() {
    return "StopDTO [stop=" + stop + ", getUuid()=" + getUuid() + "]";
  }

}