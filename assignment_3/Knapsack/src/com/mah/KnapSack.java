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

    private KnapSack(LinkedList<Item> items, int constraint, int totalWeight, int totalValue) {
        this.items = items;
        this.constraint = constraint;
        this.totalWeight = totalWeight;
        this.totalValue = totalValue;
    }

    public KnapSack(int constraint) {
        this.constraint = constraint;
    }

    public LinkedList<Item> getItems() {
        return items;
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

    public KnapSack copy() {
        return new KnapSack((LinkedList<Item>)items.clone(), constraint, totalWeight, totalValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KNAPSACK:\n");
        sb.append("Weight: " + totalWeight + "\n");
        sb.append("Value: " + totalValue + "\n");
        sb.append("Contains " + items.size() + " items:\n");
        items.forEach(sb::append);
        sb.append("\n");
        return sb.toString();
    }
}
