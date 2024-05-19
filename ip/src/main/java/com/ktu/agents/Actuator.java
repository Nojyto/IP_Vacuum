package com.ktu.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Actuator extends Agent {
    private AID environmentAgent;
    private AID sensorAgent; 
    private String[] moveOrder; 
    private int moveIndex = 0; 

    @Override
    protected void setup() {
        registerWithDF();
        environmentAgent = findAgent("environment-agent");
        sensorAgent = findAgent("sensor-agent");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    moveOrder = msg.getContent().split(" ");
                    moveIndex = 0;
                    executeMoveOrder();
                } else {
                    block();
                }
            }
        });
    }

    private void executeMoveOrder() {
        if (moveIndex < moveOrder.length) {
            String move = moveOrder[moveIndex];
            moveIndex++;

            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(environmentAgent);
            request.setContent(move);
            send(request);

            addBehaviour(new WakerBehaviour(this, 500) {
                @Override
                protected void onWake() {
                    executeMoveOrder();
                }
            });
        } else {
            notifySensor();
        }
    }

    private void notifySensor() {
        if (sensorAgent != null) {
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
