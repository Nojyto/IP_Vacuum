package com.ktu;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.AgentContainer;

public class Launcher extends Agent {
    @Override
    protected void setup() {
        try {
            AgentContainer container = getContainerController();
            // Object[][] configurations = {
            //     {"P1", "com.ktu.agents.Provider", new Object[]{1, 0, 0}},
            //     {"P2", "com.ktu.agents.Provider", new Object[]{0, 1, 0}},
            //     {"P3", "com.ktu.agents.Provider", new Object[]{0, 0, 1}},
            //     {"P4", "com.ktu.agents.Provider", new Object[]{1, 1, 0}},
            //     {"P5", "com.ktu.agents.Provider", new Object[]{1, 0, 1}},
            //     {"P6", "com.ktu.agents.Provider", new Object[]{1, 1, 1}}
            // };
            // createAndStartAgents(container, configurations);
            createAndStartAgents(container,  new Object[][]{{"Env", "com.ktu.agents.Environment", new Object[]{}}});
            createAndStartAgents(container,  new Object[][]{{"Rob", "com.ktu.agents.Robot", new Object[]{}}});
            createAndStartAgents(container,  new Object[][]{{"Sen", "com.ktu.agents.Sensor", new Object[]{}}});
            createAndStartAgents(container,  new Object[][]{{"Act", "com.ktu.agents.Actuator", new Object[]{}}});
            Thread.sleep(1000);
        } catch (InterruptedException | ControllerException e) {
            e.printStackTrace();
        }

        //doDelete();
    }

    private void createAndStartAgents(AgentContainer container, Object[][] configurations) throws ControllerException {
        for (Object[] config : configurations) {
            AgentController agent = container.createNewAgent((String) config[0], (String) config[1], (Object[]) config[2]);
            agent.start();
        }
    }
}
