package com.insilicosoft.portal.svc.results.value;

public class ResultsDTO {

  private String appManagerId;

  /**
   * @return the appManagerId
   */
  public String getAppManagerId() {
    return appManagerId;
  }

  /**
   * @param appManagerId the appManagerId to set
   */
  public void setAppManagerId(String appManagerId) {
    this.appManagerId = appManagerId;
  }

  @Override
  public String toString() {
    return "ResultsDTO [appManagerId=" + appManagerId + "]";
  }

}