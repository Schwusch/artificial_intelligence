package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Olle on 2016-10-22.
 */
public class KnapSackSolver {

    public static void greedyFillKnapsacks(ProblemWrapper wrapper) {
        ArrayList<KnapSack> knapsack = wrapper.getKnapsacks();
        LinkedList<Item> items = wrapper.getItems();
        items.sort(new ItemComparatorByBenefit());

    }

    public static void improvingNeighborSearch(ProblemWrapper wrapper) {

    }
}
