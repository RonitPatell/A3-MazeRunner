package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

    private Player testPlayer;

    @Before
    public void setUp() {
        testPlayer = new Player(0, 0, Player.Direction.NORTH);
    }

    @Test
    public void testCurrentPosition() {
        assertEquals(0, testPlayer.getXPos());
        assertEquals(0, testPlayer.getYPos());
        assertEquals(Player.Direction.NORTH, testPlayer.getDirection());
    }

    @Test
    public void testTurnLeft() { // Tests a full 360 of left turns
        testPlayer.turnLeft();
        assertEquals(Player.Direction.WEST, testPlayer.getDirection());

        testPlayer.turnLeft();
        assertEquals(Player.Direction.SOUTH, testPlayer.getDirection());

        testPlayer.turnLeft();
        assertEquals(Player.Direction.EAST, testPlayer.getDirection());

        testPlayer.turnLeft();
        assertEquals(Player.Direction.NORTH, testPlayer.getDirection());
    }

    @Test
    public void testTurnRight() { // Tests a full 360 of right turns
        testPlayer.turnRight();
        assertEquals(Player.Direction.EAST, testPlayer.getDirection());

        testPlayer.turnRight();
        assertEquals(Player.Direction.SOUTH, testPlayer.getDirection());

        testPlayer.turnRight();
        assertEquals(Player.Direction.WEST, testPlayer.getDirection());

        testPlayer.turnRight();
        assertEquals(Player.Direction.NORTH, testPlayer.getDirection());
    }

    @Test
    public void testMoveForward() { // Tests a 360 with forward movements
        testPlayer.moveForward();
        assertEquals(-1, testPlayer.getXPos());
        assertEquals(0, testPlayer.getYPos());

        testPlayer.turnLeft();
        testPlayer.moveForward();
        assertEquals(-1, testPlayer.getXPos());
        assertEquals(-1, testPlayer.getYPos());

        testPlayer.turnLeft();
        testPlayer.moveForward();
        assertEquals(0, testPlayer.getXPos());
        assertEquals(-1, testPlayer.getYPos());

        testPlayer.turnLeft();
        testPlayer.moveForward();
        assertEquals(0, testPlayer.getXPos());
        assertEquals(0, testPlayer.getYPos());
    }
}
