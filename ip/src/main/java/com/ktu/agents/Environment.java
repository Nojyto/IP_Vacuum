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

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Environment extends Agent {
    private int[][] grid;
    private int robotX, robotY;
    private GridFrame gridFrame;
    private Random random;

    @Override
    protected void setup() {
        grid = new int[10][10];
        initializeGrid();
        registerWithDF();
        gridFrame = new GridFrame(grid);
        gridFrame.setVisible(true);
        random = new Random();
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                moveRandomly();
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(getSurroundingGrid());
                    send(reply);
                    gridFrame.repaint();
                } else {
                    block();
                }
            }
        });
    }

    private void initializeGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 2; 
            }
        }
        grid[2][2] = 3;
        grid[3][3] = 3;
        robotX = 0;
        robotY = 0;
        grid[robotX][robotY] = 4;
    }

    private String getSurroundingGrid() {
        StringBuilder sb = new StringBuilder();
        for (int i = Math.max(0, robotX - 1); i <= Math.min(grid.length - 1, robotX + 1); i++) {
            for (int j = Math.max(0, robotY - 1); j <= Math.min(grid[i].length - 1, robotY + 1); j++) {
                sb.append(grid[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public void moveUp() {
        if (robotX > 0 && grid[robotX - 1][robotY] != 3) {
            grid[robotX][robotY] = 1;
            robotX--;
            grid[robotX][robotY] = 4;
            gridFrame.repaint();
        }
    }

    public void moveDown() {
        if (robotX < grid.length - 1 && grid[robotX + 1][robotY] != 3) {
            grid[robotX][robotY] = 1;
            robotX++;
            grid[robotX][robotY] = 4;
            gridFrame.repaint();
        }
    }

    public void moveLeft() {
        if (robotY > 0 && grid[robotX][robotY - 1] != 3) {
            grid[robotX][robotY] = 1;
            robotY--;
            grid[robotX][robotY] = 4;
            gridFrame.repaint();
        }
    }

    public void moveRight() {
        if (robotY < grid[0].length - 1 && grid[robotX][robotY + 1] != 3) {
            grid[robotX][robotY] = 1;
            robotY++;
            grid[robotX][robotY] = 4;
            gridFrame.repaint();
        }
    }

    public void moveRandomly() {
        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                moveUp();
                break;
            case 1:
                moveDown();
                break;
            case 2:
                moveLeft();
                break;
            case 3:
                moveRight();
                break;
        }
    }

    private void registerWithDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("environment-agent");
        sd.setName(getLocalName() + "-environment-agent");
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

    class GridFrame extends JFrame {
        private int[][] grid;

        public GridFrame(int[][] grid) {
            this.grid = grid;
            setTitle("Environment Grid");
            setSize(500, 500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            GridPanel gridPanel = new GridPanel(grid);
            add(gridPanel, BorderLayout.CENTER);
        }
    }

    class GridPanel extends JPanel {
        private int[][] grid;

        public GridPanel(int[][] grid) {
            this.grid = grid;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cellSize = Math.min(getWidth() / grid.length, getHeight() / grid[0].length);

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    switch (grid[i][j]) {
                        case 0:
                            g.setColor(Color.GRAY);
                            break;
                        case 1:
                            g.setColor(Color.GREEN);
                            break;
                        case 2:
                            g.setColor(Color.YELLOW);
                            break;
                        case 3:
                            g.setColor(Color.RED);
                            break;
                        case 4:
                            g.setColor(Color.BLUE);
                            break;
                    }
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }
}
