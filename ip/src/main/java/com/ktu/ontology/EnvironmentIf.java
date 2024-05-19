package com.ktu.ontology;



/**
* Protege name: Environment
* @author ontology bean generator
* @version 2024/05/19, 15:32:28
*/
public interface EnvironmentIf extends jade.content.Concept {

   /**
   * Protege name: Grid
   */
   public void setGrid(String value);
   public String getGrid();

   /**
   * Protege name: N
   */
   public void setN(int value);
   public int getN();

   /**
   * Protege name: RobotY
   */
   public void setRobotY(int value);
   public int getRobotY();

   /**
   * Protege name: RobotX
   */
   public void setRobotX(int value);
   public int getRobotX();

}
