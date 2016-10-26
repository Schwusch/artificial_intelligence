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
    public int id;
    
    /**
     * Creates a knapsack with some necessary attributes.
     * @param items A list containing items
     * @param constraint Weight constraint of the knapsack
     * @param totalWeight Total weight of the items in the knapsack
     * @param totalValue Total value of the items in the knapsack
     * @param id Id of the knapsack
     */
    private KnapSack(LinkedList<Item> items, int constraint, int totalWeight, int totalValue, int id) {
        this.items = items;
        this.constraint = constraint;
        this.totalWeight = totalWeight;
        this.totalValue = totalValue;
        this.id = id;
    }
    
    /**
     * Creates an empty knapsack.
     * @param constraint Weight constraint of the knapsack
     * @param id Id of the knapsack
     */
    public KnapSack(int constraint, int id) {
        this.constraint = constraint;
        this.id = id;
    }
    
    /**
     * Returns the items that a knapsack contains.
     * @return A list of items
     */
    public LinkedList<Item> getItems() {
        return items;
    }
    
    /**
     * The method controls if an item fits in a knapsack. 
     * If it does fit, it adds the item and returns true.
     * @param item The item to add in the knapsack
     * @return True if the item is added, false if not
     */
    public boolean addItem(Item item) {
        if(totalWeight + item.weight <= constraint && !items.contains(item)) {
            items.add(item);
            totalWeight += item.weight;
            totalValue += item.value;
            return true;
        } else return false;
    }
    
    /**
     * Removes an item from a knapsack.
     * @param item The item to be removed
     * @return True if the item is removed, otherwise false
     */
    public boolean removeItem(Item item) {
        if(items.remove(item)) {
            totalWeight -= item.weight;
            totalValue -= item.value;
            return true;
        } else return false;
    }
    
    /**
     * Returns the total value of all the items in the knapsack. 
     * @return Total value
     */
    public int getTotalValue() {
        return totalValue;
    }
    
    /**
     * Returns the total weight of a knapsack
     * @return Total weight
     */
    public int getTotalWeight() {
        return totalWeight;
    }
    
    /**
     * The method makes a clone of a KnapSack object.
     * @return A cloned knapsack
     */
    public KnapSack copy() {
        return new KnapSack((LinkedList<Item>)items.clone(), constraint, totalWeight, totalValue, id);
    }

    
    /**
     * Builds a string that shows information about a knapsack object.
     */
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