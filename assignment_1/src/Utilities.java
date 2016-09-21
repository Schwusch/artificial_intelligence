import java.math.BigDecimal;

/**
 * Created by Jonathan Böcker on 2016-09-12.
 *
 * This is where i stash methods that doesn't need instantiated object
 */
class Utilities {
    private static final int NORTH = -1;
    private static final int SOUTH = 1;
    private static final int WEST = -1;
    private static final int EAST = 1;
    private static final int NO_DIRECTION = 0;

    /**
     * Checks if there is any vacant spots on the board
     *
     * @param board The board to be checked
     * @return False if there is vacant spots, True if there is not
     */
    static boolean isGameFinished(int[][] board) {

        if(Utilities.hasPossibleMoves(board, OthelloGUI.HUMAN) || Utilities.hasPossibleMoves(board, OthelloGUI.AI))
            return false;

        for (int[] aBoard : board)
            for (int anABoard : aBoard)
                if (anABoard == OthelloGUI.NONE)
                    return false;

        return true;
    }

    /**
     * Returns the board score count
     *
     * @param board The board to be counted
     * @return Negative values if Human has more bricks, positive if AI has more, zero if it is a tie
     */
    static int boardScore(int[][] board) {
        int count = 0;
        for (int[] aBoard : board)
            for (int anABoard : aBoard) count += anABoard;

        return count;
    }

    /**
     * Evaluates the board according to Othello rules and flips bricks accordingly
     *
     * @param start The board before the move
     * @param move The move that was made
     * @param player The player that made the move
     * @return A board after the move was made
     */
    static int[][] calculateBoardChange(int[][] start, OthelloCoordinate move, int player) {
        int[][] afterMove = cloneMatrix(start);
        if(start[move.getRow()][move.getCol()] != OthelloGUI.NONE) {
            System.out.print("Error: making move on a none empty space");

        } else {
            int currentRow = move.getRow();
            int currentCol = move.getCol();

            // Put the player brick down
            afterMove[currentRow][currentCol] = player;

            // Check in west direction
            afterMove = Utilities.lookInDirection(NO_DIRECTION, WEST, move, afterMove, player);
            // Check in east direction
            afterMove = Utilities.lookInDirection(NO_DIRECTION, EAST, move, afterMove, player);
            // Check in the north direction
            afterMove = Utilities.lookInDirection(NORTH, NO_DIRECTION, move, afterMove, player);
            // Check in south direction
            afterMove = Utilities.lookInDirection(SOUTH, NO_DIRECTION, move, afterMove, player);
            // Check in north-west direction
            afterMove = Utilities.lookInDirection(NORTH, WEST, move, afterMove, player);
            // Check in south-west direction
            afterMove = Utilities.lookInDirection(SOUTH, WEST, move, afterMove, player);
            // Check in south-east direction
            afterMove = Utilities.lookInDirection(SOUTH, EAST, move, afterMove, player);
            // Check in north-east direction
            afterMove = Utilities.lookInDirection(NORTH, EAST, move, afterMove, player);
        }
        return afterMove;
    }

    /**
     * Helper method to make floats look better
     */
    static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    static boolean[][] findValidMoves(int[][] grid, int player) {
        boolean[][] validMoves = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                validMoves[i][j] = grid[i][j] == OthelloGUI.NONE && isValidMove(grid, i, j, player);

        return validMoves;
    }

    /**
     * Counts number of boolean true values in a boolean matrix
     * @param grid
     * @return number of trues
     */
    static int numberOfValidMoves(boolean[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                if(grid[i][j]) count++;

        return count;
    }

    /**
     * Checks if a grid/board has any possible moves for a given player
     * @param grid
     * @param player OthelloGUI.HUMAN or OthelloGUI.AI
     * @return basically yes or no
     */
    static boolean hasPossibleMoves(int[][] grid, int player){
        boolean[][] validMoves = findValidMoves(grid, player);
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                if(validMoves[i][j])
                    return true;
        return false;
    }

    /*
    Checks if a coordinate is ok to play for a given player on a given board/grid
     */
    private static boolean isValidMove(int[][] grid, int row, int col, int player){
        // Checks all 8 possible directions
        return Utilities.possibleDirection(NO_DIRECTION, WEST, row, col, grid, player) ||
                Utilities.possibleDirection(NO_DIRECTION, EAST, row, col, grid, player) ||
                Utilities.possibleDirection(NORTH, NO_DIRECTION, row, col, grid, player) ||
                Utilities.possibleDirection(SOUTH, NO_DIRECTION, row, col, grid, player) ||
                Utilities.possibleDirection(NORTH, WEST, row, col, grid, player) ||
                Utilities.possibleDirection(SOUTH, WEST, row, col, grid, player) ||
                Utilities.possibleDirection(SOUTH, EAST, row, col, grid, player) ||
                Utilities.possibleDirection(NORTH, EAST, row, col, grid, player);
    }

    /*
    Checks if a move in a certain given row and column is possible
    in a certain direction(deltaRow, deltaCol) for a certain player on a certain board/grid
     */
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
        while (currentCol < OthelloGUI.COLS && currentCol >= 0 && currentRow < OthelloGUI.ROWS && currentRow >= 0) {
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

    /*
    Checks if there is bricks to be flipped in a given direction and flips accordingly to Othello rules
     */
    private static int[][] lookInDirection(int deltaRow, int deltaCol, OthelloCoordinate move, int[][] grid, int player) {
        int currentRow = move.getRow();
        int currentCol = move.getCol();

        currentCol += deltaCol;
        currentRow += deltaRow;
        while (currentCol < OthelloGUI.COLS && currentCol >= 0 && currentRow < OthelloGUI.ROWS && currentRow >= 0) {
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

    /*
    Simply clones a matrix
     */
    private static int[][] cloneMatrix(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }
}
