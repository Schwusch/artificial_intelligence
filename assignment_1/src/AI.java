import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
class AI {
    private StateNode startNode;

    /**
     * Applies a minimax algorithm with alpha-beta pruning to find the best move
     * @param grid The board state after a human move has been made
     * @return A calculated move
     */
    OthelloCoordinate getNextMove(int[][] grid) {
        this.startNode = new StateNode(grid, OthelloGUI.AI);
        return startNode.getBestMove();
    }
}
