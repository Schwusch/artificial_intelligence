package com.mah;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    private static final int RESTRAINT = 12;
    private static final int KNAPSACKS = 3;

    public static void main(String[] args) throws Exception {
        ProblemWrapper problemWrapper = new ProblemWrapper(loadItems(), loadKnapsacks());
        KnapSackSolver.greedyFillKnapsacks(problemWrapper);
        System.out.println(problemWrapper);
        KnapSackSolver.improvingNeighborSearch(problemWrapper);
    }

    /*
    Loads items from text file
     */
    private static LinkedList<Item> loadItems() throws Exception{
        LinkedList<Item> items = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader("items.txt"));
        String line = br.readLine();

        while (line != null) {
            if(!line.startsWith("#")) {
                String[] values = line.split(",");
                items.add(new Item(Integer.parseInt(values[0]),
                        Integer.parseInt(values[1])));
            }
            line = br.readLine();
        }
        return items;
    }

    /*
    Creates some knapsacks
     */
    private static ArrayList<KnapSack> loadKnapsacks() {
        ArrayList<KnapSack> knapsacks = new ArrayList<>();

        for (int i = 0; i < KNAPSACKS; i++) {
            knapsacks.add(new KnapSack(RESTRAINT));
        }
        return knapsacks;
    }
}
