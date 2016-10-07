package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Jonathan Böcker on 2016-10-01.
 */
public class Node {
    private String attribute;
    private HashMap<String, Node> childNodes = new HashMap<>();
    private boolean isLeafNode = false;
    private Constants.MOVE move = Constants.MOVE.NEUTRAL;

    public Node(LinkedList<DataTuple> data, LinkedList<String> attributes) {

        if (data.size() > 0 && Utils.checkifSetHasSameClass(data)) {
            this.move = data.getFirst().DirectionChosen;
            this.isLeafNode = true;

        } else if (attributes.size() == 0) {
            this.move = Utils.findMajorityClass(data);
            this.isLeafNode = true;

        } else {
            try {
                this.attribute = ID3.selectAttribute(data, attributes);
                attributes.remove(this.attribute);
                Field field = DataTuple.class.getDeclaredField(this.attribute);

                if (this.attribute.contains("Edible")) {
                    LinkedList<DataTuple> subset = Utils.createSubset(data, field, "true");
                    if (subset.size() > 0) {
                        childNodes.put("true", new Node(subset, (LinkedList<String>) attributes.clone()));
                    } else {
                        childNodes.put("true", new Node(Utils.findMajorityClass(data)));
                    }

                    subset = Utils.createSubset(data, field, "false");
                    if (subset.size() > 0) {
                        childNodes.put("false", new Node(Utils.createSubset(data, field, "false"), (LinkedList<String>) attributes.clone()));
                    } else {
                        childNodes.put("false", new Node(Utils.findMajorityClass(data)));
                    }
                } else if (this.attribute.contains("Dist")) {
                    Utils.DiscreteTag tags[] = Utils.DiscreteTag.values();

                    for (Utils.DiscreteTag tag : tags) {
                        LinkedList<DataTuple> subset = Utils.createSubset(data, field, tag.toString());
                        if (subset.size() > 0) {
                            childNodes.put(tag.toString(), new Node(subset, (LinkedList<String>) attributes.clone()));
                        } else {
                            childNodes.put(tag.toString(), new Node(Utils.findMajorityClass(data)));
                        }
                    }
                } else if (this.attribute.contains("Dir")) {
                    Constants.MOVE moves[] = Constants.MOVE.values();

                    for (Constants.MOVE move : moves) {
                        LinkedList<DataTuple> subset = Utils.createSubset(data, field, move.toString());
                        if (subset.size() > 0) {
                            childNodes.put(move.toString(), new Node(subset, (LinkedList<String>) attributes.clone()));
                        } else {
                            childNodes.put(move.toString(), new Node(Utils.findMajorityClass(data)));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Node(Constants.MOVE move) {
        this.isLeafNode = true;
        this.move = move;
    }

    Constants.MOVE getDecision(DataTuple tuple) {
        Field field;
        Constants.MOVE returnMove = null;
        if (isLeafNode) returnMove = move;
        else {
            try {
                field = DataTuple.class.getDeclaredField(this.attribute);

                if (this.attribute.contains("Edible") || this.attribute.contains("Dir")) {
                    returnMove = childNodes.get(
                            field.get(tuple).toString()
                    ).getDecision(tuple);
                } else if (this.attribute.contains("Dist")) {
                    returnMove = childNodes.get(
                            Utils.discretizeDistance((Integer) field.get(tuple)).toString()
                    ).getDecision(tuple);
                } else {
                    System.out.println("No decision was found at attribute: " + attribute +
                            ", number of child nodes: " + childNodes.size());
                    returnMove = this.move;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnMove;
    }

    void print(int depth) {
        if (isLeafNode) {
            for (int i = 0; i < depth; i++) {
                System.out.print((char) 0x21A7 + " ");
            }
            System.out.println("Return " + move.toString());
        } else {
            for (int i = 0; i < depth; i++) {
                System.out.print((char) 0x21A7 + " ");
            }
            System.out.println("<" + attribute + ">");
            for (Map.Entry<String, Node> entry : childNodes.entrySet()) {
                for (int i = 0; i < depth; i++) {
                    System.out.print((char) 0x21A7 + " ");
                }
                System.out.println((char) 0x27A5 + "If " + entry.getKey() + ":");
                entry.getValue().print(depth + 1);
            }
        }
    }
}
