package com.mah;

public class Main {
    public static void main(String[] args) throws Exception {
        ProblemWrapper greedyProblemWrapper = new ProblemWrapper(Utilities.loadItems(), Utilities.loadKnapsacks());
        KnapSackSolver.greedyFillKnapsacks(greedyProblemWrapper);

        System.out.println(greedyProblemWrapper);

        ProblemWrapper neighborProblemWrapper = greedyProblemWrapper.copy();
        neighborProblemWrapper = KnapSackSolver.improvingNeighborSearch(neighborProblemWrapper);

        System.out.println(neighborProblemWrapper);
    }
}
