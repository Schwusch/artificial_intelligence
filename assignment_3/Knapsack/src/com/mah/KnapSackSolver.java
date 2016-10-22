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

    public static void improvingNeighborSearch(ProblemWrapper wrapper) {
        ArrayList<ProblemWrapper> neighbors = findNeighbors(wrapper);
    }

    private static ArrayList<ProblemWrapper> findNeighbors(ProblemWrapper wrapper) {
        ArrayList<ProblemWrapper> neighbors = new ArrayList<>();
        ArrayList<KnapSack> knapSacks = wrapper.getKnapsacks();

        for (KnapSack sack1 : knapSacks) {
            for (KnapSack sack2 : knapSacks) {
                if (sack1 != sack2) {
                    // TODO: Flytta paket och ha sig för att göra nya ProblemWrappers
                }
            }
        }

        return neighbors;
    }
}
