package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.Arrays;
import java.util.LinkedList;

import static dataRecording.DataSaverLoader.LoadPacManData;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private Node rootNode;

	public MyPacMan() {
		LinkedList<DataTuple> dataList = new LinkedList<>(Arrays.asList(LoadPacManData()));
		LinkedList<String> attributes = new LinkedList<>();
		// Booleans
		attributes.add("isBlinkyEdible");
		attributes.add("isInkyEdible");
		attributes.add("isPinkyEdible");
		attributes.add("isSueEdible");
		// Ints
		attributes.add("blinkyDist");
		attributes.add("inkyDist");
		attributes.add("pinkyDist");
		attributes.add("sueDist");
        // Directions
        attributes.add("blinkyDir");
        attributes.add("inkyDir");
        attributes.add("pinkyDir");
        attributes.add("sueDir");
		this.rootNode = new Node(dataList, attributes);
		this.rootNode.print(0);
	}

	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man
		return rootNode.getDecision(new DataTuple(game, MOVE.NEUTRAL));
	}
}