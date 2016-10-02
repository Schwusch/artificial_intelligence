package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static dataRecording.DataSaverLoader.LoadPacManData;

/**
 * Created by Jonathan Böcker on 2016-09-27.
 *
 * Implementation of the ID3 algorithm for the Pacman assignment
 */
public class ID3 {

    public static String selectAttribute(LinkedList<DataTuple> dataList, LinkedList<String> attributes){

        double infoD = calculateInfoD(dataList);

        System.out.println("infoD: " + infoD);
        System.out.println("\nObject attributes and values:");

        // I might also use .toGenericString() instead of .getName(). This will give the type information as well.
        for(Field field : attributes) {
            try {
                System.out.println(field.getName() + " = " + field.get(dataList.getLast()) + " " );
                if(field.getAnnotatedType().getType().toString().equals("int")) {
                    System.out.println(
                            dataList.getLast().discretizeDistance(
                                    (int)field.get(dataList.getLast())
                            )
                    );
                    DataTuple.DiscreteTag.DiscretizeDouble(0.5);
                }

            } catch (IllegalAccessException e) { }
        }

        return null;
    }

    private static double calculateInfoD(LinkedList<DataTuple> dataTuples) {
        // Will hold the number of occurrences of each classifier class in the set
        HashMap<Constants.MOVE, Integer> tuplePerClass = new HashMap<>();
        // Insert an Integer for each classifier class
        for (Constants.MOVE move : Constants.MOVE.values()) {
            tuplePerClass.put(move, 0);
        }

        // Increase every find by one in the map
        for(DataTuple tuple : dataTuples) {
            tuplePerClass.put(tuple.DirectionChosen, tuplePerClass.get(tuple.DirectionChosen) + 1);
        }

        // Calculate the entropy of the data set
        int dataSize = dataTuples.size();
        double infoD = 0;


        for (Constants.MOVE move : Constants.MOVE.values()) {
            double count = (double)tuplePerClass.get(move);
            infoD += -(count/(double)dataSize) *
                    (Math.log(count/(double)dataSize) / Math.log(2));
        }
        return infoD;
    }

    public static void main(String[] args) {
        LinkedList<DataTuple> dataList = new LinkedList<>(Arrays.asList(LoadPacManData()));
        LinkedList<Field> attributes = new LinkedList<>();
        Class<?> tuple = DataTuple.class;
        try {
            // Booleans
            attributes.add(tuple.getField("isBlinkyEdible"));
            attributes.add(tuple.getField("isInkyEdible"));
            attributes.add(tuple.getField("isPinkyEdible"));
            attributes.add(tuple.getField("isSueEdible"));
            // Ints
            attributes.add(tuple.getField("blinkyDist"));
            attributes.add(tuple.getField("inkyDist"));
            attributes.add(tuple.getField("pinkyDist"));
            attributes.add(tuple.getField("sueDist"));
            // MOVES:s
           /* attributes.add(tuple.getField("blinkyDir"));
            attributes.add(tuple.getField("inkyDir"));
            attributes.add(tuple.getField("pinkyDir"));
            attributes.add(tuple.getField("sueDir"));*/
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        selectAttribute(dataList, attributes);
    }
}
