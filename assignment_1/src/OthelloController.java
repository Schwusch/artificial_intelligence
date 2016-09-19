import javax.swing.*;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
class OthelloController {
    private OthelloGUI gui;
    private int[][] grid = new int[OthelloGUI.ROWS][OthelloGUI.COLS];
    private AI ai = new AI();
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new OthelloGUI("test", OthelloController.this);
                gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                gui.pack();
                gui.setVisible(true);
                // Update the GUI
                gui.setGrid(grid, false);
            }
        });

    }

    void nodeFound() {
        this.nodesExamined++;
        SwingUtilities.invokeLater(() -> gui.updateNodeCount(nodesExamined));
    }

    /**
     * When the human player presses a button, the AI has to figure out the best next move
     * @param coord The board coordinate pressed by human
     */
    void buttonPressed(OthelloCoordinate coord) {
        this.nodesExamined = 0;
        // Flips a few bricks if possible and update the GUI board
        this.grid = Utilities.calculateBoardChange(this.grid, coord, OthelloGUI.HUMAN);
        SwingUtilities.invokeLater(() -> gui.setGrid(grid, true));


        // AI finds the best possible counter move
        OthelloCoordinate computerMove = ai.getNextMove(grid, this);
        this.grid = Utilities.calculateBoardChange(this.grid, computerMove, OthelloGUI.AI);
        SwingUtilities.invokeLater(() -> gui.setGrid(grid, false));

        if(Utilities.isGameFinished(this.grid)) {
            int score = Utilities.boardScore(this.grid);
            if (score > 0)
                SwingUtilities.invokeLater(() -> gui.updateInfo("You lost to a machine."));
            else if (score < 0)
                SwingUtilities.invokeLater(() -> gui.updateInfo("You beat my Othello bot :("));
            else
                SwingUtilities.invokeLater(() -> gui.updateInfo("Tie! How inconvenient."));
        }
    }
}
