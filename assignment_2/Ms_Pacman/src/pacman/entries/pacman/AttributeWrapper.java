package pacman.entries.pacman;

import dataRecording.DataTuple;

import java.util.ArrayList;

/**
 * Created by Jonathan Böcker on 2016-10-20.
 *
 * Wrapper for an attribute to hold its subsets and gainratio
 */
public class AttributeWrapper {
    public String name;
    public ArrayList<SubSetWrapper> subsets = new ArrayList<>();
    public Double gainRatio;

    public AttributeWrapper(String name){
        this.name = name;
    }

    public void addSubset(SubSetWrapper subSetWrapper) {
        subsets.add(subSetWrapper);
    }

}
