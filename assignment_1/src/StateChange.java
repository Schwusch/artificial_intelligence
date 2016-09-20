/**
 * Created by Jonathan Böcker on 2016-09-12.
 *
 * Data structure remembering an initial Othello state, a move, and the resulting state
 */
class StateChange {
    private StateNode endNode;
    private OthelloCoordinate move;

    StateChange(StateNode endNode, OthelloCoordinate move) {
        this.endNode = endNode;
        this.move = move;
    }

    StateNode getEndNode(){
        return endNode;
    }

    OthelloCoordinate getMove(){
        return move;
    }
}
