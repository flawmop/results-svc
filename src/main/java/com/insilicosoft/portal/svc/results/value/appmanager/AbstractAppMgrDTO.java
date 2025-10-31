package com.insilicosoft.portal.svc.results.value.appmanager;

public abstract class AbstractAppMgrDTO {

  private String uuid;

  public AbstractAppMgrDTO(String uuid) {
    super();
    this.uuid = uuid;
  }

  /**
   * @return the uuid
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * @param uuid the uuid to set
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public String toString() {
    return "AbstractAppMgrDTO [uuid=" + uuid + "]";
  }

}