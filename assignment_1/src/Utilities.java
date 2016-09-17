/**
 * Created by Jonathan Böcker on 2016-09-12.
 */
class Utilities {

    /**
     * Clones a matrix
     * @param src The matrix to be cloned
     * @return The new matrix
     */
    static int[][] cloneMatrix(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    /**
     * Evaluates a move on an Othelloboard and changes bricks according to Othello rules
     *
     * @param start The board as it is when someone made a move
     * @param move The move that was made
     * @param player Human or AI move?
     * @return The board after rules applied
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
            currentCol--;
            while(currentCol >= 0) {
                if(afterMove[currentRow][currentCol] == player) {
                    for(; currentCol < move.getCol(); currentCol++) {
                        afterMove[currentRow][currentCol] = player;
                    }

                    break;
                }

                currentCol--;
            }
            currentCol = move.getCol();

            // Check in east direction
            currentCol++;
            while (currentCol < OthelloGUI.COLS) {
                if(afterMove[currentRow][currentCol] == player) {
                    for(; currentCol > move.getCol(); currentCol--) {
                        afterMove[currentRow][currentCol] = player;
                    }

                    break;
                }
                currentCol++;
            }
            currentCol = move.getCol();

            // Check in the north direction
            currentRow--;
            while (currentRow >= 0) {
                if (afterMove[currentRow][currentCol] == player) {
                    for(;currentRow < move.getRow(); currentRow++) {
                        afterMove[currentRow][currentCol] = player;
                    }
                    break;
                }
                currentRow--;
            }
            currentRow = move.getRow();

            // Check in south direction
            currentRow++;
            while(currentRow < OthelloGUI.ROWS) {
                if (afterMove[currentRow][currentCol] == player) {
                    for (; currentRow > move.getRow(); currentRow--) {
                        afterMove[currentRow][currentCol] = player;
                    }
                    break;
                }
                currentRow++;
            }
            currentRow = move.getRow();

            // Check in north-west direction
            currentCol--;
            currentRow--;
            while ( currentCol >= 0 && currentRow >= 0) {
                if (afterMove[currentRow][currentCol] == player) {
                    for(;currentRow < move.getRow() && currentCol < move.getCol(); currentRow++, currentCol++) {
                        afterMove[currentRow][currentCol] = player;
                    }
                    break;
                }
                currentCol--;
                currentRow--;
            }
            currentRow = move.getRow();
            currentCol = move.getCol();

            // Check in south-west direction
            currentCol--;
            currentRow++;
            while ( currentCol >= 0 && currentRow < OthelloGUI.ROWS) {
                if (afterMove[currentRow][currentCol] == player) {
                    for(; currentRow > move.getRow() && currentCol < move.getCol(); currentRow--, currentCol++) {
                        afterMove[currentRow][currentCol] = player;
                    }
                    break;
                }
                currentCol--;
                currentRow++;
            }
            currentRow = move.getRow();
            currentCol = move.getCol();

            // Check in south-east direction
            currentCol++;
            currentRow++;
            while (currentCol < OthelloGUI.COLS && currentRow < OthelloGUI.ROWS) {
                if (afterMove[currentRow][currentCol] == player) {
                    for(; currentRow > move.getRow() && currentCol > move.getCol(); currentRow--, currentCol--) {
                        afterMove[currentRow][currentCol] = player;
                    }
                    break;
                }
                currentCol++;
                currentRow++;
            }
            currentRow = move.getRow();
            currentCol = move.getCol();

            // Check in north-east direction
            currentCol++;
            currentRow--;
            while (currentCol < OthelloGUI.COLS && currentRow >= 0) {
                if (afterMove[currentRow][currentCol] == player) {
                    for (; currentRow < move.getRow() && currentCol > move.getCol(); currentRow++, currentCol--){
                        afterMove[currentRow][currentCol] = player;
                    }
                    break;
                }
                currentCol++;
                currentRow--;
            }
        }
        return afterMove;
    }
}
