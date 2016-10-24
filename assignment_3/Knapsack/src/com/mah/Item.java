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

    public Item(int weight, int value) {
        this.value = value;
        this.weight = weight;
        this.benefit = (float)value/weight;
    }

    public String toString(){
        return "ITEM: weight: " + weight + ", value: " + value + ", benefit: " + benefit + "\n";
    }
}