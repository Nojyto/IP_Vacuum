package com.ktu.ontology;


import com.ktu.ontology.*;

/**
* Protege name: Sensor
* @author ontology bean generator
* @version 2024/05/19, 15:32:28
*/
public class Sensor implements SensorIf {

  private static final long serialVersionUID = 2018774048610738269L;

  private String _internalInstanceName = null;

  public Sensor() {
    this._internalInstanceName = "";
  }

  public Sensor(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: GridSubset
   */
   private String gridSubset;
   public void setGridSubset(String value) { 
    this.gridSubset=value;
   }
   public String getGridSubset() {
     return this.gridSubset;
   }

   /**
   * Protege name: VisionRadius
   */
   private int visionRadius;
   public void setVisionRadius(int value) { 
    this.visionRadius=value;
   }
   public int getVisionRadius() {
     return this.visionRadius;
   }

}
