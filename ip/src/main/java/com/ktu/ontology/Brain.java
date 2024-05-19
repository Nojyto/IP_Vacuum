package com.ktu.ontology;


import com.ktu.ontology.*;

/**
* Protege name: Brain
* @author ontology bean generator
* @version 2024/05/19, 15:32:28
*/
public class Brain implements BrainIf {

  private static final long serialVersionUID = 2018774048610738269L;

  private String _internalInstanceName = null;

  public Brain() {
    this._internalInstanceName = "";
  }

  public Brain(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: IP_Class9
   */
   private String iP_Class9;
   public void setIP_Class9(String value) { 
    this.iP_Class9=value;
   }
   public String getIP_Class9() {
     return this.iP_Class9;
   }

}
