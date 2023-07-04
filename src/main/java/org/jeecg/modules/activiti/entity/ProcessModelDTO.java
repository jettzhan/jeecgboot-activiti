package org.jeecg.modules.activiti.entity;

import org.activiti.engine.impl.persistence.entity.ModelEntity;

public class ProcessModelDTO {
  private String bpm;
  private String svg;
  private ModelEntity modelEntity;

  public ProcessModelDTO() {}

  public ProcessModelDTO(ModelEntity modelEntity) {
    this.modelEntity = modelEntity;
  }

  public ModelEntity getModelEntity() {
    return modelEntity;
  }

  public void setModelEntity(ModelEntity modelEntity) {
    this.modelEntity = modelEntity;
  }

  public ProcessModelDTO(String bpm, String svg) {
    this.bpm = bpm;
    this.svg = svg;
  }

  public String getBpm() {
    return bpm;
  }

  public void setBpm(String bpm) {
    this.bpm = bpm;
  }

  public String getSvg() {
    return svg;
  }

  public void setSvg(String svg) {
    this.svg = svg;
  }
}
