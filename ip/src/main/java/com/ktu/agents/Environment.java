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

/*
 * Initializina grida nxn
 * Processina aktuatoriaus judesio requestus, parasyta turi taip but UP RIGHT DOWN LEFT
 * Sensoriui duoda informacija apie aplinka
 * 0 = unexplored, 1 = cleaned, 2 = dirty, 3 = obstacle, 4 = robot
 * 
 * 
 */


public class Environment extends Agent {
    private int[][] grid;
    private int robotX, robotY;
    private GridFrame gridFrame;


    @Override
    protected void setup() {
        // Initialize environment grid
        grid = new int[6][6]; // Example size
        initializeGrid();
        // for (int i = 0; i < 6; i++) {
        //     for (int j = 0; j < 6; j++) {
        //         if (i <= 3 && j <= 3) {
        //             grid[i][j] = 1;
        //         } else {
        //             grid[i][j] = 2;
        //         }
        //     }
        // }

        // Register with the DF
        registerWithDF();

        // Initialize the GUI
        gridFrame = new GridFrame(grid);
        gridFrame.setVisible(true);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    String content = msg.getContent();
                    //System.out.println("Received message: " + content + " from " + msg.getSender().getLocalName());
                    if (content == null) {
                        System.err.println("Received null content in message from: " + msg.getSender().getLocalName());
                        return;
                    }
                    boolean moved = false;
                    switch (content) {
                        case "UP":
                            moved = moveUp();
                            break;
                        case "DOWN":
                            moved = moveDown();
                            break;
                        case "LEFT":
                            moved = moveLeft();
                            break;
                        case "RIGHT":
                            moved = moveRight();
                            break;
                        default:
                            System.err.println("Received unknown command: " + content);
                            break;
                    }
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    send(reply);

                    // Update the GUI
                    gridFrame.repaint();
                } else {
                    block();
                }
            }
        });
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println("Received request from " + msg.getSender().getLocalName());
                    System.out.println(content);
                    ACLMessage reply = msg.createReply();
                    
                    if (content.equals("DIFFERENT_INFORMATION")) {
                        System.out.println("Handling different information request from " + msg.getSender().getLocalName());
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(getBigGrid());
                    } else {
                        System.out.println("Handling regular information request from " + msg.getSender().getLocalName());
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(getSurroundingGrid());
                    }
                    send(reply);
        
                    // Update the GUI
                    gridFrame.repaint();
                } else {
                    block();
                }
            }
        });
    }

    private void initializeGrid() {
        // Initialize grid with some obstacles and dirty cells
        // 0 = unexplored, 1 = cleaned, 2 = dirty, 3 = obstacle, 4 = robot
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 2; // Example: All cells are dirty initially
            }
        }
        // Set some obstacles
        grid[2][2] = 3;
        grid[3][3] = 3;
        // Set robot initial position
        robotX = 0;
        robotY = 0;
        grid[robotX][robotY] = 4;
    }

    private String getBigGrid() {
        // Provide a 6x6 submatrix around the robot
        StringBuilder sb = new StringBuilder();
        int startX = Math.max(0, robotX - 3);
        int endX = Math.min(grid.length, robotX + 3);
        int startY = Math.max(0, robotY - 3);
        int endY = Math.min(grid[0].length, robotY + 3);
    
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                sb.append(grid[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private String getSurroundingGrid() {
        // Provide a 2x2 submatrix around the robot
        StringBuilder sb = new StringBuilder();
        for (int i = Math.max(0, robotX - 1); i <= Math.min(grid.length - 1, robotX + 1); i++) {
            for (int j = Math.max(0, robotY - 1); j <= Math.min(grid[i].length - 1, robotY + 1); j++) {
                sb.append(grid[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
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

    private boolean moveUp() {
        if (robotX > 0 && grid[robotX - 1][robotY] != 3) {
            grid[robotX][robotY] = 1; // Mark the current cell as cleaned
            robotX--;
            grid[robotX][robotY] = 4; // Update the robot's new position
            return true;
        }
        return false;
    }

    private boolean moveDown() {
        if (robotX < grid.length - 1 && grid[robotX + 1][robotY] != 3) {
            grid[robotX][robotY] = 1; // Mark the current cell as cleaned
            robotX++;
            grid[robotX][robotY] = 4; // Update the robot's new position
            return true;
        }
        return false;
    }

    private boolean moveLeft() {
        if (robotY > 0 && grid[robotX][robotY - 1] != 3) {
            grid[robotX][robotY] = 1; // Mark the current cell as cleaned
            robotY--;
            grid[robotX][robotY] = 4; // Update the robot's new position
            return true;
        }
        return false;
    }

    private boolean moveRight() {
        if (robotY < grid[0].length - 1 && grid[robotX][robotY + 1] != 3) {
            grid[robotX][robotY] = 1; // Mark the current cell as cleaned
            robotY++;
            grid[robotX][robotY] = 4; // Update the robot's new position
            return true;
        }
        return false;
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
