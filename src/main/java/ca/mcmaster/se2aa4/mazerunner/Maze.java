package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Maze {

    private static final Logger logger = LogManager.getLogger(Maze.class);

    private final String inputPath;
    private char[][] grid;
    private int totalRows;
    private int totalCols;

    private int entryRow, entryCol;
    private int exitRow, exitCol;

    private final Observable observable = new Observable(); // Observable instance

    public Maze(String inputPath) {
        this.inputPath = inputPath;
        processMaze();
    }

    public void processMaze() {
        List<String> fileLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (Exception e) {
            logger.error("Failed to load the maze: " + e.getMessage());
            return;
        }
        totalRows = fileLines.size();
        totalCols = fileLines.get(0).length();
        grid = new char[totalRows][totalCols];

        for (int row = 0; row < totalRows; row++) {
            String line = fileLines.get(row);
            for (int col = 0; col < totalCols; col++) {
                if (col < line.length()) {
                    grid[row][col] = line.charAt(col);
                } else {
                    grid[row][col] = ' ';
                }
            }
        }
        findEntryAndExit();
    }

    protected void findEntryAndExit() {
        Integer leftOpeningRow = null;
        Integer rightOpeningRow = null;
        for (int row = 0; row < totalRows; row++) {
            if (grid[row][0] == ' ') {
                leftOpeningRow = row;
            }
            if (grid[row][totalCols - 1] == ' ') {
                rightOpeningRow = row;
            }
        }
        if (leftOpeningRow != null && rightOpeningRow != null) {
            entryRow = leftOpeningRow;
            entryCol = 0;
            exitRow = rightOpeningRow;
            exitCol = totalCols - 1;
        } else {
            logger.error("No valid entry/exit found in the maze.");
        }
    }

    public boolean isFree(int row, int col) {
        return row >= 0 && row < totalRows
                && col >= 0 && col < totalCols
                && grid[row][col] == ' ';
    }

    public void displayMaze() {
        for (int row = 0; row < totalRows; row++) {
            StringBuilder line = new StringBuilder();
            line.append("  ");
            for (int col = 0; col < totalCols; col++) {
                line.append(grid[row][col] == ' ' ? ' ' : '#');
            }
            System.out.println(line.toString());
        }
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer); // Delegate to Observable
    }

    public String computePath() {
        StringBuilder path = new StringBuilder();
        int r = entryRow;
        int c = entryCol;

        Player.Direction d = (entryCol == 0) ? Player.Direction.EAST : Player.Direction.WEST;

        int steps = 0;
        int maxSteps = totalRows * totalCols * 10;
        while (!(r == exitRow && c == exitCol) && steps < maxSteps) {
            steps++;
            Player.Direction rightDir = d.turnRight();
            int rRight = r + rightDir.deltaRow();
            int cRight = c + rightDir.deltaCol();
            if (isFree(rRight, cRight)) {
                d = rightDir;
                path.append("R");
                r += d.deltaRow();
                c += d.deltaCol();
                path.append("F");
                observable.notifyObservers("Player moved to (" + r + ", " + c + ") facing " + d);
                continue;
            }
            int rForward = r + d.deltaRow();
            int cForward = c + d.deltaCol();
            if (isFree(rForward, cForward)) {
                r = rForward;
                c = cForward;
                path.append("F");
                observable.notifyObservers("Player moved to (" + r + ", " + c + ") facing " + d);
                continue;
            }
            Player.Direction leftDir = d.turnLeft();
            int rLeft = r + leftDir.deltaRow();
            int cLeft = c + leftDir.deltaCol();
            if (isFree(rLeft, cLeft)) {
                d = leftDir;
                path.append("L");
                r += d.deltaRow();
                c += d.deltaCol();
                path.append("F");
                observable.notifyObservers("Player moved to (" + r + ", " + c + ") facing " + d);
                continue;
            }
            d = d.turnRight().turnRight();
            path.append("RR");
            r += d.deltaRow();
            c += d.deltaCol();
            path.append("F");
            observable.notifyObservers("Player moved to (" + r + ", " + c + ") facing " + d);
        }
        if (steps >= maxSteps) {
            observable.notifyObservers("Failed to solve maze using the right-hand rule within the step limit.");
            return null;
        }
        observable.notifyObservers("Maze solved successfully!");
        return path.toString();
    }

    public static String expandPath(String path) {
        String trimmed = path.replaceAll("\\s+", "");
        StringBuilder expanded = new StringBuilder();
        int i = 0;
        while (i < trimmed.length()) {
            char ch = trimmed.charAt(i);
            if (Character.isDigit(ch)) {
                int j = i;
                while (j < trimmed.length() && Character.isDigit(trimmed.charAt(j))) {
                    j++;
                }
                int count = Integer.parseInt(trimmed.substring(i, j));
                if (j < trimmed.length()) {
                    char instruction = trimmed.charAt(j);
                    for (int k = 0; k < count; k++) {
                        expanded.append(instruction);
                    }
                    i = j + 1;
                } else {
                    break;
                }
            } else {
                expanded.append(ch);
                i++;
            }
        }
        return expanded.toString();
    }

    public boolean verifyPath(String inputPath) {
        String canonicalPath = expandPath(inputPath);

        Map<Character, MoveCommand> commandMap = new HashMap<>();
        commandMap.put('L', new TurnLeftCommand());
        commandMap.put('R', new TurnRightCommand());
        commandMap.put('F', new MoveForwardCommand(1));

        Player player = new Player(entryRow, entryCol, (entryCol == 0) ? Player.Direction.EAST : Player.Direction.WEST);

        for (char move : canonicalPath.toCharArray()) {
            MoveCommand command = commandMap.get(move);
            command.execute(player);
            if (!isFree(player.getRow(), player.getCol())) {
                return false;
            }
        }
        return player.getRow() == exitRow && player.getCol() == exitCol;
    }

    public static String factorizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int count = 1;
        char prev = path.charAt(0);
        for (int i = 1; i < path.length(); i++) {
            char curr = path.charAt(i);
            if (curr == prev) {
                count++;
            } else {
                if (count > 1) {
                    result.append(count);
                }
                result.append(prev);
                prev = curr;
                count = 1;
            }
        }
        if (count > 1) {
            result.append(count);
        }
        result.append(prev);
        return result.toString();
    }

    public int getEntryRow() {
        return entryRow;
    }

    public int getExitRow() {
        return exitRow;
    }

    public int getEntryCol() {
        return entryCol;
    }

    public int getExitCol() {
        return exitCol;
    }
}
