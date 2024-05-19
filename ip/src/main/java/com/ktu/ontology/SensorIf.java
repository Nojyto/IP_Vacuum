package com.ktu.ontology;



/**
* Protege name: Sensor
* @author ontology bean generator
* @version 2024/05/19, 15:32:28
*/
public interface SensorIf extends jade.content.Concept {

   /**
   * Protege name: GridSubset
   */
   public void setGridSubset(String value);
   public String getGridSubset();

   /**
   * Protege name: VisionRadius
   */
   public void setVisionRadius(int value);
   public int getVisionRadius();

}
