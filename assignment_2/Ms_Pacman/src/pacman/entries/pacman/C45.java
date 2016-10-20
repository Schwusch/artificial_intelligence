package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Jonathan Böcker on 2016-09-27.
 * <p>
 * Implementation of the C45 algorithm for the Pacman assignment
 */
public class C45 {

    /**
     * Takes a list of {@link DataTuple} and a list of attributes and returns the most beneficial
     * attribute for a decision tree node. Based on the C4.5 selection algorithm.
     *
     * @param dataList The data list
     * @param attributes The attribute list
     * @return The most beneficial attribute
     */
    public static AttributeWrapper selectAttribute(
            LinkedList<DataTuple> dataList,
            LinkedList<String> attributes) {
        // Calculate the total dataset entropy
        double infoD = calculateInfoD(dataList);

        // Calculate benefit and gainratio for every attribute
        HashMap<String, AttributeWrapper> gains = new HashMap<>();
        for (String attr : attributes) {
            AttributeWrapper attrWrapper = new AttributeWrapper(attr);
            double infoAD = 0;
            double splitInfoAD = 0;
            // Calculate sum of every attribute name
            for (String value : Utils.getAttributeVariations(attr)) {
                LinkedList<DataTuple> subset = Utils.createSubset(dataList, attr, value);
                attrWrapper.addSubset(new SubSetWrapper(value, subset));
                infoAD += ((double) subset.size() / (double) dataList.size()) * calculateInfoD(subset);
                // Log(0) is undefined, so only do it if the subset size exceed 0
                if(subset.size() > 0) {
                    splitInfoAD += -((double) subset.size() / (double) dataList.size()) *
                            (Math.log((double) subset.size() / (double) dataList.size()) / (double) Math.log(2));
                }
            }

            double gainA = infoD - infoAD;
            double gainRatio = Double.MAX_VALUE;
            if(splitInfoAD > 0)
                gainRatio = gainA / splitInfoAD;
            // Store the attributes gainratio in a hashmap
            attrWrapper.gainRatio = gainRatio;
            gains.put(attr, attrWrapper);
        }

        AttributeWrapper maxGainRatioInMap = null;
        double maxValueInMap = -Double.MAX_VALUE;
        for (Map.Entry<String, AttributeWrapper> entry : gains.entrySet()) {
            if (entry.getValue().gainRatio > maxValueInMap) {
                maxValueInMap = entry.getValue().gainRatio;
                maxGainRatioInMap = entry.getValue();
            }
        }
        return maxGainRatioInMap;
    }

    private static double calculateInfoD(LinkedList<DataTuple> dataTuples) {
        // Will hold the number of occurrences of each classifier class in the set
        HashMap<Constants.MOVE, Double> tuplePerClass = new HashMap<>();
        // Insert an Integer for each classifier class
        for (Constants.MOVE move : Constants.MOVE.values()) {
            tuplePerClass.put(move, 0.0);
        }

        // Increase every find by one in the map
        for (DataTuple tuple : dataTuples) {
            tuplePerClass.put(tuple.DirectionChosen, tuplePerClass.get(tuple.DirectionChosen) + 1);
        }

        // Calculate the entropy of the data set
        double dataSize = (double) dataTuples.size();
        double infoD = 0;

        for (Constants.MOVE move : Constants.MOVE.values()) {
            double count = tuplePerClass.get(move);
            if (count > 0) {
                infoD += -(count / dataSize) *
                        (Math.log(count / dataSize) / Math.log(2));
            }
        }
        return infoD;
    }


}
