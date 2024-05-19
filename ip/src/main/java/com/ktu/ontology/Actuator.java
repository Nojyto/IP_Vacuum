package com.ktu.ontology;


import jade.util.leap.*;
import com.ktu.ontology.*;

/**
* Protege name: Actuator
* @author ontology bean generator
* @version 2024/05/19, 15:32:28
*/
public class Actuator implements ActuatorIf {

  private static final long serialVersionUID = 2018774048610738269L;

  private String _internalInstanceName = null;

  public Actuator() {
    this._internalInstanceName = "";
  }

  public Actuator(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: MoveQueue
   */
   private List moveQueue = new ArrayList();
   public void addMoveQueue(String elem) { 
     moveQueue.add(elem);
   }
   public boolean removeMoveQueue(String elem) {
     boolean result = moveQueue.remove(elem);
     return result;
   }
   public void clearAllMoveQueue() {
     moveQueue.clear();
   }
   public Iterator getAllMoveQueue() {return moveQueue.iterator(); }
   public List getMoveQueue() {return moveQueue; }
   public void setMoveQueue(List l) {moveQueue = l; }

}
