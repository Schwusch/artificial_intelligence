import javax.swing.*;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
class OthelloController {
    private OthelloGUI gui;
    private int[][] grid = new int[OthelloGUI.ROWS][OthelloGUI.COLS];
    private AI ai = new AI();

    /**
     * Initializes a GUI with an Othelloboard
     */
    OthelloController(){
        // Make an interface
        this.gui = new OthelloGUI("test", this);
        this.gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.gui.pack();
        this.gui.setVisible(true);

        // Initialize the board with 4 bricks
        grid[1][1] = OthelloGUI.HUMAN;
        grid[2][2] = OthelloGUI.HUMAN;
        grid[1][2] = OthelloGUI.AI;
        grid[2][1] = OthelloGUI.AI;
        // Update the GUI
        this.gui.setGrid(grid);
    }

    /**
     * When the human player presses a button, the AI has to figure out the best next move
     * @param coord The board coordinate pressed by human
     */
    void buttonPressed(OthelloCoordinate coord) {
        // Flips a few bricks if possible and update the GUI board
        this.grid = Utilities.calculateBoardChange(this.grid, coord, OthelloGUI.HUMAN);
        this.gui.setGrid(grid);

        // AI finds the best possible counter move
        OthelloCoordinate computerMove = ai.getNextMove(grid);
        this.grid[computerMove.getRow()][computerMove.getCol()] = OthelloGUI.AI;
        this.gui.setGrid(grid);
    }
}
