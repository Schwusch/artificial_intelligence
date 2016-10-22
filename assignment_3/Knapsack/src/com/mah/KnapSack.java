package com.mah;

import java.util.LinkedList;

/**
 * Created by schwusch on 2016-10-22.
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

    public int getTotalValue() {
        return totalValue;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        items.forEach(sb::append);
        return sb.toString();
    }
}
