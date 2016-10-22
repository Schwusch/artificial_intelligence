package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Wraps a knapsack problem with knapsacks and items
 */
public class ProblemWrapper {
    private LinkedList<Item> items;
    private ArrayList<KnapSack> knapsacks;

    public ProblemWrapper(LinkedList<Item> items, ArrayList<KnapSack> knapsacks) {
        this.items = items;
        this.knapsacks = knapsacks;
    }

    public LinkedList<Item> getItems() {
        return items;
    }

    public ArrayList<KnapSack> getKnapsacks() {
        return knapsacks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(KnapSack sack : knapsacks) sb.append(sack + "\n");
        sb.append("Remaining items not placed:\n");
        for(Item item : items) sb.append(item);
        return sb.toString();
    }
}
