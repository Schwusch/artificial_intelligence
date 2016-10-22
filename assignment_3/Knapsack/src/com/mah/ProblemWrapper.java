package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by schwusch on 2016-10-22.
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
}
