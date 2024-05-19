package com.ktu.ontology;


import jade.util.leap.*;

/**
* Protege name: Actuator
* @author ontology bean generator
* @version 2024/05/19, 15:32:28
*/
public interface ActuatorIf extends jade.content.Concept {

   /**
   * Protege name: MoveQueue
   */
   public void addMoveQueue(String elem);
   public boolean removeMoveQueue(String elem);
   public void clearAllMoveQueue();
   public Iterator getAllMoveQueue();
   public List getMoveQueue();
   public void setMoveQueue(List l);

}
