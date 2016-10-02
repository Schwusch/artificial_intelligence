package pacman.entries.pacman;

import dataRecording.DataTuple;

import java.lang.reflect.Field;

/**
 * Created by schwusch on 2016-10-01.
 */
public interface Attribute {

    DataTuple.DiscreteTag quantizeAttribute(Field field, DataTuple tuple);
}
