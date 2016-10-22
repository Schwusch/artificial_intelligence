package com.mah;

import java.util.Comparator;

/**
 * Created by schwusch on 2016-10-22.
 */
public class ItemComparatorByBenefit implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        float item1benefit = (float) item1.value / item1.weight;
        float item2benefit = (float) item2.value / item2.weight;
        if (item1benefit < item2benefit) return 1;
        else if (item2benefit < item1benefit) return -1;
        else return 0;
    }
}
