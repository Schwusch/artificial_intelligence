import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 *
 * A class extending JButton to remember its position on an Othello board
 * It has a satisfactory orange color, pleasing the human eye, caressing the soul.
 */
class OthelloButton extends JButton {
    private OthelloCoordinate coord;

    OthelloButton(OthelloCoordinate coord){
        super();
        this.coord = coord;
        setBackground(Color.ORANGE);
    }

    OthelloCoordinate getCoord() {
        return coord;
    }
}
