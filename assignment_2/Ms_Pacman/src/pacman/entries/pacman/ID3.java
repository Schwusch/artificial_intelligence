package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.lang.reflect.Field;
import java.util.HashMap;

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

        // Print all the keys and occurrenses
        for (Object key : tuplePerClass.keySet()) {
            System.out.println(key + ": " + tuplePerClass.get(key));
        }

        // Calculate the entropy of the data set
        int keys = Constants.MOVE.values().length;
        double infoD = 0;

        for (Constants.MOVE move : Constants.MOVE.values()) {
            double count = (double)tuplePerClass.get(move);
            infoD += -(count/(double)data.length) *
                    (Math.log(count/(double)data.length) / Math.log(2));
        }

        System.out.println("infoD: " + infoD);
        System.out.println("\nObject attributes and values:");

        // Lets see if we can access the Fields via reflection, perhaps not the most correct approach
        Class<?> instance = data[0].getClass();
        // I might also use .toGenericString() instead of .getName(). This will give the type information as well.
        for(Field field : instance.getDeclaredFields()) {
            try {
                System.out.println(field.getName() + " = " + field.get(data[0]));
            } catch (IllegalAccessException e) { }
        }

        return null;
    }

    public static void main(String[] args) {
        selectAttribute(LoadPacManData());
    }
}
