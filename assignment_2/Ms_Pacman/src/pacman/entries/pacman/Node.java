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

    public Constants.MOVE getDecision(Game game) {
        Constants.MOVE returnMove;
        if (isLeafNode) returnMove = move;
        else if (attribute.equals("isBlinkyEdible")) {
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.BLINKY)
                    )
            ).getDecision(game);
        } else if (attribute.equals("isInkyEdible")) {
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.INKY)
                    )
            ).getDecision(game);
        } else if (attribute.equals("isPinkyEdible")) {
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.PINKY)
                    )
            ).getDecision(game);
        } else if (attribute.equals("isSueEdible")) {
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.SUE)
                    )
            ).getDecision(game);
        } else if (attribute.equals("blinkyDist")) {
            returnMove = childNodes.get(
                    Utils.getDistanceDiscreteTag(Constants.GHOST.BLINKY, game)
            ).getDecision(game);
        } else if (attribute.equals("inkyDist")) {
            returnMove = childNodes.get(
                    Utils.getDistanceDiscreteTag(Constants.GHOST.INKY, game)
            ).getDecision(game);
        } else if (attribute.equals("pinkyDist")) {
            returnMove = childNodes.get(
                    Utils.getDistanceDiscreteTag(Constants.GHOST.PINKY, game)
            ).getDecision(game);
        } else if (attribute.equals("sueDist")) {
            returnMove = childNodes.get(
                    Utils.getDistanceDiscreteTag(Constants.GHOST.SUE, game)
            ).getDecision(game);
        } else if (attribute.equals("blinkyDir")) {
            returnMove = childNodes.get(
                    game.getGhostLastMoveMade(Constants.GHOST.BLINKY).toString()
            ).getDecision(game);
        } else if (attribute.equals("inkyDir")) {
            returnMove = childNodes.get(
                    game.getGhostLastMoveMade(Constants.GHOST.INKY).toString()
            ).getDecision(game);
        } else if (attribute.equals("pinkyDir")) {
            returnMove = childNodes.get(
                    game.getGhostLastMoveMade(Constants.GHOST.PINKY).toString()
            ).getDecision(game);
        } else if (attribute.equals("sueDir")) {
            returnMove = childNodes.get(
                    game.getGhostLastMoveMade(Constants.GHOST.SUE).toString()
            ).getDecision(game);
        } else {
            System.out.println("No decision was found at attribute: " + attribute +
                    ", number of child nodes: " + childNodes.size());
            returnMove = this.move;
        }

        return returnMove;
    }

    public void print(int depth) {
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
