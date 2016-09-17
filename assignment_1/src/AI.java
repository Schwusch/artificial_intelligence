import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
class AI {
    private StateNode startNode;

    OthelloCoordinate getNextMove(int[][] grid) {
        this.startNode = new StateNode(grid, OthelloGUI.AI);
        return startNode.getBestMove();
    }
}
