package ca.mcmaster.se2aa4.mazerunner;

public class TurnLeftCommand implements MoveCommand {

    @Override
    public void execute(Player player) {
        player.turnLeft();
    }

}
