import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 *
 * A simple Othello GUI with buttons representing every grid position.
 * The buttons are clickable!(Almost guaranteed*)
 */
class OthelloGUI extends JFrame implements ActionListener{
    static final int ROWS = 4;
    static final int COLS = 4;
    static final int AI   = -1;
    static final int HUMAN= 1;
    static final int NONE = 0;

    private OthelloController controller;
    private OthelloButton[][] buttons = new OthelloButton[4][4];


    OthelloGUI(String name, OthelloController controller) {
        super(name);
        setResizable(false);
        this.controller = controller;

        JPanel panel = new JPanel(new GridLayout(ROWS, COLS));
        panel.setMaximumSize(new Dimension(400,400));

        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++){
                buttons[row][col] = new OthelloButton(new OthelloCoordinate(row, col));
                buttons[row][col].setPreferredSize(new Dimension(40,40));
                buttons[row][col].addActionListener(this);
                panel.add(buttons[row][col]);
            }
        }
        add(panel);
    }

    void setGrid(int[][] grid) {
        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                setButtonState(row, col, grid[row][col]);
            }
        }
    }

    private void setButtonState(int row, int col, int value) {
        switch (value){
            case AI:
                buttons[row][col].setBackground(Color.WHITE);
                buttons[row][col].setEnabled(false);
                break;
            case HUMAN:
                buttons[row][col].setBackground(Color.BLACK);
                buttons[row][col].setEnabled(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() instanceof OthelloButton) {
            OthelloButton button = (OthelloButton) actionEvent.getSource();
            controller.buttonPressed(button.getCoord());
        }
    }
}
