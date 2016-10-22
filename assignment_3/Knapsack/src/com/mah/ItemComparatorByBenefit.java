package com.mah;

import java.util.Comparator;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Comparator for sorting items by benefit.
 */
public class ItemComparatorByBenefit implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        if (item1.benefit < item2.benefit) return 1;
        else if (item2.benefit < item1.benefit) return -1;
        else return 0;
    }
}
