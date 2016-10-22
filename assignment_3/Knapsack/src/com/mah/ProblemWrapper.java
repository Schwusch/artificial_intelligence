package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Wraps a knapsack problem with knapsacks and items
 */
public class ProblemWrapper {
    private LinkedList<Item> itemsLeft;
    private LinkedList<Item> allItems;
    private ArrayList<KnapSack> knapsacks;

    public ProblemWrapper(LinkedList<Item> itemsLeft, LinkedList<Item> allItems, ArrayList<KnapSack> knapsacks) {
        this.itemsLeft = itemsLeft;
        this.allItems = allItems;
        this.knapsacks = knapsacks;
    }

    public ProblemWrapper(LinkedList<Item> items, ArrayList<KnapSack> knapsacks) {
        this(items, (LinkedList<Item>) items.clone(), knapsacks);
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

    public int totalValue(){
        int sum = 0;
        for (KnapSack sack : knapsacks) {
            sum += sack.getTotalValue();
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------PROBLEM WRAPPER-----\n");
        sb.append("Total value: " + totalValue() + "\n\n");
        knapsacks.forEach(sb::append);
        sb.append("Remaining items not placed:\n");
        itemsLeft.forEach(sb::append);
        sb.append("--------------------------\n");
        return sb.toString();
    }

    public ProblemWrapper copy() {
        return new ProblemWrapper(
                (LinkedList<Item>)this.itemsLeft.clone(),
                (LinkedList<Item>)this.allItems.clone(),
                this.knapsacks.stream().map(KnapSack::copy).collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
