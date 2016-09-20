/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
class AI {
    private StateNode rootNode;

    /**
     * Applies a minimax algorithm with alpha-beta pruning to find the best move
     * @param grid The board state after a human move has been made
     * @return A calculated move
     */
    OthelloCoordinate getNextMove(int[][] grid, OthelloController controller) {
        return new StateNode(grid, OthelloGUI.AI, controller).getBestMove();
    }
}
