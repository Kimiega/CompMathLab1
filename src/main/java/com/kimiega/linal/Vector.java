package com.kimiega.linal;

import java.util.Arrays;

public class Vector implements IVector{
    private final Double[] vector;

    public Vector(Double[] vector) {
        this.vector = vector;
    }

    public Vector(int n) {
        vector = new Double[n];
        for (int i = 0; i < n; ++i)
            vector[i] = 0d;
    }

    @Override
    public Double getCoef(int n) {
        if (vector.length < n)
            return null;
        return vector[n-1];
    }
    public Vector makeCopy() {
        return new Vector(vector.clone());
    }

    @Override
    public Boolean setCoef(int n, Double x) {
        if (vector.length < n)
            return false;
        vector[n-1] = x;
        return true;
    }
    @Override
    public int getSize() {
        return vector.length;
    }
    public Double max() {
        Double max = Double.MIN_VALUE;
        for (var x : vector)
            max = x > max ? x : max;
        return max;
    }

    @Override
    public void draw() {
        Arrays.stream(vector).forEach(s -> System.out.print(s + "\t"));
        System.out.println();
    }
}
