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
    private StateChange worstChange;
    private ArrayList<StateChange> possibleChanges;
    private int player;
    private boolean isEndState = false;


    /**
     *
     * @param gridState The state that the board is currently in
     * @param player The player about to make a move
     */
    StateNode(int[][] gridState, int player){
        this.gridState = gridState;
        this.player = player;
        findAllChanges();
    }


    /**
     * If this state will be a child node, this constructor is used
     *
     * @param gridState The state that the board is currently in
     * @param player The player about to make a move
     */
    StateNode(int[][] gridState, int player, int alpha, int beta){
        this.gridState = gridState;
        this.player = player;
        this.alpha = alpha;
        this.beta = beta;
        findAllChanges();
    }

    OthelloCoordinate getBestMove() {
        return this.bestChange.getMove();
    }

    boolean isEndState(){
        return this.isEndState;
    }

    /**
     * Returns the board in this states point count.
     * Zero is a draw,
     * negative values means the AI has more bricks,
     * positive values means the human player has more bricks.
     *
     * @return The point count
     */
    int pointCount(){
        int points = 0;
        for(int row = 0; row < OthelloGUI.ROWS; row++) {
            for (int col = 0; col < OthelloGUI.COLS; col++) {
                points += gridState[row][col];
            }
        }
        return points;
    }

    StateChange getBestChange() {
        return this.bestChange;
    }

    StateChange getWorstChange() {
        return this.worstChange;
    }

    /*
    Find and save all possible state changes, save pre and post state and the move that lead to state change
     */
    private void findAllChanges(){
        this.possibleChanges = new ArrayList<>();

        // Loop through matrix to find vacant move spots
        outerloop:
        for(int row = 0; row < OthelloGUI.ROWS; row++) {
            for(int col = 0; col < OthelloGUI.COLS; col++) {
                if(this.gridState[row][col] == OthelloGUI.NONE) {
                    this.possibleChanges.add(createChange(row, col));
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
    private StateChange createChange(int row, int col) {
        OthelloCoordinate move = new OthelloCoordinate(row, col);
        int[][] newGridState = Utilities.calculateBoardChange(gridState, move, player);
        
        // Next State is created
        StateNode newState = new StateNode(
                // Next grid will look different after the move
                newGridState,
                // Next move is the other players turn
                -this.player,
                // Pass on alpha and beta
                this.alpha,
                this.beta);

        this.possibleChanges.add(new StateChange(this, newState,move));

        if (newState.isEndState()) {
            int points = newState.pointCount();
            if (points > this.alpha && this.player == OthelloGUI.AI) {
                this.alpha = points;
                // TODO Update alpha/beta and so on
            } else if (points < this.beta && this.player == OthelloGUI.HUMAN) {
                this.beta = points;
                // TODO Update alpha/beta and so on
            }

            // Check if we've found a branch worth pruning
            return new StateChange(this, newState, move);
        }

        // If not an endstate, check if it has resources to keep looking
        return null;
    }

    /*
    TODO: Implement a time and search depth limitation to the algorithm
     */
    private boolean shouldKeepLooking() {
        return true;
    }
}
