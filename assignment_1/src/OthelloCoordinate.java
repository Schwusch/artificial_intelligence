/**
 * Created by Jonathan Böcker on 2016-09-09.
 */
class OthelloCoordinate {
    private int row;
    private int col;

    OthelloCoordinate(int row, int col){
        this.col = col;
        this.row = row;
    }

    int getCol(){
        return this.col;
    }

    int getRow(){
        return this.row;
    }
}
