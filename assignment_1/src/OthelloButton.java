import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Jonathan Böcker on 2016-09-09.
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
