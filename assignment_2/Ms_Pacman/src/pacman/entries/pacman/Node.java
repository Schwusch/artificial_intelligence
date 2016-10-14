package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Jonathan Böcker on 2016-10-01.
 * This is a class representing a node in a decision tree
 */
public class Node implements Serializable {
    private String attribute;
    private HashMap<String, Node> childNodes = new HashMap<>();
    private boolean isLeafNode = false;
    private Constants.MOVE move = Constants.MOVE.NEUTRAL;

    /**
     * Creates a decision tree until it has exhausted all possible paths down to the leaf nodes.
     * @param data Data to consider/learn from in the learning algorithm.
     * @param attributes Relevant attributes in the {@link DataTuple} class.
      */
    public Node(LinkedList<DataTuple> data, LinkedList<String> attributes) {
        // If all tuples has the same class, this can be a leaf node
        if (data.size() > 0 && Utils.checkifSetHasSameClass(data)) {
            this.move = data.getFirst().DirectionChosen;
            this.isLeafNode = true;
        // If there is no more attributes we will roll with the majority class and call it a leaf node
        } else if (attributes.size() == 0) {
            this.move = Utils.findMajorityClass(data);
            this.isLeafNode = true;
        // Otherwise, make child nodes
        } else {
            try {
                // Get the most relevant attribute for this node
                this.attribute = C45.selectAttribute(data, attributes);
                attributes.remove(this.attribute);
                // With reflection, get the corresponding Field for the chosen attribute
                Field field = DataTuple.class.getDeclaredField(this.attribute);
                // Create a child node for each variation
                for (String value : Utils.getAttributeVariations(this.attribute)) {
                    makeChildNode(data, attributes, value, field);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a leaf node without any recursive calls or data processing
     * @param move The leaf nodes
     */
    public Node(Constants.MOVE move) {
        this.isLeafNode = true;
        this.move = move;
    }

    /**
     * Retrieves a {@link pacman.game.Constants.MOVE} from the decision tree
     * based on a {@link DataTuple} representing a game state
     * @param tuple A game state
     * @return A {@link pacman.game.Constants.MOVE}
     */
    Constants.MOVE getDecision(DataTuple tuple) {
        Field field;
        Constants.MOVE returnMove = null;
        if (this.isLeafNode) returnMove = this.move;
        else {
            try {
                // Retrieve the attributes field through reflection
                field = DataTuple.class.getDeclaredField(this.attribute);
                // If this attribute is already discretized:
                if (this.attribute.contains("Edible") || this.attribute.contains("Dir")) {
                    returnMove = childNodes.get(
                            field.get(tuple).toString()
                    ).getDecision(tuple);
                // If it's a distance, it needs to be discretized
                } else if (this.attribute.contains("Dist")) {
                    returnMove = childNodes.get(
                            Utils.discretizeDistance( (Integer) field.get(tuple) ).toString()
                    ).getDecision(tuple);
                // Something is really wrong, (abort, abort) return this nodes MOVE, which is probably NEUTRAL...
                } else {
                    System.out.println("No decision was found at attribute: " + this.attribute +
                            ", number of child nodes: " + this.childNodes.size());
                    returnMove = this.move;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnMove;
    }

    /*
    Produces a child node. May be recursive.
     */
    private void makeChildNode(LinkedList<DataTuple> dataset,
                               LinkedList<String> attributes,
                               String key,
                               Field field){
        // Make a subset with the attribute incarnated as a certain value
        LinkedList<DataTuple> subset = Utils.createSubset(dataset, field, key);
        // If there exists a subset, make a recursive call
        if (subset.size() > 0) {
            childNodes.put(key, new Node(subset, (LinkedList<String>) attributes.clone()));
        // If there is no data with the value embodied in the attribute,
        // take the majority class in the big data set and make it a child node
        } else {
            childNodes.put(key, new Node(Utils.findMajorityClass(dataset)));
        }
    }

    /**
     * Prints the decision tree recursively in a beautiful depth-to-tabs fashion in the console.
     * Pretty rad.
     */
    void print() {
        System.out.println("ROOT");
        print("");
    }

    /*
    Prints a formatted tree in the console.
     */
    private void print(String indent) {

        if (isLeafNode) {
            System.out.print(indent);
            System.out.println("  └─ Return " + move.toString());
        }
        Map.Entry<String, Node>[] nodes = childNodes.entrySet().toArray(new Map.Entry[0]);
        for(int i = 0; i < nodes.length; i++) {
            System.out.print(indent);
            if(i == nodes.length - 1){
                System.out.println( "└─ \"" + attribute + "\" = " + nodes[i].getKey() + ":");
                nodes[i].getValue().print(indent +  "    ");
            }else {
                System.out.println( "├─ \"" + attribute + "\" = " + nodes[i].getKey() + ":");
                nodes[i].getValue().print(indent + "│   ");
            }
        }
    }
}
