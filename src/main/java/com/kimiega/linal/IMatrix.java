package com.kimiega.linal;

public interface IMatrix {

    Double getCoef(int n, int m);

    boolean setCoef(int n, int m, Double x);

    boolean swapLines(int k, int p);

    void drawMatrix();
}
