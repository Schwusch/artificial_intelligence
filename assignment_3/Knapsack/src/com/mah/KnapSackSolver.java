package com.mah;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Contains methods to solve a multiple knapsack problem
 */
public class KnapSackSolver {

    public static void greedyFillKnapsacks(ProblemWrapper wrapper) {
        ArrayList<KnapSack> knapSacks = wrapper.getKnapsacks();
        LinkedList<Item> itemsToAdd = wrapper.getItemsLeft();
        itemsToAdd.sort(new ItemComparatorByBenefit());

        for (KnapSack sack : knapSacks) {
            Iterator<Item> iter = itemsToAdd.iterator();
            while (iter.hasNext())
                if(sack.addItem(iter.next()))
                    iter.remove();
        }
    }

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

    private static ProblemWrapper findBestNeighbor(ProblemWrapper wrapper) {
        ProblemWrapper bestNeighbor = wrapper;
        ArrayList<KnapSack> knapSacks = wrapper.getKnapsacks();

        // Try all permutations of knapsack item moves to find neighbors
        for (KnapSack fromSack : knapSacks) {
            for (KnapSack toSack : knapSacks) {
                if (fromSack != toSack) {
                    Iterator<Item> fromSackItemIter = ((LinkedList<Item>)fromSack.getItems().clone()).iterator();
                    while (fromSackItemIter.hasNext()) {
                        Item item = fromSackItemIter.next();
                        // If the item fits the other sack
                        ProblemWrapper newNeighbor = addToKnapsack(item, fromSack, toSack, wrapper);
                        if (newNeighbor == null) {
                            //TODO
                        }
                    }
                }
            }
        }

        return bestNeighbor;
    }

    private static ProblemWrapper addToKnapsack(Item item, KnapSack fromSack, KnapSack toSack, ProblemWrapper currentState) {
        ProblemWrapper bestNeighbor = null;
        if (toSack.addItem(item)) {
            LinkedList<Item> fromSackItemsClone = (LinkedList<Item>)fromSack.getItems().clone();
            if (!fromSackItemsClone.remove(item)) {
                System.out.println("Could not remove item...");
            }
            // Add another item to the first sack
            Iterator<Item> itemLeftIter = ((LinkedList<Item>) currentState.getItemsLeft().clone()).iterator();
            while (itemLeftIter.hasNext()) {
                if (fromSack.addItem(itemLeftIter.next())) {
                    itemLeftIter.remove();
                    // TODO: This is a valid neighbor?
                }
            }
        }

        return bestNeighbor;
    }
}
