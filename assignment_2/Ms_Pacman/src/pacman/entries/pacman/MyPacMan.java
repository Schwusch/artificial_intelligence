package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import javax.swing.*;
import java.io.*;
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
		String filePathString = "tree.ser";
		LinkedList<DataTuple> dataList = new LinkedList<>(Arrays.asList(LoadPacManData()));
		File f = new File(filePathString);
		if(f.exists() &&
				!f.isDirectory() &&
				JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
						"An old decision tree was found. Load Tree?") && loadTree(filePathString)) {
			JOptionPane.showMessageDialog(null, "Decision tree was loaded");
		} else {
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
			saveTree(filePathString);
			JOptionPane.showMessageDialog(null,
					"An old decision tree was not found or couldn't be loaded or you wanted to make a new one. \nA new decision tree was created");
		}
		this.rootNode.print();
		verifyData(dataList);
	}

	public MOVE getMove(Game game, long timeDue) 
	{
		// Advanced game logic below
		return rootNode.getDecision(new DataTuple(game, MOVE.NEUTRAL));
	}

	private void verifyData(LinkedList<DataTuple> dataList) {
		int correctGuesses = 0;

		for (DataTuple tuple : dataList)
			if (tuple.DirectionChosen.equals(this.rootNode.getDecision(tuple)))
				correctGuesses++;
		System.out.println("\n>> Classifier accuracy: " + correctGuesses / (double)dataList.size());
	}

	private void saveTree(String filePathString) {
		try{
			FileOutputStream fout = new FileOutputStream(filePathString);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this.rootNode);
			oos.close();
			System.out.println(">> Saved Tree.");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private boolean loadTree(String filePathString) {
		try {
			FileInputStream fin = new FileInputStream(filePathString);
			ObjectInputStream ois = new ObjectInputStream(fin);
			this.rootNode = (Node)ois.readObject();
			ois.close();
			System.out.println(">> Loaded Tree");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}