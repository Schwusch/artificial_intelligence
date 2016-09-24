Introduction
============

This assignment consists of implementing the alpha-beta pruning search algorithm and heuristic evaluation functions for an Othello AI opponent. I chose to implement proper Othello rules and expand the board area to an 8 by 8 grid to make it a little more challenging. I also chose to make the search algorithm with an iterative deepening search, making it time dependent instead. This makes the algorithm ‘smarter’ the faster the host CPU is.

Building the search algorithm
=============================

I chose to represent the search nodes as states, that holds a snapshot of all important variables at the time of evaluation. It is important to understand this class in order to understand the search algorithm:

``` java
class StateNode {
    private int[][] gridState;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
    private int player;
    private boolean isEndState = false;
    private int depth;
    private StateChange bestChange;
}
```

-   The `gridState` variable is an integer matrix where values span from -1 to 1, where -1 represents a human player brick, 1 represents an AI brick and 0 represents an unoccupied space. This is an application wide representation the Othello board.

-   The `alpha` and `beta` variables are important to the pruning algorithm at every node to evaluate the need of searching further down in the current branch.

-   The `player` int variable is either 1(AI) or -1(Human) and tells the algorithm which player is about to make the next virtual draw in this node.

-   The `isEndState` boolean variable is used to determine if a total board score is relevant. This is important when the algorithm needs to know if the node is a leafnode e.g. when the algorithm isn’t allowed to search further due to time constraints.

-   The `depth` int variable is only for requirements sake. It keeps track of current search depth, making it possible to graphically display the search depth for the user.

-   The last instance variable `bestChange` is an object with the following specifications:

``` java
class StateChange {
    private StateNode endNode;
    private OthelloCoordinate move;
}
```

Its purpose is to keep track of the, according to the search algorithm, best state change and by extension the best next draw for this node. This is most useful for the root node when asked to provide the next AI draw. In this context the draw is an object named move, simply holding a row and a column value in the board matrix as follows:

``` java
class OthelloCoordinate {
    private int row;
    private int col;
}
```

`StateNode` has one package-private constructor which is called when the application controller wants a calculated draw from the AI:

``` java
StateNode(int[][] gridState, OthelloController controller, long deadline){
      this.gridState = gridState;
      this.player = OthelloGUI.AI;
      this.depth = 0;
      controller.nodeFound(this.depth);
      findAllStateChanges(controller, deadline);
}
```

The constructor passes on the `OthelloController` to the recursive method `findAllStateChanges()` after it has reported itself as a found node at depth 0. One of the first things done in `findAllStateChanges()` is checking if it should attempt to find more nodes:

``` java
private void findAllStateChanges(OthelloController controller, long deadline){
      ArrayList<StateChange> exploredChanges = new ArrayList<>();
      // Check if there is any limitations or we should keep looking
      if(this.shouldKeepLooking(deadline)) {
      ...
```

The method `shouldKeepLooking()` has the purpose of limiting the search depth according to some criteria, in this case it is a time limit:

``` java
private boolean shouldKeepLooking(long deadline) {
        return System.currentTimeMillis() < deadline;
}
```

If `shouldKeepLooking()` returns true, `findAllStateChanges()` will continue to take all possible draws and create a new `StateNode` for each one using the private constructor solely for recursive calls:

``` java
private StateNode(int[][] gridState, int player, int alpha, int beta, int depth, OthelloController controller, long deadline)
```

The private constructor requires a little more information. The `player` variable oscillates between Human or AI between every recursive call in order for the pruning algorithm to decide whether it is a prunable branch or not. The player variable also controls if alpha or beta is passed back to the parent node when asked for its value.

The pruning condition
=====================

When looping through the matrix and exploring nodes in the current tree branch, there might be no need to search further. Towards the end of the `findAllStateChanges()` evaluation loop, the algorithm checks and perhaps updates the alpha and beta values as follows:

``` java
private void findAllStateChanges(OthelloController controller, long deadline){
...
// Check if we've found a prunable branch
  if (prune(change))
      break evaluationLoop;
```

It makes a call to `prune()` with a calculated state change as the argument, which returns `true` or `false` whether to cancel the search for more nodes. The `prune()` method looks as follows:

``` java
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
```

The pruning algorithms reasoning is that if the current node is AI, the child node must be human. If that is so, it wants to see if the child nodes beta value is larger than its own alpha value. Because if it is, that path of draws is the most favorable for the moment, so it saves that draw and beta value as its alpha value until it finds a better one.

In the other way around, when the current node is human and child node is AI, it checks if its child nodes alpha value is smaller than its beta. If that is true it updates its beta to the child nodes alpha and saves the draw as the most favorable draw. If the child node is a leaf node, it simply compares with the board score count.

Lastly, the condition that decides whether there is reason to keep looking in that branch, is if the current node is AI and its alpha is larger than its beta. If that is true there is no point in looking further into that branch. In the other way around, if the node is human and its alpha is smaller than its beta, there is no reason to continue in that branch.

The evaluation functions
========================

There are several things to be evaluated in the Othello Game. The easiest and most crucial is the board score, which is designed for the AI perspective. A positive score means that the AI has more bricks on the board, a negative score means that the human player has more bricks and a board score of zero means that the brick count is even. The static function for calculating the board score is a simple nested for-loop that sums all integers in an integer matrix representing the Othello board:

