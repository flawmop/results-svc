package com.insilicosoft.portal.svc.results.value.appmanager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewRecordDTO extends AbstractAppMgrDTO {

  @JsonCreator
  public NewRecordDTO(@JsonProperty String uuid) {
    super(uuid);
  }

}