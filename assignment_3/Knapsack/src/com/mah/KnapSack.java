package com.mah;

import java.util.LinkedList;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Represents a knapsack.
 */
public class KnapSack {
    private LinkedList<Item> items = new LinkedList<>();
    private int constraint;
    private int totalWeight = 0;
    private int totalValue = 0;

    public KnapSack(int constraint) {
        this.constraint = constraint;
    }

    public boolean addItem(Item item) {
        if(totalWeight + item.weight <= constraint) {
            items.add(item);
            totalWeight += item.weight;
            totalValue += item.value;
            return true;
        } else return false;
    }

    public boolean removeItem(Item item) {
        if(items.remove(item)) {
            totalWeight -= item.weight;
            totalValue -= item.value;
            return true;
        } else return false;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Knapsack weight: " + totalWeight + "\n");
        sb.append("Knapsack value: " + totalValue + "\n");
        sb.append("Items in knapsack:\n");
        items.forEach(sb::append);
        return sb.toString();
    }
}
