package com.mah;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Represent a knapsack item.
 */
public class Item {
    public int weight;
    public int value;
    public float benefit;
    
    /**
     * Creates an item with a weight and a value. 
     * Also calculates the benefit of the item.
     * @param weight Weight of the item
     * @param value Value of the item
     */
    public Item(int weight, int value) {
        this.value = value;
        this.weight = weight;
        this.benefit = (float)value/weight;
    }
    
    /**
     * Returns a string with information about an item 
     */
    public String toString(){
        return "ITEM: weight: " + weight + ", value: " + value + ", benefit: " + benefit + "\n";
    }
}