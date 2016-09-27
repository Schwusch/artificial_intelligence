package pacman.entries.pacman;

import com.sun.corba.se.spi.ior.ObjectAdapterId;
import dataRecording.DataTuple;
import pacman.game.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static dataRecording.DataSaverLoader.LoadPacManData;

/**
 * Created by Jonathan Böcker on 2016-09-27.
 *
 * Implementation of the ID3 algorithm for the Pacman assignment
 */
public class ID3 {

    public static String selectAttribute(DataTuple[] data ){//, ArrayList<String> attributes){
        // Will hold the number of occurrences of each classifier class in the set
        HashMap<Constants.MOVE, Integer> tuplePerClass = new HashMap<>();

        // Insert an Integer for each classifier class
        for (Constants.MOVE move : Constants.MOVE.values()) {
            tuplePerClass.put(move, 0);
        }

        // Increase every find by one in the map
        for(DataTuple tuple : data) {
            tuplePerClass.put(tuple.DirectionChosen, tuplePerClass.get(tuple.DirectionChosen) + 1);
        }

        // Print all the keys
        for (Object key : tuplePerClass.keySet()) {
            System.out.println(key + ": " + tuplePerClass.get(key));
        }
        return null;
    }

    public static void main(String[] args) {
        selectAttribute(LoadPacManData());
    }
}
