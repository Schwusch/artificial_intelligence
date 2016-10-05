package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Jonathan Böcker on 2016-09-27.
 * <p>
 * Implementation of the ID3 algorithm for the Pacman assignment
 */
public class ID3 {

    public static String selectAttribute(LinkedList<DataTuple> dataList, LinkedList<String> attributes) {
        double infoD = calculateInfoD(dataList);
        String maxKeyInMap = null;
        HashMap<String, Double> gains = new HashMap<>();

        for (String attr : attributes) {
            double infoAD = 0;
            Field field = null;
            try {
                field = DataTuple.class.getDeclaredField(attr);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            LinkedList<DataTuple> subset = null;

            if (attr.contains("Edible")) {
                String arr[] = {"true", "false"};
                for (String value : arr) {
                    subset = Utils.createSubset(dataList, field, value);
                    infoAD += ((double) subset.size() / (double) dataList.size()) * calculateInfoD(subset);
                }

            } else if (attr.contains("Dist")) {
                DataTuple.DiscreteTag tags[] = DataTuple.DiscreteTag.values();
                for (DataTuple.DiscreteTag tag : tags) {
                    subset = Utils.createSubset(dataList, field, tag.toString());
                    infoAD += ((double) subset.size() / (double) dataList.size()) * calculateInfoD(subset);
                }
            } else if (attr.contains("Dir")) {
                Constants.MOVE moves[] = Constants.MOVE.values();
                for (Constants.MOVE move: moves) {
                    subset = Utils.createSubset(dataList, field, move.toString());
                    infoAD += ((double) subset.size() / (double) dataList.size()) * calculateInfoD(subset);
                }
            }

            double gain = infoD - infoAD;
            gains.put(attr, gain);
        }

        double maxValueInMap = -Double.MAX_VALUE;
        for (Map.Entry<String, Double> entry : gains.entrySet()) {
            if (entry.getValue() > maxValueInMap) {
                maxValueInMap = entry.getValue();
                maxKeyInMap = entry.getKey();
            }
        }
        return maxKeyInMap;
    }

    private static double calculateInfoD(LinkedList<DataTuple> dataTuples) {
        // Will hold the number of occurrences of each classifier class in the set
        HashMap<Constants.MOVE, Integer> tuplePerClass = new HashMap<>();
        // Insert an Integer for each classifier class
        for (Constants.MOVE move : Constants.MOVE.values()) {
            tuplePerClass.put(move, 0);
        }

        // Increase every find by one in the map
        for (DataTuple tuple : dataTuples) {
            tuplePerClass.put(tuple.DirectionChosen, tuplePerClass.get(tuple.DirectionChosen) + 1);
        }

        // Calculate the entropy of the data set
        int dataSize = dataTuples.size();
        double infoD = 0;


        for (Constants.MOVE move : Constants.MOVE.values()) {
            double count = (double) tuplePerClass.get(move);
            if (count > 0) {
                infoD += -((double) count / (double) dataSize) *
                        (Math.log((double) count / (double) dataSize) / (double) Math.log(2));
            }
        }
        return infoD;
    }


}