``` java
static int boardScore(int[][] board) {
      int count = 0;
      for (int[] aBoard : board)
          for (int anABoard : aBoard) count += anABoard;
      return count;
}
```

This is possible due to the player representation I have chosen, where an AI brick is the integer 1, and a human brick is -1. An unoccupied space is simply 0. This method is called in the `getValue()` method, which is called by the `prune()` method. The `getValue()` method looks as follows:

``` java
int getValue() {
        if(this.isEndState){
            return Utilities.boardScore(this.gridState);
        } else if(this.player == OthelloGUI.AI) {
            return this.alpha;
        } else {
            return this.beta;
        }
}
```

As stated in the pruning section, the algorithm makes some assumptions. If the current node is AI, the parent node most probably wants the alpha value. Similarly if the current node is human, the parent node wants the beta value. Leafnodes stands out, since there is no point in returning alpha or beta. A leafnode represents an endgame and simply returns the boardscore

How does the node know if it is a leafnode you might ask? Well, it might not be the end of the game at all in the current node. It might pose as a leafnode if it is not allowed to search further due to time constraints. It might be an endgame even if not all cells in the grid are occupied, if neither the AI or the human player are able to make a move.

That leads us in to another evaluation function, `findValidMoves()`. It takes two arguments, the board matrix and the player about to make a move. It returns a boolean matrix where `true` values means there is a valid move. It loops through the matrix, evaluating every position which is unoccupied, through calling the `isValidMove()` function. `isValidMove()` checks in all 8 legal directions through calling the `possibleDirection()` function 8 times with different arguments.

`possibleDirection()` is a somewhat complex function generalizing a direction check as follows:

``` java
private static boolean possibleDirection(int deltaRow, int deltaCol, int row, int col, int[][] grid, int player) {
        int currentRow = row;
        int currentCol = col;

        if(currentRow + deltaRow < grid.length &&
                currentRow + deltaRow >= 0 &&
                currentCol + deltaCol < grid[0].length &&
                currentCol + deltaCol >= 0 &&
                grid[currentRow + deltaRow][currentCol + deltaCol] != -player) {
            return false;
        }

        currentCol += 2 * deltaCol;
        currentRow += 2 * deltaRow;

        while (currentCol < OthelloGUI.COLS && currentCol >= 0 &&
                    currentRow < OthelloGUI.ROWS && currentRow >= 0) {
            if(grid[currentRow][currentCol] == OthelloGUI.NONE ) {
               return false;
            } else if (grid[currentRow][currentCol] == player) {
                return true;
            }
            currentCol += deltaCol;
            currentRow += deltaRow;
        }

        return false;
}
```

The `deltaRow` and `deltaCol` parameters decides how big of a step, row-wise and column-wise, the function should take every iteration in its while loop. The `row` and `col` parameters are the draw to be examined, `grid` and `player` are self-explanatory.

Before anything else, the function checks to see if the intended draw has an opponent brick next to it, one `deltaCol` and `deltaRow` away(line 5-11). If it does not, it is not a valid move and the function returns `false` immediately. If it does, it starts iterating in that direction to see if there is a brick belonging to the intended player further away, with no unoccupied spaces inbetween(line 16-25). If it finds such a brick, it is indeed a valid draw in that particular direction and the function returns `true`(line 21). Although, if it finds an unoccupied space before finding a player brick, it is not a valid move and returns `false`(line 19).

Lastly, there is only one more important evaluation function. It takes a draw that is to be made and flips as many bricks as possible on an Othello board according to Othello rules. The function is:

``` java
static int[][] calculateBoardChange(int[][] start, OthelloCoordinate move, int player)
```

It is very similar to `isValidMove()` in that it checks all 8 legal directions for bricks to flip. It assumes it is a valid move to begin with and puts down the player brick before checking all the directions. It calls a similar, but different function than `isValidMove()` does, called `lookInDirection()`.

``` java
private static int[][] lookInDirection(int deltaRow, int deltaCol,
        OthelloCoordinate move, int[][] grid, int player) {
    int currentRow = move.getRow() + deltaRow;
    int currentCol = move.getCol() + deltaCol;

    while (currentCol < OthelloGUI.COLS && currentCol >= 0 &&
              currentRow < OthelloGUI.ROWS && currentRow >= 0) {
        if(grid[currentRow][currentCol] == OthelloGUI.NONE) {
            break;
        } else if (grid[currentRow][currentCol] == player) {
            while (currentCol != move.getCol() || currentRow != move.getRow()) {
                grid[currentRow][currentCol] = player;

                currentCol -= deltaCol;
                currentRow -= deltaRow;
            }
            break;
        }
        currentCol += deltaCol;
        currentRow += deltaRow;
    }
    return grid;
}
```

It starts with looking in the `deltaRow` and `deltaCol` direction for a `player` brick, and aborts if it finds an unoccupied cell(line 8-9). When it finds a `player` brick, it enters a nested while-loop, iterating in the opposite direction and turning all bricks into `player` bricks until it is back to the original draw coordinate. It then breaks the outer while-loop, because the function has done what it is supposed to.
