package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maze {

    private static final Logger logger = LogManager.getLogger(Maze.class);

    private String inputPath;
    private char[][] grid;
    private int totalRows;
    private int totalCols;

    private int entryRow, entryCol;
    private int exitRow, exitCol;

    public Maze(String inputPath) {
        this.inputPath = inputPath;
        processMaze();
    }

    private void processMaze() {
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

    private void findEntryAndExit() {
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
                continue;
            }
            int rForward = r + d.deltaRow();
            int cForward = c + d.deltaCol();
            if (isFree(rForward, cForward)) {
                r = rForward;
                c = cForward;
                path.append("F");
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
                continue;
            }
            d = d.turnRight().turnRight();
            path.append("RR");
            r += d.deltaRow();
            c += d.deltaCol();
            path.append("F");
        }
        if (steps >= maxSteps) {
            logger.error("Failed to solve maze using the right-hand rule within the step limit.");
            return null;
        }
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

        int r = entryRow;
        int c = entryCol;
        Player.Direction d = (entryCol == 0) ? Player.Direction.EAST : Player.Direction.WEST;

        for (int i = 0; i < canonicalPath.length(); i++) {
            char move = canonicalPath.charAt(i);
            if (move == 'L') {
                d = d.turnLeft();
            } else if (move == 'R') {
                d = d.turnRight();
            } else if (move == 'F') {
                int newRow = r + d.deltaRow();
                int newCol = c + d.deltaCol();
                if (!isFree(newRow, newCol)) {
                    return false;
                }
                r = newRow;
                c = newCol;
            }
        }
        return (r == exitRow && c == exitCol);
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
}
