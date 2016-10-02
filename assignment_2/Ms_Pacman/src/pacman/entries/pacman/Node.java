package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.HashMap;

/**
 * Created by schwusch on 2016-10-01.
 */
public class Node {
    private String attribute;
    private HashMap<String, Node> childNodes;
    private boolean isLeafNode = false;
    private Constants.MOVE move = Constants.MOVE.NEUTRAL;

    public Node(String attribute) {
        this.attribute = attribute;
    }

    public void addChildNode(String value, Node childNode) {
        childNodes.put(value, childNode);
    }

    public Constants.MOVE getDecision(Game game) {
        Constants.MOVE returnMove;
        if(isLeafNode) returnMove = move;
        else if(attribute.equals("isBlinkyEdible")){
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.BLINKY)
                    )
            ).getDecision(game);
        } else if (attribute.equals("isInkyEdible")){
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.INKY)
                    )
            ).getDecision(game);
        } else if (attribute.equals("isPinkyEdible")){
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.PINKY)
                    )
            ).getDecision(game);
        } else if (attribute.equals("isSueEdible")){
            returnMove = childNodes.get(
                    String.valueOf(
                            game.isGhostEdible(Constants.GHOST.SUE)
                    )
            ).getDecision(game);
        } else if (attribute.equals("blinkyDist")){
            returnMove = childNodes.get(
                    getDiscreteTag(Constants.GHOST.BLINKY, game)
            ).getDecision(game);
        } else if (attribute.equals("inkyDist")){
            returnMove = childNodes.get(
                    getDiscreteTag(Constants.GHOST.INKY, game)
            ).getDecision(game);
        } else if (attribute.equals("pinkyDist")){
            returnMove = childNodes.get(
                    getDiscreteTag(Constants.GHOST.PINKY, game)
            ).getDecision(game);
        } else if (attribute.equals("sueDist")){
            returnMove = childNodes.get(
                    getDiscreteTag(Constants.GHOST.SUE, game)
            ).getDecision(game);
        }
        else {
            System.out.println("No decision was found at attribute: " + attribute +
                    ", number of child nodes: " + childNodes.size());
            returnMove = this.move;
        }

        return returnMove;
    }

    public void setLeafNode(Constants.MOVE move) {
        this.move = move;
        this.isLeafNode = true;
    }

    private String getDiscreteTag(Constants.GHOST ghost, Game game){
        int shortestDistance = game.getShortestPathDistance(
                game.getPacmanCurrentNodeIndex(),
                game.getGhostCurrentNodeIndex(ghost));

        if (shortestDistance == -1){
            return DataTuple.DiscreteTag.NONE.toString();
        } else {
            return DataTuple.DiscreteTag.DiscretizeDouble(
                    (double)game.getShortestPathDistance(
                            game.getPacmanCurrentNodeIndex(),
                            game.getGhostCurrentNodeIndex(ghost)
                    ) / 150d
            ).toString();
        }
    }
}
