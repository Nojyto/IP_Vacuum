package com.ktu.ontology;


import com.ktu.ontology.*;

/**
* Protege name: Environment
* @author ontology bean generator
* @version 2024/05/19, 15:32:28
*/
public class Environment implements EnvironmentIf {

  private static final long serialVersionUID = 2018774048610738269L;

  private String _internalInstanceName = null;

  public Environment() {
    this._internalInstanceName = "";
  }

  public Environment(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: Grid
   */
   private String grid;
   public void setGrid(String value) { 
    this.grid=value;
   }
   public String getGrid() {
     return this.grid;
   }

   /**
   * Protege name: N
   */
   private int n;
   public void setN(int value) { 
    this.n=value;
   }
   public int getN() {
     return this.n;
   }

   /**
   * Protege name: RobotY
   */
   private int robotY;
   public void setRobotY(int value) { 
    this.robotY=value;
   }
   public int getRobotY() {
     return this.robotY;
   }

   /**
   * Protege name: RobotX
   */
   private int robotX;
   public void setRobotX(int value) { 
    this.robotX=value;
   }
   public int getRobotX() {
     return this.robotX;
   }

}
