package com.ktu.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;
// bbz neveikia nebenoru
public class Robot extends Agent {
    private AID actuatorAgent;
    private AID sensorAgent;

    @Override
    protected void setup() {
        registerWithDF();
        actuatorAgent = findAgent("actuator-agent");
        sensorAgent = findAgent("sensor-agent");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    String content = msg.getContent();
                    int[][] grid = parseGrid(content);

                    List<String> moves = calculateMinimumPath(grid);
                    sendMoveQueueToActuator(moves);
                } else {
                    block();
                }
            }
        });
    }

    private int[][] parseGrid(String gridData) {
        String[] rows = gridData.split("\n");
        int[][] grid = new int[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cells = rows[i].trim().split(" ");
            grid[i] = new int[cells.length];
            for (int j = 0; j < cells.length; j++) {
                grid[i][j] = Integer.parseInt(cells[j]);
            }
        }
        return grid;
    }

    private List<String> calculateMinimumPath(int[][] grid) {
        // Implement Dijkstra's algorithm to find the minimum path
        // This is a placeholder for the actual pathfinding logic

        // For simplicity, we'll assume the path is a sequence of "UP", "DOWN", "LEFT", "RIGHT" moves
        List<String> moves = new ArrayList<>();
        // Placeholder for path calculation
        moves.add("UP");
        moves.add("RIGHT");
        moves.add("DOWN");
        moves.add("LEFT");

        return moves;
    }

    private void sendMoveQueueToActuator(List<String> moves) {
        if (actuatorAgent != null) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(actuatorAgent);
            msg.setContent(String.join(" ", moves));
            send(msg);
        }
    }

    private void registerWithDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("robot-agent");
        sd.setName(getLocalName() + "-robot-agent");
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
