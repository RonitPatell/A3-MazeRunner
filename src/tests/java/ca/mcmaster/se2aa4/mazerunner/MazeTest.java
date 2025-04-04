package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MazeTest {

    private Maze testMaze;

    @BeforeEach
    public void setUp() {
        testMaze = new Maze("./examples/tiny.maz.txt");
    }

    @Test
    public void testIsFree() {
        testMaze.findEntryAndExit();
        assertEquals(true, testMaze.isFree(testMaze.getEntryRow(), testMaze.getEntryCol()));
    }

    @Test
    public void testExpandPath() {
        String path = "F2R3L2F";
        String expandedPath = Maze.expandPath(path);
        assertEquals("FRRLLLFF", expandedPath);
    }

    @Test
    public void testComputePath() {
        String path = testMaze.computePath();
        System.out.println("path");
        assertEquals("FFFFFRRFFRFFRFFRRFFRFFRFFF", path);
    }

    @Test
    public void testFactorizePath() {
        String path = "FFFFFRRFFRFFRFFRRFFRFFRFFF";
        String factorizedPath = Maze.factorizePath(path);
        assertEquals("5F2R2FR2FR2F2R2FR2FR3F", factorizedPath);
    }

    @Test
    public void testFindEntryAndExit() {
        testMaze.findEntryAndExit();
        assertEquals(5, testMaze.getEntryRow());
        assertEquals(0, testMaze.getEntryCol());
        assertEquals(1, testMaze.getExitRow());
        assertEquals(6, testMaze.getExitCol());
    }

    @Test
    public void testVerifyPath() {
        String path = "FFFFFRRFFRFFRFFRRFFRFFRFFF";
        boolean isValid = testMaze.verifyPath(path);
        assertEquals(true, isValid);
    }
}
