package pacman.entries.pacman;

import dataRecording.DataTuple;

import java.util.LinkedList;

/**
 * Created by Jonathan Böcker on 2016-10-20.
 *
 * Wrapper to hold a subset and its value name
 */
public class SubSetWrapper {
    public String name;
    public LinkedList<DataTuple> subset;

    public SubSetWrapper(String name, LinkedList<DataTuple> subset){
        this.name = name;
        this.subset = subset;
    }
}
