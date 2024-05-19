# IP

## Užrašams

The system should work as fallows: there is a environment agent that has a NxN grid that consists of 0 = unexplored, 1 = cleaned, 2 = dirty, 3 = obstacle, 4 = robot also knows tha x,y of the agent, the the sensor agent requests information from the grid and it receives a certain radius around the robot locaiton, if there is dirty present the informatin is sent to the brain and the brain given that subset routes a minimum path for the drone (using deikstra) to travers over all the dirty nodes and sends the information as a movement queue to the actuator which sends the information back to the environment and it applies the changes. Also note that if the sensor does not find any dirty cells it expands its search radius and return a simple heuristic for the direction in which new trash ought to be and given to the brain it gives a direction to move (avoiding obstacles)

/*
 * Initializina grida nxn
 * Processina aktuatoriaus judesio requestus, parasyta turi taip but UP RIGHT DOWN LEFT
 * Sensoriui duoda informacija apie aplinka
 * 0 = unexplored, 1 = cleaned, 2 = dirty, 3 = obstacle, 4 = robot
 * 
 * 
 */

 /*
 * INFO APIE AGENTA
 * 
 * Gauna info is actuatorius kai tas atlieka visus veiksmus, veliau bus kad gauna info is roboto
 * Kai gauna info kad baigesi aktuatoriaus darbas, pranesa environment agentui kad reikia isscoutint area
 * Area gauna kaip stringa, td pavercia i 2d masyva, veliau atgal i stringa ir nusiuncia smegenims
 * 
 * Jeigu gautoj area is environment nera dirty langeliu, tai pranesa environment agentui kad reikia isscoutint didesne area
 * Tada ta area processina ir nusiuncia smegenim.
 * PO AREA PROCESSING LIEKA TOKIA INFO:
 * 0 - clean, 1 - dirty, 2 - blocked, -1 - robot
 */


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