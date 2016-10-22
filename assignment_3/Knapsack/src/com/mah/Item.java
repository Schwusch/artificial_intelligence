package com.mah;

/**
 * Created by schwusch on 2016-10-22.
 */
public class Item {
    public int weight;
    public int value;

    public Item(int weight, int value) {
        this.value = value;
        this.weight = weight;
    }

    public String toString(){
        return "weight: " + weight + ", value: " + value + ", benefit: " + (float)value/weight + "\n";
    }
}
