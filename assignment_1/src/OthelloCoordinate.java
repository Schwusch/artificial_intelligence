/**
 * Created by Jonathan Böcker on 2016-09-09.
 *
 * A perhaps unnecessary class holding a matrix position.
 * An interminably more object-oriented approach than an int array!
 */
class OthelloCoordinate {
    private int row;
    private int col;

    /**
     * You might have guessed... but this creates an Othello coordinate
     *
     * @param row Can also be described as a Y position
     * @param col Can alsa be described as an X position
     */
    OthelloCoordinate(int row, int col){
        this.col = col;
        this.row = row;
    }

    /**
     * I'm not even going to comment this one...
     * But I just did?
     *
     * @return an int representing the coordinate describing the horizontal position in a 2D plane.
     */
    int getCol(){
        return this.col;
    }

    /**
     * Dito
     *
     * @return dito except the horizontal part.
     */
    int getRow(){
        return this.row;
    }
}
