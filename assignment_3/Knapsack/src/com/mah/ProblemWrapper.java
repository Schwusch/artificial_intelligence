package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by Olle Caspersson and Jonathan Böcker on 2016-10-22.
 *
 * Wraps a knapsack problem with knapsacks and items
 */
public class ProblemWrapper {
    private LinkedList<Item> itemsLeft;
    private LinkedList<Item> allItems;
    private ArrayList<KnapSack> knapsacks;
    /**
     * Creates a wrapper containing lists of knapsacks and items
     * @param itemsLeft A {@link LinkedList<Item>}
     * @param allItems A {@link LinkedList<Item>}
     * @param knapsacks A {@link ArrayList<KnapSack>}
     */
    public ProblemWrapper(LinkedList<Item> itemsLeft, LinkedList<Item> allItems, ArrayList<KnapSack> knapsacks) {
        this.itemsLeft = itemsLeft;
        this.allItems = allItems;
        this.knapsacks = knapsacks;
    }
    
    /**
     * Creates a wrapper containing lists of knapsacks and items
     * @param items A {@link LinkedList<Item>}
     * @param knapsacks {@link ArrayList<KnapSack>}
     */
    public ProblemWrapper(LinkedList<Item> items, ArrayList<KnapSack> knapsacks) {
        this(items, (LinkedList<Item>) items.clone(), knapsacks);
    }
    
    /**
     * Returns a list of remaining items
     * @return remaining items 
     */
    public LinkedList<Item> getItemsLeft() {
        return itemsLeft;
    }
    
    /**
     * Returns a list of all items
     * @return all items
     */
    public LinkedList<Item> getAllItems() {
        return allItems;
    }
    
    /**
     * Returns a list of KnapSack objects
     * @return knapsacks
     */
    public ArrayList<KnapSack> getKnapsacks() {
        return knapsacks;
    }
    
    /**
     * Calculates and returns the total value of all the knapsacks 
     * @return value of all knapsacks
     */
    public int totalValue(){
        int sum = 0;
        for (KnapSack sack : knapsacks) {
            sum += sack.getTotalValue();
        }
        return sum;
    }
    
    /**
     * Takes an id and returns the knapsack whose id matches the received. 
     * @param id An id
     * @return The knapsack that matches the id. Null if no knapsack matches the id.
     */
    public KnapSack getKnapsackById(int id){
        for (KnapSack sack : knapsacks) {
            if (sack.id == id) {
                return sack;
            }
        }
        return null;
    }
    
    /**
     * Returns a string containing information of a wrapper
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------PROBLEM WRAPPER--------------\n");
        sb.append("Total value: " + totalValue() + "\n\n");
        knapsacks.forEach(sb::append);
        sb.append("Remaining items not placed:\n");
        itemsLeft.forEach(sb::append);
        sb.append("--------------------------------------------\n");
        return sb.toString();
    }
    
    /**
     * Method that clones a wrapper
     * @return A cloned wrapper
     */
    public ProblemWrapper copy() {
        return new ProblemWrapper(
                (LinkedList<Item>)this.itemsLeft.clone(),
                (LinkedList<Item>)this.allItems.clone(),
                this.knapsacks.stream().map(KnapSack::copy).collect(Collectors.toCollection(ArrayList::new))
        );
    }
}