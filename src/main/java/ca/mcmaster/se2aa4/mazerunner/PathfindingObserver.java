package ca.mcmaster.se2aa4.mazerunner;

public class PathfindingObserver implements Observer {
    
    @Override
    public void update(String event) {
        System.out.println("Pathfinding Event: " + event);
    }
}
