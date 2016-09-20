import javax.swing.*;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 *
 * This class is the glue between the GUI and the AI/Othello logic.
 */
class OthelloController {
    private OthelloGUI gui;
    private int[][] grid = new int[OthelloGUI.ROWS][OthelloGUI.COLS];
    private int nodesExamined = 0;

    /**
     * Initializes a GUI with an Othelloboard
     */
    OthelloController(){
        // Initialize the board with 4 bricks
        grid[1][1] = OthelloGUI.HUMAN;
        grid[2][2] = OthelloGUI.HUMAN;
        grid[1][2] = OthelloGUI.AI;
        grid[2][1] = OthelloGUI.AI;
        // Make an interface
        SwingUtilities.invokeLater(() -> {
            gui = new OthelloGUI("Othello", OthelloController.this);
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            gui.pack();
            gui.setVisible(true);
            // Update the GUI
            gui.setGrid(grid, false);
        });

    }

    /**
     * Increases the node count and updates the GUI every 1000nd node
     */
    void nodeFound() {
        this.nodesExamined++;
        if(this.nodesExamined % 1000 == 0)
            SwingUtilities.invokeLater(() -> gui.updateNodeCount(nodesExamined));
    }

    /**
     * When the human player presses a button, the AI has to figure out the best next move
     * @param coord The board coordinate pressed by human
     */
    void buttonPressed(OthelloCoordinate coord) {
        // Reset the node count
        this.nodesExamined = 0;
        // Flips a few bricks if possible and update the GUI board
        this.grid = Utilities.calculateBoardChange(this.grid, coord, OthelloGUI.HUMAN);
        SwingUtilities.invokeLater(() -> gui.setGrid(grid, true));

        // AI finds the best possible counter move and flips a lot of bricks if possible :)
        OthelloCoordinate computerMove = new StateNode(grid, OthelloGUI.AI, this).getBestMove();
        this.grid = Utilities.calculateBoardChange(this.grid, computerMove, OthelloGUI.AI);

        // Update GUI stuff
        SwingUtilities.invokeLater(() -> {
                    gui.setGrid(grid, false);
                    gui.stopTimer();
                    gui.updateNodeCount(nodesExamined);
                });

        // Check if the game has been finished and if it has, who is the winner
        if(Utilities.isGameFinished(this.grid)) {
            int score = Utilities.boardScore(this.grid);
            String info;
            if (score > 0)
                info = "You lost to a machine.";
            else if (score < 0)
                info = "You beat my Othello bot :(";
            else
                info = "Tie! How inconvenient.";
            // Update GUI message
            SwingUtilities.invokeLater(() -> gui.updateInfo(info));
        }
    }
}
