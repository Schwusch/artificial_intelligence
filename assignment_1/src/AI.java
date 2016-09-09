import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
public class AI {

    OthelloCoordinate getNextMove(int[][] grid) {
        return decideMove(getPossibleMoves(grid));
    }

    private OthelloCoordinate decideMove(ArrayList<OthelloCoordinate> possibleMoves) {
        //TODO make a little smarter algorithm
        Random rand = new Random();
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }

    private ArrayList<OthelloCoordinate> getPossibleMoves(int[][] grid) {
        ArrayList<OthelloCoordinate> possibleMoves = new ArrayList<>();
        for(int row = 0; row < OthelloGUI.ROWS; row++) {
            for(int col = 0; col < OthelloGUI.COLS; col++) {
                if(grid[row][col] == OthelloGUI.NONE) {
                    possibleMoves.add(new OthelloCoordinate(row, col));
                }
            }
        }

        return possibleMoves;
    }
}
