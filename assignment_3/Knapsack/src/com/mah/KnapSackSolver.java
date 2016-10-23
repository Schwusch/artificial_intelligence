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
        ProblemWrapper neighbor = wrapper;
        do {
            ProblemWrapper neighborMaybe = findBestNeighbor(neighbor.copy());
            if(neighborMaybe != null && neighborMaybe.totalValue() > neighbor.totalValue()) {
                neighbor = neighborMaybe;
            }
        } while(neighbor.totalValue() > wrapper.totalValue());

        return neighbor;
    }

    private static ProblemWrapper findBestNeighbor(ProblemWrapper wrapper) {
        ProblemWrapper bestNeighbor = null;
        ArrayList<KnapSack> knapSacks = wrapper.getKnapsacks();

        // Try all permutations of knapsack item moves to find neighbors
        for (KnapSack sack1 : knapSacks) {
            for (KnapSack sack2 : knapSacks) {
                if (sack1 != sack2) {
                    Iterator<Item> sack1ItemIter = ((LinkedList<Item>)sack1.getItems().clone()).iterator();
                    while (sack1ItemIter.hasNext()) {
                        Item item = sack1ItemIter.next();
                        // If the item fits the other sack
                        if (sack2.addItem(item)) {
                            sack1ItemIter.remove();
                            // Add another item to the first sack
                            Iterator<Item> itemLeftIter = ((LinkedList<Item>)wrapper.getItemsLeft().clone()).iterator();
                            while (itemLeftIter.hasNext()) {
                                if (sack1.addItem(itemLeftIter.next())) {
                                    itemLeftIter.remove();
                                    // TODO: This is a valid neighbor?
                                }
                            }
                        // If the item does NOT fit the other sack, remove one, then add it
                        } else {
                            Iterator<Item> sack2ItemIter = ((LinkedList<Item>)sack2.getItems().clone()).iterator();
                            while (sack2ItemIter.hasNext()) {
                                sack2ItemIter.next();
                            }
                        }
                    }
                }
            }
        }

        return bestNeighbor;
    }
}
