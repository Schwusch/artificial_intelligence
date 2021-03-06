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
    private int player;
    private boolean isEndState = false;
    private int depth;

    /**
     * Creates a root node that represents the board currently as it is.
     * @param gridState The state that the board is currently in
     * @param controller The guy we call when something exciting happens
     * @param deadline
     */
    StateNode(int[][] gridState, OthelloController controller, long deadline){
        this.gridState = gridState;
        this.player = OthelloGUI.AI;
        this.depth = 0;
        controller.nodeFound(this.depth);
        findAllStateChanges(controller, deadline);
    }

    /**
     * This is the recursive constructor used internally
     *
     * @param gridState The state that the board is currently in
     * @param player The player about to make a move
     * @param alpha The alpha value that should be used
     * @param beta The beta value that should be used
     * @param depth How many moves we should predict and evaluate
     * @param controller The guy we call when something exciting happens
     */
    private StateNode(int[][] gridState, int player, int alpha, int beta, int depth, OthelloController controller, long deadline){
        this.gridState = gridState;
        this.player = player;
        this.alpha = alpha;
        this.beta = beta;
        this.depth = depth;
        controller.nodeFound(depth);
        findAllStateChanges(controller, deadline);
    }

    /**
     * Get the best calculated move
     *
     * @return The move that will change the world
     */
    OthelloCoordinate getBestMove() {
        return this.bestChange.getMove();
    }

    /**
     * Returns Alpha value if player is AI at this stage, Beta value if Human.
     * If end-node, board score will be returned.
     *
     * @return the value
     */
    int getValue() {
        if(this.isEndState){
            return Utilities.boardScore(this.gridState);
        } else if(this.player == OthelloGUI.AI) {
            return this.alpha;
        } else {
            return this.beta;
        }
    }

    boolean hasMove() {
        return bestChange != null;
    }

    /*
    Find and save all possible state changes, save post state and the move that lead to state change
     */
    private void findAllStateChanges(OthelloController controller, long deadline){
        ArrayList<StateChange> exploredChanges = new ArrayList<>();

        // Check if there is any limitations or we should keep looking
        if(this.shouldKeepLooking(deadline)) {
            boolean[][] possibleMoves = Utilities.findValidMoves(gridState, player);
            int possibleMovesCount = Utilities.numberOfValidMoves(possibleMoves);
            // Loop through matrix to find vacant move spots
            evaluationLoop:
            for (int row = 0; row < OthelloGUI.ROWS; row++) {
                for (int col = 0; col < OthelloGUI.COLS; col++) {
                    if (possibleMoves[row][col]) {
                        // Check how much time we have left to calculate
                        long disposableTime = deadline - System.currentTimeMillis();
                        // Split the time fairly between child nodes
                        long childNodeDeadline =
                                deadline - ((disposableTime / possibleMovesCount) * (possibleMovesCount - 1));

                        StateChange change = createChange(row, col, controller, childNodeDeadline);
                        exploredChanges.add(change);

                        // Now we have one less possible move
                        possibleMovesCount--;
                        // Check if we've found a prunable branch
                        if (prune(change))
                            break evaluationLoop;
                    }
                }
            }
        }
        // Have we perhaps hit a bottom in the search tree?
        if (exploredChanges.size() == 0) {
            this.isEndState = true;
        }
    }

    /*
    Update alpha/beta values and evaluate if it's necessary to keep looking in this branch
     */
    private boolean prune(StateChange change){
        int value = change.getEndNode().getValue();
        if(this.player == OthelloGUI.HUMAN && value <= beta) {
            beta = value;
            bestChange = change;
        } else if (this.player == OthelloGUI.AI && value >= alpha) {
            alpha = value;
            bestChange = change;
        }
        return this.player == OthelloGUI.HUMAN && this.alpha > this.beta ||
                this.player == OthelloGUI.AI && this.alpha < this.beta;
    }

    /*
    Makes a "virtual" move and creates a state change
    This is the recursive part which doesn't return until we've hit a base case,
    which is when a time limit has been reached or all non pruned leafnodes has been found
     */
    private StateChange createChange(int row, int col, OthelloController controller, long childNodeDeadline) {
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
                // Increase depth searched
                this.depth + 1,
                controller,
                // The new nodes deadline
                childNodeDeadline);

        return new StateChange(newState, move);
    }

    /*
    TODO: Implement a time limitation to the algorithm
    UPDATE: It's done, I think. Tell your boss.
     */
    private boolean shouldKeepLooking(long deadline) {
        return System.currentTimeMillis() < deadline;
    }
}
