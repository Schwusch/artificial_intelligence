package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Jonathan Böcker and Olle Casperson on 2016-10-04.
 * A class containing utility functions for the other classes to use.
 */
public class Utils {

    /**
     * Checks if the tuples has the same class by comparing the tuples with the first chosen direction.
     *
     * @param data The data to be compared
     * @return TRUE/FALSE depending on the outcome
     */
    static boolean checkifSetHasSameClass(LinkedList<DataTuple> data) {
        Constants.MOVE firstTupleMove = data.getFirst().DirectionChosen;
        for (DataTuple tuple : data) {
            if (!tuple.DirectionChosen.equals(firstTupleMove)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Iterates through a list of tuples and finds the move that is most recurring.
     *
     * @param data A list with tuples
     * @return The most recurring move
     */
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
            if (tuplePerClass.get(majorityMove) < tuplePerClass.get(move))
                majorityMove = move;
        }

        return majorityMove;
    }

    /**
     * Creates a subset of a recieved list of tuples by comparing the attributes of the
     * tuples with a key(value)
     *
     * @param data      List of tuples
     * @param attribute Attribute of the tuples
     * @param value     The value to compare the attributes to
     * @return The subset
     */
    static LinkedList<DataTuple> createSubset(LinkedList<DataTuple> data, String attribute, String value) {
        LinkedList<DataTuple> subset = new LinkedList<>();
        try {
            for (DataTuple tuple : data) {
                // With reflection, get the corresponding Field for the attribute
                Field field = DataTuple.class.getDeclaredField(attribute);
                // Make a subset with the attribute incarnated as a certain value
                //If the attribute is distance the value needs to be discretized
                if (attribute.contains("Dist") &&
                        Utils.discretizeDistance(
                                (Integer) field.get(tuple)).toString().equals(value)) {
                    subset.add(tuple);
                } else if (!attribute.contains("Dist") &&
                        field.get(tuple).toString().equals(value)) {
                    subset.add(tuple);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subset;
    }

    /**
     * Discretizes a distance
     *
     * @param dist The distance to be discretized
     * @return LOW if the discretized distance is > 0 and <= 0.2.
     * Otherwise HIGH
     */
    static Utils.DiscreteTag discretizeDistance(int dist) {
        double aux = dist / (double) 150;
        return Utils.DiscreteTag.DiscretizeDouble(aux);
    }

    /**
     * The method returns an array with the different variations depending on
     * the attribute (Edible, Distance, Direction) it gets.
     *
     * @param attribute The attribute whose variation should be returned
     * @return Array with the different variations
     */
    static String[] getAttributeVariations(String attribute) {
        String array[];
        if (attribute.contains("Edible")) {
            //Edible, either true or false
            array = new String[]{"true", "false"};

        } else if (attribute.contains("Dist")) {
            //Distance, two different variations, HIGH or LOW
            Utils.DiscreteTag tags[] = Utils.DiscreteTag.values();
            array = new String[tags.length];
            for (int i = 0; i < tags.length; i++) {
                array[i] = tags[i].toString();
            }

        } else if (attribute.contains("Dir")) {
            //Direction could be UP, DOWN, RIGHT, LEFT, NETUTRAL
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

        /**
         * The method returns LOW or HIGH depending on the double it recieves
         *
         * @param aux
         * @return HIGH/LOW
         */
        public static Utils.DiscreteTag DiscretizeDouble(double aux) {
            if (aux > 0 && aux <= 0.2)
                return Utils.DiscreteTag.LOW;
            else
                return Utils.DiscreteTag.HIGH;
        }
    }
}
