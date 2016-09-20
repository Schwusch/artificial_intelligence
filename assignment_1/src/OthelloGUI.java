import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 *
 * A simple Othello GUI with buttons representing every grid position.
 * The buttons are clickable!(Almost guaranteed*)
 */
class OthelloGUI extends JFrame implements ActionListener{
    static final int ROWS = 4;
    static final int COLS = 4;
    static final int AI   = 1;
    static final int HUMAN= -1;
    static final int NONE = 0;

    private OthelloController controller;
    private OthelloButton[][] buttons = new OthelloButton[4][4];

    private JLabel depthLabel = new JLabel();
    private JLabel nodeCountLabel = new JLabel();
    private JLabel infoLabel = new JLabel();
    private JLabel timerLabel = new JLabel();

    private long timeStarted;
    private Timer timer;
    private TimerUpdater updater = new TimerUpdater();


    OthelloGUI(String name, OthelloController controller) {
        super(name);
        setResizable(true);
        this.controller = controller;
        JPanel biggerPanel = new JPanel();
        JPanel textsPanel = new JPanel(new GridLayout(4,0));
        infoLabel.setText("Lets Play! You are black bricks.");
        infoLabel.setPreferredSize(new Dimension(280, 50));
        depthLabel.setText("Current depth limitation: " + StartOthello.SEARCH_DEPTH);
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

    void updateNodeCount(int count) {
        nodeCountLabel.setText("Nodes examined: " + count);
    }

    void updateInfo(String text) {
        infoLabel.setText(text);
    }

    void stopTimer(){
        this.timer.removeActionListener(this.updater);
    }

    void setGrid(int[][] grid, Boolean freeze) {
        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                setButtonState(row, col, grid[row][col]);
            }
        }
        if(freeze) freezeButtons();
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
                buttons[row][col].setEnabled(true);
                break;
        }
    }

    private void freezeButtons(){
        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    private static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() instanceof OthelloButton) {
            OthelloButton button = (OthelloButton) actionEvent.getSource();
            freezeButtons();
            timerLabel.setText("Time calculated: 0s");
            timeStarted = System.currentTimeMillis();
            timer = new Timer(500, this.updater);
            timer.start();
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    controller.buttonPressed(button.getCoord());
                    return null;
                }
            };
            worker.execute();

        }
    }
    private class TimerUpdater implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timerLabel.setText("Time calculated:" + Float.toString(round((System.currentTimeMillis() - timeStarted) / 1000.0f, 2)) + "s");
        }
    }
}
