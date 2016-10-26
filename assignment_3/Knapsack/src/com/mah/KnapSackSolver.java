package com.mah;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Contains methods to solve a multiple knapsack problem
 */
public class KnapSackSolver {

    /**
     * Fill the knapsacks with a randomly shuffled list.
     * This is a destructive method! The parameter will be altered!
     *
     * @param wrapper The solution with empty knapsacks
     */
    public static void randomFillKnapsacks(ProblemWrapper wrapper) {
        Collections.shuffle(wrapper.getItemsLeft());
        fillKnapsacks(wrapper.getKnapsacks(), wrapper.getItemsLeft());
    }

    /**
     * Fill the knapsacks with a greedy sorted list.
     * This is a destructive method! The parameter will be altered!
     *
     * @param wrapper
     */
    public static void greedyFillKnapsacks(ProblemWrapper wrapper) {
        wrapper.getItemsLeft().sort(new ItemComparatorByBenefit());
        fillKnapsacks(wrapper.getKnapsacks(), wrapper.getItemsLeft());
    }

    /*
    Fill knapsacks in order to the max
     */
    private static void fillKnapsacks(ArrayList<KnapSack> knapSacks, LinkedList<Item> itemsToAdd) {
        for (KnapSack sack : knapSacks) {
            Iterator<Item> iter = itemsToAdd.iterator();
            while (iter.hasNext())
                if(sack.addItem(iter.next()))
                    iter.remove();
        }
    }

    /**
     * Tries to improve a solution with a greedy neighbor search
     *
     * @param wrapper The solution to improve.
     * @return The best found solution.
     */
    public static ProblemWrapper improvingNeighborSearch(ProblemWrapper wrapper) {
        ProblemWrapper bestNeighbor = wrapper;
        ProblemWrapper lastNeighbor = wrapper;
        do {
            if(lastNeighbor.totalValue() > bestNeighbor.totalValue() ) {
                bestNeighbor = lastNeighbor;
            }
            lastNeighbor = findBestNeighbor(bestNeighbor.copy());
        } while(lastNeighbor.totalValue() > bestNeighbor.totalValue());

        return bestNeighbor;
    }

    /*
    Finds the next best neighbor to a solution.
    Returns the best found neighbor.
     */
    private static ProblemWrapper findBestNeighbor(ProblemWrapper originalState) {
        ProblemWrapper bestNeighbor = originalState;
        ArrayList<KnapSack> knapSacks = originalState.getKnapsacks();

        // Try all permutations of knapsack item moves to find neighbors
        for (KnapSack fromSack : knapSacks) {
            // Try to simply add a non-included item to a sack
            ProblemWrapper newNeighborNonIncluded = addNonIncludedToKnapsack(fromSack, originalState);
            if(newNeighborNonIncluded != null && newNeighborNonIncluded.totalValue() > bestNeighbor.totalValue()) {
                // We've found a better neighbor!
                bestNeighbor = newNeighborNonIncluded;
            }
            for (KnapSack toSack : knapSacks) {
                if (fromSack != toSack) {
                    for (Item fromSackItem : fromSack.getItems()){
                        // Try to put an item from one sack into another
                        ProblemWrapper newNeighbor = addToKnapsack(fromSackItem, fromSack, toSack, originalState);
                        if (newNeighbor == null) {
                            // If it doesn't fit, try to remove one item then try again
                            for (Item toSackItem : toSack.getItems()) {
                                KnapSack toSackCopy = toSack.copy();
                                toSackCopy.removeItem(toSackItem);
                                newNeighbor = addToKnapsack(fromSackItem, fromSack, toSackCopy, originalState);
                                if (newNeighbor != null && newNeighbor.totalValue() > bestNeighbor.totalValue()) {
                                    // We've found a better neighbor!
                                    bestNeighbor = newNeighbor;
                                }
                            }
                        } else if(newNeighbor.totalValue() > bestNeighbor.totalValue()) {
                            // We've found a better neighbor!
                            bestNeighbor = newNeighbor;
                        }
                    }
                }
            }
        }

        return bestNeighbor;
    }

    /*
    Tries all combinations of adding a non-included item to fromSack if adding the item to toSack is successful.
    Returns the best combination as a neighbor, returns null if none was successful.
     */
    private static ProblemWrapper addToKnapsack(Item item, KnapSack fromSack, KnapSack toSack, ProblemWrapper originalState) {
        ProblemWrapper bestNeighbor = null;
        KnapSack toSackCopy = toSack.copy();
        if (toSackCopy.addItem(item)) {
            // If adding item to other sack was successful, remove it from the first sack
            KnapSack fromSackCopy = fromSack.copy();
            fromSackCopy.removeItem(item);
            // Try adding a non-included item to the first sack
            for (Item itemNoSack : originalState.getItemsLeft()) {
                KnapSack fromSackCopyCopy = fromSackCopy.copy();
                if(fromSackCopyCopy.addItem(itemNoSack)) {
                    // If adding an item to the first sack was successful, save the state as a wrapper
                    ProblemWrapper newNeighbor = originalState.copy();
                    newNeighbor.getKnapsacks().remove(newNeighbor.getKnapsackById(fromSackCopyCopy.id));
                    newNeighbor.getKnapsacks().add(fromSackCopyCopy);
                    newNeighbor.getKnapsacks().remove(newNeighbor.getKnapsackById(toSackCopy.id));
                    newNeighbor.getKnapsacks().add(toSackCopy);
                    newNeighbor.getItemsLeft().remove(itemNoSack);
                    
                    if (bestNeighbor == null || newNeighbor.totalValue() > bestNeighbor.totalValue()) {
                        // We've found a local best neighbor!
                        bestNeighbor = newNeighbor;
                    }
                }
            }
        }

        return bestNeighbor;
    }

    private static ProblemWrapper addNonIncludedToKnapsack(KnapSack toSack, ProblemWrapper originalState) {
        ProblemWrapper bestNeighbor = null;
        for (Item itemNoSack : originalState.getItemsLeft()) {
            KnapSack toSackCopy = toSack.copy();
            if(toSackCopy.addItem(itemNoSack)) {
                ProblemWrapper newNeighbor = originalState.copy();
                newNeighbor.getKnapsacks().remove(newNeighbor.getKnapsackById(toSackCopy.id));
                newNeighbor.getKnapsacks().add(toSackCopy);
                newNeighbor.getItemsLeft().remove(itemNoSack);

                if (bestNeighbor == null || newNeighbor.totalValue() > bestNeighbor.totalValue()) {
                    // We've found a local best neighbor!
                    bestNeighbor = newNeighbor;
                }
            }
        }
        return bestNeighbor;
    }
}