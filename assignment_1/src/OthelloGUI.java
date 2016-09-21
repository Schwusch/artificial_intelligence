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
    static final int ROWS = 8;
    static final int COLS = 8;
    static final int AI   = 1;
    static final int HUMAN= -1;
    static final int NONE = 0;

    private OthelloController controller;
    private OthelloButton[][] buttons = new OthelloButton[ROWS][COLS];

    private JLabel depthLabel = new JLabel();
    private JLabel nodeCountLabel = new JLabel();
    private JLabel infoLabel = new JLabel();
    private JLabel timerLabel = new JLabel();

    private long timeStarted;
    private Timer timer;
    private TimerUpdater updater = new TimerUpdater();

    /**
     * Makes the GUI look good
     *
     * @param name Title of the window
     * @param controller A reference to someone whe can call if the user does something
     */
    OthelloGUI(String name, OthelloController controller) {
        super(name);
        setResizable(true);
        this.controller = controller;
        JPanel biggerPanel = new JPanel();
        JPanel textsPanel = new JPanel(new GridLayout(4,0));
        infoLabel.setText("Lets Play! You are black bricks.");
        infoLabel.setPreferredSize(new Dimension(280, 50));
        depthLabel.setText("Current calculated moves ahead: ");
        depthLabel.setPreferredSize(new Dimension(280, 50));
        nodeCountLabel.setText("Nodes examined: 0");
        nodeCountLabel.setPreferredSize(new Dimension(280, 50));
        timerLabel.setText("Time calculated:");
        timerLabel.setPreferredSize(new Dimension(280, 50));
        textsPanel.add(infoLabel);
        textsPanel.add(depthLabel);
        textsPanel.add(nodeCountLabel);
        textsPanel.add(timerLabel);
        biggerPanel.add(textsPanel);

        JPanel panel = new JPanel(new GridLayout(ROWS, COLS));
        panel.setBackground(new Color(0x0066cc));
        panel.setMaximumSize(new Dimension(400,400));

        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++){
                buttons[row][col] = new OthelloButton(new OthelloCoordinate(row, col));
                buttons[row][col].setPreferredSize(new Dimension(70,70));
                buttons[row][col].addActionListener(this);
                panel.add(buttons[row][col]);
            }
        }
        biggerPanel.add(panel);
        add(biggerPanel);
    }
    // This is an occasion where i believe the code speaks for itself
    void updateNodeCount(int count) {
        nodeCountLabel.setText("Nodes examined: " + count);
    }
    // Dito
    void updateInfo(String text) {
        infoLabel.setText(text);
    }
    // Dito
    void updateDepthLabel(int depth) {
        depthLabel.setText("Current calculated moves ahead: " + depth);
    }
    // Dito
    void stopTimer(){
        this.timer.removeActionListener(this.updater);
    }

    /**
     * Updates the graphical board
     * @param grid The board data
     * @param freeze If the buttons should be disabled or not
     */
    void setGrid(int[][] grid, Boolean freeze) {
        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                setButtonState(row, col, grid[row][col]);
            }
        }
        if(freeze) freezeButtons();
    }

    /**
     * Sets GUI buttons enabled or disabled according to the grid parameter
     * @param grid
     */
    void setValidMoves(boolean[][] grid) {
        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if(grid[row][col]) {
                    buttons[row][col].setEnabled(true);
                    buttons[row][col].setBackground(new Color(0,140,60));
                } else {
                    buttons[row][col].setEnabled(false);
                }
            }
        }
    }

    /*
    Enables/Disables button depending if there is a brick on it or not
     */
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
                buttons[row][col].setEnabled(true);
                buttons[row][col].setBackground(new Color(0,99,33));
                break;
        }
    }

    /*
    Disables all buttons
     */
    private void freezeButtons(){
        for(OthelloButton[] buttonArray: buttons) for(OthelloButton button: buttonArray) button.setEnabled(false);
    }

    /**
     * React to user input(clicks on buttons)
     * Tells the controller which button has been pressed
     * @param actionEvent
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() instanceof OthelloButton) {
            OthelloButton button = (OthelloButton) actionEvent.getSource();
            freezeButtons();
            timerLabel.setText("Time calculated: 0s");
            timeStarted = System.currentTimeMillis();
            timer = new Timer(500, this.updater);
            timer.start();
            // Start a background thread to *not* freeze the GUI
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    controller.buttonPressed(button.getCoord(), timeStarted);
                    return null;
                }
            };
            worker.execute();

        }
    }

    /*
    Updates a label with time passed
     */
    private class TimerUpdater implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timerLabel.setText(
                    "Time calculated:" +
                    Float.toString(
                            Utilities.round(
                                    (System.currentTimeMillis() - timeStarted) / 1000.0f, 2
                            )
                    ) +
                    "s"
            );
        }
    }

    /**
     * Created by Jonathan Böcker on 2016-09-09.
     *
     * A class extending JButton to remember its position on an Othello board
     * It has a satisfactory green color, pleasing the human eye, caressing the soul.
     */
    private class OthelloButton extends JButton {
        private OthelloCoordinate coord;

        OthelloButton(OthelloCoordinate coord){
            super();
            this.coord = coord;
            setBackground(new Color(0,99,33));
        }

        OthelloCoordinate getCoord() {
            return coord;
        }
    }
}
