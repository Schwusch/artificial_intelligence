package com.mah;

public class Main {
    private static final int RANDOM_START_ITERATIONS = 1000;

    public static void main(String[] args) throws Exception {
        ProblemWrapper startingWrapper = new ProblemWrapper(Utilities.loadItems(), Utilities.loadKnapsacks());
        ProblemWrapper greedyFilledWrapper = startingWrapper.copy();

        KnapSackSolver.greedyFillKnapsacks(greedyFilledWrapper);
        System.out.println("GREEDY FILL:");
        System.out.println(greedyFilledWrapper);
        System.out.println("GREEDY START NEIGHBOR SEARCH:");
        System.out.println(KnapSackSolver.improvingNeighborSearch(greedyFilledWrapper));
        iterateRandomStarts(startingWrapper);
    }

    private static void iterateRandomStarts(ProblemWrapper startWrapper){
        ProblemWrapper bestRandomWrapper = startWrapper;

        for (int i = 0; i< RANDOM_START_ITERATIONS; i++) {
            ProblemWrapper randomProblemWrapper = startWrapper.copy();
            KnapSackSolver.randomFillKnapsacks(randomProblemWrapper);
            randomProblemWrapper = KnapSackSolver.improvingNeighborSearch(randomProblemWrapper);
            if (randomProblemWrapper.totalValue() > bestRandomWrapper.totalValue()) {
                bestRandomWrapper = randomProblemWrapper;
            }
        }

        System.out.println("Best solution during " + RANDOM_START_ITERATIONS +
                " iterations of solving neighborsearch with random start: ");
        System.out.print(bestRandomWrapper);
    }
}