package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Wraps a knapsack problem with knapsacks and items
 */
public class ProblemWrapper {
    private LinkedList<Item> itemsLeft;
    private LinkedList<Item> allItems;
    private ArrayList<KnapSack> knapsacks;

    public ProblemWrapper(LinkedList<Item> items, ArrayList<KnapSack> knapsacks) {
        this.allItems = items;
        this.itemsLeft = (LinkedList<Item>) items.clone();
        this.knapsacks = knapsacks;
    }

    public LinkedList<Item> getItemsLeft() {
        return itemsLeft;
    }
    public LinkedList<Item> getAllItems() {
        return allItems;
    }

    public ArrayList<KnapSack> getKnapsacks() {
        return knapsacks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------PROBLEM WRAPPER-----\n");
        knapsacks.forEach(sb::append);
        sb.append("Remaining items not placed:\n");
        itemsLeft.forEach(sb::append);
        sb.append("--------------------------\n");
        return sb.toString();
    }
}
