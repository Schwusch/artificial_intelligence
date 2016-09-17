/**
 * Created by Jonathan Böcker on 2016-09-12.
 *
 * Data structure remembering an initial Othello state, a move, and the resulting state
 */
class StateChange {
    private StateNode startNode, endNode;
    private OthelloCoordinate move;

    StateChange(StateNode startNode, StateNode endNode, OthelloCoordinate move) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.move = move;
    }

    StateNode getStartNode(){
        return startNode;
    }

    StateNode getEndNode(){
        return endNode;
    }

    OthelloCoordinate getMove(){
        return move;
    }
}
