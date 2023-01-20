package UtilsClasses;

import java.util.Comparator;
import java.util.function.Function;

public class ComparatorInfosElement<T> implements Comparator<InfosElement> {
    private Function<InfosElement, T> func;

    public ComparatorInfosElement(Function<InfosElement, T> func) {
        this.func = func;
    }

    @Override
    public int compare(InfosElement elm1, InfosElement elm2) {
        return ((Comparable<T>) func.apply(elm1)).compareTo(func.apply(elm2));
    }
}