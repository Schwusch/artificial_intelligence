package com.mah;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Olle Caspersson and Jonathan BÃ¶cker on 2016-10-22.
 *
 * Contains methods to solve a multiple knapsack problem
 */
public class KnapSackSolver {

    public static void greedyFillKnapsacks(ProblemWrapper wrapper) {
        ArrayList<KnapSack> knapsack = wrapper.getKnapsacks();
        LinkedList<Item> items = wrapper.getItems();
        items.sort(new ItemComparatorByBenefit());
        
       LinkedList<Item> itemsToAdd = (LinkedList<Item>) items.clone();
                
        for(int i = 0; i < knapsack.size(); i++) {
        	for(int j = 0; j < itemsToAdd.size(); j++) {       	
				if (knapsack.get(i).addItem(itemsToAdd.get(j))) {
					itemsToAdd.remove(j);
				}
			}
		}
          
        for(int i = 0; i < knapsack.size(); i++) {
        		System.out.println("värde " + knapsack.get(i).getTotalValue() + " vikt: " + knapsack.get(i).getTotalWeight());
   
        }
        for(int i = 0; i < itemsToAdd.size(); i++) {
        	System.out.println(itemsToAdd.get(i));
        }
        
    }

    public static void improvingNeighborSearch(ProblemWrapper wrapper) {
        ArrayList<ProblemWrapper> neighbors = findNeighbors(wrapper);

    }

    private static ArrayList<ProblemWrapper> findNeighbors(ProblemWrapper wrapper) {
        ArrayList<ProblemWrapper> neighbors = new ArrayList<>();

        return neighbors;
    }
}
