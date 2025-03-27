package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Maze Runner");

        Options options = new Options();
        options.addOption("i", true, "Maze input file path");
        options.addOption("p", true, "Path sequence for verification");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Parsing error: " + e.getMessage());
            return;
        }

        if (!cmd.hasOption("i")) {
            logger.error("Maze input file not specified.");
            return;
        }

        String inputFile = cmd.getOptionValue("i");
        logger.info("Reading the maze from file: " + inputFile);
        Maze maze = new Maze(inputFile);
        //maze.displayMaze();

        if (cmd.hasOption("p")) {
            String pathSequence = cmd.getOptionValue("p");
            boolean valid = maze.verifyPath(pathSequence);
            System.out.println(valid ? "correct path" : "incorrect path");
        } else {
            String canonicalPath = maze.computePath();
            if (canonicalPath == null) {
                System.out.println("PATH NOT COMPUTED");
            } else {
                String factoredPath = Maze.factorizePath(canonicalPath);
                System.out.println(factoredPath);
            }
        }

        logger.info("End of MazeRunner");
    }
}
