package ca.mcmaster.se2aa4.mazerunner;

public class TurnRightCommand implements MoveCommand {
    
    @Override
    public void execute(Player player) {
        player.turnRight();
    }
}