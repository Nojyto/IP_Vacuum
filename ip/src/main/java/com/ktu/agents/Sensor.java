package com.ktu.agents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Sensor extends Agent {
    private AID environmentAgent;

    @Override
    protected void setup() {
        addBehaviour(new TickerBehaviour(this, 500) {
            @Override
            protected void onTick() {
                if (environmentAgent == null) {
                    environmentAgent = findEnvironmentAgent();
                }

                if (environmentAgent != null) {
                    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                    request.addReceiver(environmentAgent);
                    send(request);

                    ACLMessage reply = receive();
                    if (reply != null && reply.getPerformative() == ACLMessage.INFORM) {
                        String data = reply.getContent();
                        System.out.println("Received grid data: \n" + data);
                    }
                }
            }
        });
    }

    private AID findEnvironmentAgent() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("environment-agent");
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
