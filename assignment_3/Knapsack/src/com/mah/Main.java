package com.mah;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws Exception {
        ProblemWrapper greedyProblemWrapper = new ProblemWrapper(Utilities.loadItems(), Utilities.loadKnapsacks());
        KnapSackSolver.greedyFillKnapsacks(greedyProblemWrapper);

        System.out.println(greedyProblemWrapper);

        ProblemWrapper neighborProblemWrapper = greedyProblemWrapper.copy();
        KnapSackSolver.improvingNeighborSearch(neighborProblemWrapper);

        System.out.println(neighborProblemWrapper);
    }
}
