import javax.swing.*;

/**
 * Created by Jonathan Böcker on 2016-09-09.
 *
 * This class is the glue between the GUI and the AI/Othello logic.
 */
class OthelloController {
    private OthelloGUI gui;
    private int[][] grid = new int[OthelloGUI.ROWS][OthelloGUI.COLS];
    private int nodesExamined = 0;
    private int depth = 0;

    /**
     * Initializes a GUI with an Othelloboard
     */
    OthelloController(){
        // Initialize the board with 4 bricks
        grid[OthelloGUI.ROWS/2 - 1][OthelloGUI.COLS/2 - 1] = OthelloGUI.HUMAN;
        grid[OthelloGUI.ROWS/2][OthelloGUI.COLS/2] = OthelloGUI.HUMAN;
        grid[OthelloGUI.ROWS/2 - 1][OthelloGUI.COLS/2] = OthelloGUI.AI;
        grid[OthelloGUI.ROWS/2][OthelloGUI.COLS/2 - 1] = OthelloGUI.AI;
        // Make an interface
        SwingUtilities.invokeLater(() -> {
            gui = new OthelloGUI("Othello", OthelloController.this);
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            gui.pack();
            gui.setVisible(true);
            // Update the GUI
            gui.setGrid(grid, false);
            gui.setValidMoves(Utilities.findValidMoves(this.grid, OthelloGUI.HUMAN));
        });

    }

    /**
     * Increases the node count and updates the GUI every 1000nd node
     */
    void nodeFound(int depth) {
        this.nodesExamined++;
        if(depth > this.depth) {
            this.depth = depth;
            SwingUtilities.invokeLater(() -> gui.updateDepthLabel(depth));
        }
        if(this.nodesExamined % 1000 == 0)
            SwingUtilities.invokeLater(() -> gui.updateNodeCount(nodesExamined));
    }

    /**
     * When the human player presses a button, the AI has to figure out the best next move
     * @param coord The board coordinate pressed by human
     * @param timeStarted
     */
    void buttonPressed(OthelloCoordinate coord, long timeStarted) {
        // Reset the node count
        this.nodesExamined = 0;
        this.depth = 0;
        // Set a new deadline 4.5 seconds in the future
        long deadline = timeStarted + StartOthello.THINKING_TIME;

        // Flips a few bricks if possible and update the GUI board
        this.grid = Utilities.calculateBoardChange(this.grid, coord, OthelloGUI.HUMAN);
        SwingUtilities.invokeLater(() -> gui.setGrid(grid, true));

        // AI finds the best possible counter move and flips a lot of bricks if possible :)
        StateNode node = new StateNode(grid, this, deadline);
        if(node.hasMove()) {
            OthelloCoordinate computerMove = node.getBestMove();
            this.grid = Utilities.calculateBoardChange(this.grid, computerMove, OthelloGUI.AI);
        }

        // If human doesn't have any moves and AI has, AI makes another move
        while (!Utilities.hasPossibleMoves(this.grid, OthelloGUI.HUMAN) && Utilities.hasPossibleMoves(this.grid, OthelloGUI.AI)){
            // Give computer another 5 seconds
            deadline = deadline + StartOthello.THINKING_TIME;

            node = new StateNode(grid, this, deadline);
            OthelloCoordinate computerMove = node.getBestMove();
            this.grid = Utilities.calculateBoardChange(this.grid, computerMove, OthelloGUI.AI);
            updateGUI();
            // Reset the node count
            this.nodesExamined = 0;
        }

        // Update GUI stuff
        updateGUI();

        // Check if the game has been finished and if it has, who is the winner
        if(Utilities.isGameFinished(this.grid)) {
            int score = Utilities.boardScore(this.grid);
            String info;
            if (score > 0)
                info = "You lost to a machine.";
            else if (score < 0)
                info = "You beat my Othello bot :(";
            else
                info = "Tie! How inconvenient.";
            // Update GUI message
            SwingUtilities.invokeLater(() -> gui.updateInfo(info));
        }
    }

    private void updateGUI() {
        SwingUtilities.invokeLater(() -> {
            gui.setGrid(grid, false);
            gui.setValidMoves(Utilities.findValidMoves(this.grid, OthelloGUI.HUMAN));
            gui.stopTimer();
            gui.updateNodeCount(nodesExamined);
        });
    }
}
