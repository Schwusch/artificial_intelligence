import javax.swing.*;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
public class OthelloController {
    private OthelloGUI gui;
    private int[][] grid = new int[OthelloGUI.ROWS][OthelloGUI.COLS];
    private AI ai = new AI();

    public OthelloController(){
        this.gui = new OthelloGUI("test", this);
        this.gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.gui.pack();
        this.gui.setVisible(true);
    }

    public void buttonPressed(OthelloCoordinate coord) {
        grid[coord.getRow()][coord.getCol()] = OthelloGUI.HUMAN;
        OthelloCoordinate computerMove = ai.getNextMove(grid);
        grid[computerMove.getRow()][computerMove.getCol()] = OthelloGUI.AI;
        gui.setGrid(grid);
    }

    public OthelloCoordinate calculateComputerMove(){
        // TODO make it a little smarter
        Random rand = new Random();
        int row;
        int col;
        do {
            row = rand.nextInt(4);
            col = rand.nextInt(4);
        } while (grid[row][col] != 0);

        return new OthelloCoordinate(row, col);
    }
}
