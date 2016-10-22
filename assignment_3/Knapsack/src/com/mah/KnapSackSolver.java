package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Contains methods to solve a multiple knapsack problem
 */
public class KnapSackSolver {

    public static void greedyFillKnapsacks(ProblemWrapper wrapper) {
        ArrayList<KnapSack> knapsack = wrapper.getKnapsacks();
        LinkedList<Item> items = wrapper.getItems();
        items.sort(new ItemComparatorByBenefit());

    }

    public static void improvingNeighborSearch(ProblemWrapper wrapper) {
        ArrayList<ProblemWrapper> neighbors = findNeighbors(wrapper);

    }

    private static ArrayList<ProblemWrapper> findNeighbors(ProblemWrapper wrapper) {
        ArrayList<ProblemWrapper> neighbors = new ArrayList<>();

        return neighbors;
    }
}
