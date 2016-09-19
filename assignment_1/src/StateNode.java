import java.util.ArrayList;

/**
 * Created by Jonathan Böcker on 2016-09-12.
 *
 * A somewhat complex data structure, similar to a tree and weighted graph with unlimited and unsorted child nodes.
 * Keeps track of the edges(moves) and vertexes(statenodes) in an {@link ArrayList} of {@link StateChange} objects.
 * On constructing the object, it recursively calculates the best state change(move). This may take a considerable
 * amount of time and resources.
 */
class StateNode {
    private int[][] gridState;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
    private StateChange bestChange;
    private ArrayList<StateChange> possibleChanges;
    private int player;
    private boolean isEndState = false;
    private int depth;

    /**
     *
     * @param gridState The state that the board is currently in
     * @param player The player about to make a move
     */
    StateNode(int[][] gridState, int player, OthelloController controller){
        this.gridState = gridState;
        this.player = player;
        this.depth = 0;
        controller.nodeFound();
        findAllChanges(controller);
    }

    /**
     * If this state will be a child node, this constructor is used
     *
     * @param gridState The state that the board is currently in
     * @param player The player about to make a move
     */
    private StateNode(int[][] gridState, int player, int alpha, int beta, int depth, OthelloController controller){
        this.gridState = gridState;
        this.player = player;
        this.alpha = alpha;
        this.beta = beta;
        this.depth = depth;
        controller.nodeFound();
        findAllChanges(controller);
    }

    OthelloCoordinate getBestMove() {
        return this.bestChange.getMove();
    }

    /**
     * Returns the board in this states point count.
     * Zero is a draw,
     * negative values means the AI has more bricks,
     * positive values means the human player has more bricks.
     *
     * @return The point count
     */
    private int pointCount(){
        int points = 0;
        for(int row = 0; row < OthelloGUI.ROWS; row++) {
            for (int col = 0; col < OthelloGUI.COLS; col++) {
                points += gridState[row][col];
            }
        }
        return points;
    }

    int getValue() {
        if(this.isEndState) {
            return pointCount();
        } else if(this.player == OthelloGUI.AI) {
            return alpha;
        } else {
            return beta;
        }
    }

    /*
    Find and save all possible state changes, save pre and post state and the move that lead to state change
     */
    private void findAllChanges(OthelloController controller){
        this.possibleChanges = new ArrayList<>();

        if(this.shouldKeepLooking()) {
            // Loop through matrix to find vacant move spots
            pruningLoop:
            for (int row = 0; row < OthelloGUI.ROWS; row++) {
                for (int col = 0; col < OthelloGUI.COLS; col++) {
                    if (this.gridState[row][col] == OthelloGUI.NONE) {
                        StateChange change = createChange(row, col, controller);
                        this.possibleChanges.add(change);

                        if(this.player == OthelloGUI.HUMAN && change.getEndNode().getValue() < beta) {
                            beta = change.getEndNode().getValue();
                            bestChange = change;

                        } else if (this.player == OthelloGUI.AI && change.getEndNode().getValue() > alpha) {
                            alpha = change.getEndNode().getValue();
                            bestChange = change;

                        }
                        // Check if we've found a prunable node
                        if (this.player == OthelloGUI.HUMAN && alpha >= beta){
                            break pruningLoop;
                        }

                    }
                }
            }
        }

        if (this.possibleChanges.size() == 0) {
            this.isEndState = true;
        }
    }

    /*
    Creates a StateChange object and updates the alpha/beta variables if applicable
     */
    private StateChange createChange(int row, int col, OthelloController controller) {
        OthelloCoordinate move = new OthelloCoordinate(row, col);
        int[][] newGridState = Utilities.calculateBoardChange(this.gridState, move, this.player);
        
        // Next State is created
        StateNode newState = new StateNode(
                // Next grid will look different after the move
                newGridState,
                // Next move is the other players turn
                -this.player,
                // Pass on alpha and beta
                this.alpha,
                this.beta,
                this.depth + 1,
                controller);

        return new StateChange(this, newState, move);
    }

    /*
    TODO: Implement a time and search depth limitation to the algorithm
     */
    private boolean shouldKeepLooking() {

        return this.depth != StartOthello.SEARCH_DEPTH;
    }
}
