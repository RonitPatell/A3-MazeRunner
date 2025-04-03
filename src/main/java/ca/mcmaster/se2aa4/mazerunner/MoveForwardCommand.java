package ca.mcmaster.se2aa4.mazerunner;

public class MoveForwardCommand implements MoveCommand {

    private final int steps;

    public MoveForwardCommand(int steps) {
        this.steps = steps;
    }

    @Override
    public void execute(Player player) {
        for (int i = 0; i < steps; i++) {
            player.moveForward();
        }
    }
}
