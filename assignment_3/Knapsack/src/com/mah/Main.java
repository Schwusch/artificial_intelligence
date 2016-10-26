package com.mah;

import java.util.LinkedList;

public class Main {
    private static final int RANDOM_START_ITERATIONS = 100;

    public static void main(String[] args) throws Exception {
        ProblemWrapper startingWrapper = new ProblemWrapper(Utilities.loadItems(), Utilities.loadKnapsacks());
        ProblemWrapper greedyFilledWrapper = startingWrapper.copy();

        KnapSackSolver.greedyFillKnapsacks(greedyFilledWrapper);

        System.out.println("GREEDY FILL:");
        System.out.println(greedyFilledWrapper);
        System.out.println("GREEDY START NEIGHBOR SEARCH:");

        greedyFilledWrapper = KnapSackSolver.improvingNeighborSearch(greedyFilledWrapper);

        System.out.println(greedyFilledWrapper);
        validateSolution(greedyFilledWrapper);

        iterateRandomStarts(startingWrapper);
    }

    private static void iterateRandomStarts(ProblemWrapper startWrapper) throws Exception {
        ProblemWrapper bestRandomWrapper = startWrapper;

        for (int i = 0; i< RANDOM_START_ITERATIONS; i++) {
            ProblemWrapper randomProblemWrapper = startWrapper.copy();
            KnapSackSolver.randomFillKnapsacks(randomProblemWrapper);
            randomProblemWrapper = KnapSackSolver.improvingNeighborSearch(randomProblemWrapper);
            if (randomProblemWrapper.totalValue() > bestRandomWrapper.totalValue()) bestRandomWrapper = randomProblemWrapper;
        }

        System.out.println("Best solution during " + RANDOM_START_ITERATIONS +
                " iterations of solving neighborsearch with random start: ");
        System.out.print(bestRandomWrapper);
        validateSolution(bestRandomWrapper);
    }

    private static void validateSolution(ProblemWrapper solution) throws Exception {
        LinkedList<Item> allItems = solution.getAllItems();
        boolean anyDuplicateItem = false;
        for (Item item : allItems) {
            boolean foundItemOnce = false;
            for (KnapSack sack : solution.getKnapsacks()) {
                if (sack.removeItem(item)) {
                    if (foundItemOnce) anyDuplicateItem = true;
                    else foundItemOnce = true;
                }
            }
        }
        if (anyDuplicateItem) System.out.println("There was duplicated item(s) in the solution!");
        else System.out.println("No duplicates were found in the solution.\n");
    }
}