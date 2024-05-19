package com.ktu.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/*
 * POKYCIU KURIU REIKIA ATLIKTI:
 * Dabar actuator informuoja sensor agenta, kad jis atliko visus judesius, 
 * reiktu kad informuotu pati robota ir jis veliau informuotu sensoriu kad gali rinkti duomenis
 * Pagrinde tiesiog pakeisti executeMoveOrder metodo pabaigoje esanti koda
 * 
 * Padaryt kad gautu stringa is roboto ir pagal ta stringa judetu, 
 * kolkas tiesiog yra one shot behaviour kad pajuda 1 kart ir viskas, cia tsg testavimui
 * 
 * 
 */


public class Actuator extends Agent {
    private AID environmentAgent;
    private AID sensorAgent; // Add this to store the AID of the SensorAgent
    private String moveOrder = ""; // The sequence of movements (e.g., "UP RIGHT DOWN LEFT")
    private int moveIndex = 0; // Tracks the current position in the moveOrder string

    @Override
    protected void setup() {
        // Register with the DF
        registerWithDF();

        // Example move order, you can set this dynamically as needed
        moveOrder = "UP RIGHT DOWN RIGHT ";

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                if (environmentAgent == null) {
                    // Look up the Environment Agent
                    environmentAgent = findAgent("environment-agent");
                }
                if (sensorAgent == null) {
                    // Look up the Sensor Agent
                    sensorAgent = findAgent("sensor-agent");
                }
                if (environmentAgent != null && sensorAgent != null) {
                    executeMoveOrder();
                }
            }
        });
    }

    private void executeMoveOrder() {
        String[] moves = moveOrder.split(" ");
        if (moveIndex < moves.length) {
            String move = moves[moveIndex];
            moveIndex++;

            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(environmentAgent);
            request.setContent(move);
            System.out.println("Sending move: " + move);
            send(request);

            addBehaviour(new WakerBehaviour(this, 500) {
                @Override
                protected void onWake() {
                    // Check for the response from EnvironmentAgent
                    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                    ACLMessage reply = receive(mt);
                    if (reply != null) {
                        // Successfully moved, execute next move
                        executeMoveOrder();
                    } else {
                        // If no reply, re-attempt to move
                        moveIndex--;
                        executeMoveOrder();
                    }
                }
            });
        } else {
            // Notify the SensorAgent after all moves are complete
            ACLMessage notifySensor = new ACLMessage(ACLMessage.INFORM);
            notifySensor.addReceiver(sensorAgent);
            notifySensor.setContent("MOVE_COMPLETE");
            send(notifySensor);
        }
    }

    private void registerWithDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("actuator-agent");
        sd.setName(getLocalName() + "-actuator-agent");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private AID findAgent(String agentType) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(agentType);
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            if (result.length > 0) {
                return result[0].getName();
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        return null;
    }
}