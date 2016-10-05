package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by schwusch on 2016-10-04.
 */
public class Utils {

    static boolean checkifSetHasSameClass(LinkedList<DataTuple> data) {
        Constants.MOVE firstTupleMove = data.getFirst().DirectionChosen;
        for(DataTuple tuple : data) {
            if (!tuple.DirectionChosen.equals(firstTupleMove)){
                return false;
            }
        }

        return true;
    }

    static Constants.MOVE findMajorityClass(LinkedList<DataTuple> data) {
        // Will hold the number of occurrences of each classifier class in the set
        HashMap<Constants.MOVE, Integer> tuplePerClass = new HashMap<>();
        // Insert an Integer for each classifier class
        for (Constants.MOVE move : Constants.MOVE.values()) {
            tuplePerClass.put(move, 0);
        }

        // Increase every find by one in the map
        for (DataTuple tuple : data) {
            tuplePerClass.put(tuple.DirectionChosen, tuplePerClass.get(tuple.DirectionChosen) + 1);
        }
        // Find the most recurring move
        Constants.MOVE majorityMove = Constants.MOVE.NEUTRAL;
        for (Constants.MOVE move : tuplePerClass.keySet()) {
            if(tuplePerClass.get(majorityMove) < tuplePerClass.get(move))
                majorityMove = move;
        }

        return majorityMove;
    }

    static LinkedList<DataTuple> createSubset(LinkedList<DataTuple> data, Field attribute, String value) {
        LinkedList<DataTuple> subset = new LinkedList<>();

        for (DataTuple tuple : data) {
            try {
                if (attribute.getName().contains("Edible") && attribute.get(tuple).toString().equals(value)) {
                    subset.add(tuple);
                } else if (attribute.getName().contains("Dist") &&
                        tuple.discretizeDistance(
                                (Integer) attribute.get(tuple)).toString().equals(value)) {
                    subset.add(tuple);
                } else if (attribute.getName().contains("Dir") &&
                        attribute.get(tuple).toString().equals(value)) {
                    subset.add(tuple);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return subset;
    }

    static String getDistanceDiscreteTag(Constants.GHOST ghost, Game game) {
        int shortestDistance = game.getShortestPathDistance(
                game.getPacmanCurrentNodeIndex(),
                game.getGhostCurrentNodeIndex(ghost));

        if (shortestDistance == -1) {
            return DataTuple.DiscreteTag.NONE.toString();
        } else {
            return DataTuple.DiscreteTag.DiscretizeDouble(
                    (double) game.getShortestPathDistance(
                            game.getPacmanCurrentNodeIndex(),
                            game.getGhostCurrentNodeIndex(ghost)
                    ) / 150d
            ).toString();
        }
    }
}
