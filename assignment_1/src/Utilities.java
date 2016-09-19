/**
 * Created by Jonathan Böcker on 2016-09-12.
 */
class Utilities {
    static int[][] cloneArray(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    static Boolean isGameFinished(int[][] board) {
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if(board[i][j] == OthelloGUI.NONE)
                    return false;

        return true;
    }

    static int boardScore(int[][] board) {
        int count = 0;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                count += board[i][j];

        return count;
    }

    static int[][] calculateBoardChange(int[][] start, OthelloCoordinate move, int player) {
        int[][] afterMove = cloneArray(start);
        if(start[move.getRow()][move.getCol()] != OthelloGUI.NONE) {
            System.out.print("Error: making move on a none empty space");

        } else {
            int currentRow = move.getRow();
            int currentCol = move.getCol();

            // Put the player brick down
            afterMove[currentRow][currentCol] = player;

            // Check in west direction
            afterMove = Utilities.lookInDirection(0, -1, move, afterMove, player);

            // Check in east direction
            afterMove = Utilities.lookInDirection(0, 1, move, afterMove, player);

            // Check in the north direction
            afterMove = Utilities.lookInDirection(-1, 0, move, afterMove, player);

            // Check in south direction
            afterMove = Utilities.lookInDirection(1, 0, move, afterMove, player);

            // Check in north-west direction
            afterMove = Utilities.lookInDirection(-1, -1, move, afterMove, player);

            // Check in south-west direction
            afterMove = Utilities.lookInDirection(1, -1, move, afterMove, player);

            // Check in south-east direction
            afterMove = Utilities.lookInDirection(1, 1, move, afterMove, player);

            // Check in north-east direction
            afterMove = Utilities.lookInDirection(-1, 1, move, afterMove, player);
        }
        return afterMove;
    }

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
}
