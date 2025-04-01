package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(0, 0, Player.Direction.NORTH);
    }

    @Test
    public void testCurrentPosition() {
        assertEquals(0, player.getXPos());
        assertEquals(0, player.getYPos());
        assertEquals(Player.Direction.NORTH, player.getDirection());
    }

    @Test
    public void testTurnLeft() { // Tests a full 360 of left turns
        player.turnLeft();
        assertEquals(Player.Direction.WEST, player.getDirection());

        player.turnLeft();
        assertEquals(Player.Direction.SOUTH, player.getDirection());

        player.turnLeft();
        assertEquals(Player.Direction.EAST, player.getDirection());

        player.turnLeft();
        assertEquals(Player.Direction.NORTH, player.getDirection());
    }

    @Test
    public void testTurnRight() { // Tests a full 360 of right turns
        player.turnRight();
        assertEquals(Player.Direction.EAST, player.getDirection());

        player.turnRight();
        assertEquals(Player.Direction.SOUTH, player.getDirection());

        player.turnRight();
        assertEquals(Player.Direction.WEST, player.getDirection());

        player.turnRight();
        assertEquals(Player.Direction.NORTH, player.getDirection());
    }

    @Test
    public void testMoveForward() { // Tests a 360 with forward movements
        player.moveForward();
        assertEquals(-1, player.getXPos());
        assertEquals(0, player.getYPos());

        player.turnLeft();
        player.moveForward();
        assertEquals(-1, player.getXPos());
        assertEquals(-1, player.getYPos());

        player.turnLeft();
        player.moveForward();
        assertEquals(0, player.getXPos());
        assertEquals(-1, player.getYPos());

        player.turnLeft();
        player.moveForward();
        assertEquals(0, player.getXPos());
        assertEquals(0, player.getYPos());
    }

}
