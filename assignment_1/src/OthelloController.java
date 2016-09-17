import javax.swing.*;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
class OthelloController {
    private OthelloGUI gui;
    private int[][] grid = new int[OthelloGUI.ROWS][OthelloGUI.COLS];
    private AI ai = new AI();

    OthelloController(){
        this.gui = new OthelloGUI("test", this);
        this.gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.gui.pack();
        this.gui.setVisible(true);

        grid[1][1] = OthelloGUI.HUMAN;
        grid[2][2] = OthelloGUI.HUMAN;
        grid[1][2] = OthelloGUI.AI;
        grid[2][1] = OthelloGUI.AI;
        this.gui.setGrid(grid);
    }

    void buttonPressed(OthelloCoordinate coord) {
        this.grid = Utilities.calculateBoardChange(this.grid, coord, OthelloGUI.HUMAN);
        OthelloCoordinate computerMove = ai.getNextMove(grid);
        this.grid[computerMove.getRow()][computerMove.getCol()] = OthelloGUI.AI;
        this.gui.setGrid(grid);
    }

    OthelloCoordinate calculateComputerMove(){
        // TODO make it a little smarter
        Random rand = new Random();
        int row;
        int col;
        do {
            row = rand.nextInt(4);
            col = rand.nextInt(4);
        } while (this.grid[row][col] != 0);

        return new OthelloCoordinate(row, col);
    }
}
