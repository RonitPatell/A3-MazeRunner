package ca.mcmaster.se2aa4.mazerunner;

public class Player {

    private int x;
    private int y;
    private Direction direction;

    public Player(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void turnLeft() {
        direction = direction.turnLeft();
    }

    public void turnRight() {
        direction = direction.turnRight();
    }

    public void moveForward() {
        x += direction.deltaRow();
        y += direction.deltaCol();
    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction turnLeft() {
            switch (this) {
                case NORTH -> {
                    return WEST;
                }
                case WEST -> {
                    return SOUTH;
                }
                case SOUTH -> {
                    return EAST;
                }
                case EAST -> {
                    return NORTH;
                }
            }
            return null;
        }

        public Direction turnRight() {
            switch (this) {
                case NORTH -> {
                    return EAST;
                }
                case EAST -> {
                    return SOUTH;
                }
                case SOUTH -> {
                    return WEST;
                }
                case WEST -> {
                    return NORTH;
                }
            }
            return null;
        }

        public int deltaRow() {
            return switch (this) {
                case NORTH ->
                    -1;
                case SOUTH ->
                    1;
                default ->
                    0;
            };
        }

        public int deltaCol() {
            return switch (this) {
                case EAST ->
                    1;
                case WEST ->
                    -1;
                default ->
                    0;
            };
        }
    }
}
