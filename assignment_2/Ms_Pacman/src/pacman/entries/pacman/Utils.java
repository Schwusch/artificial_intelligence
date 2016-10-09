package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Jonathan Böcker on 2016-10-04.
 *
 *
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
                if (attribute.getName().contains("Dist") &&
                        Utils.discretizeDistance(
                                (Integer) attribute.get(tuple)).toString().equals(value)) {
                    subset.add(tuple);
                } else if (!attribute.getName().contains("Dist") &&
                        attribute.get(tuple).toString().equals(value)) {
                    subset.add(tuple);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return subset;
    }

    static Utils.DiscreteTag discretizeDistance(int dist) {
        double aux = dist / (double) 150;
        return Utils.DiscreteTag.DiscretizeDouble(aux);
    }

    static String getDistanceDiscreteTag(Constants.GHOST ghost, Game game) {
            return Utils.DiscreteTag.DiscretizeDouble(
                    (double) game.getShortestPathDistance(
                            game.getPacmanCurrentNodeIndex(),
                            game.getGhostCurrentNodeIndex(ghost)
                    ) / 150d
            ).toString();

    }

    static String[] getAttributeVariations(String attribute) {
        String array[];
        if (attribute.contains("Edible")) {
            array = new String[]{"true", "false"};

        } else if (attribute.contains("Dist")) {
            Utils.DiscreteTag tags[] = Utils.DiscreteTag.values();
            array = new String[tags.length];
            for (int i = 0; i < tags.length; i++) {
                array[i] = tags[i].toString();
            }

        }  else if (attribute.contains("Dir")) {
            Constants.MOVE moves[] = Constants.MOVE.values();
            array = new String[moves.length];
            for (int i = 0; i < moves.length; i++) {
                array[i] = moves[i].toString();
            }

        } else {
            array = new String[0];
        }

        return array;
    }

    public enum DiscreteTag {
        LOW, HIGH;

        public static Utils.DiscreteTag DiscretizeDouble(double aux) {
            if (aux > 0 && aux <= 0.2)
                return Utils.DiscreteTag.LOW;
            else
                return Utils.DiscreteTag.HIGH;
        }
    }
}
