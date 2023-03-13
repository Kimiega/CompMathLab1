package com.kimiega.linal;

import java.util.Arrays;

public class Matrix implements IMatrix {
    private Double[][] matrix;
    private Vector freeCoefs;

    private Matrix() {}

    public Matrix(int n) {
        matrix = new Double[n][n];
        freeCoefs = new Vector(n);
    }

    public Matrix(Double[][] matrix, Vector freeCoefs) {
        this.matrix = matrix;
        this.freeCoefs = freeCoefs;
    }

    private boolean isIndexesSuitable(int n, int m) {
        if (n <= 0 && m <= 0)
            return false;
        if (n > matrix.length)
            return false;
        if (m > matrix[0].length)
            return false;

        return true;
    }
    @Override
    public Double getCoef(int n, int m) {
        if (isIndexesSuitable(n, m))
            return matrix[n-1][m-1];
        return null;
    }

    @Override
    public boolean setCoef(int n, int m, Double x) {
        if (isIndexesSuitable(n, m)) {
            matrix[n-1][m-1] = x;
            return true;
        }
        return false;
    }

    @Override
    public boolean swapLines(int k, int p) {
        if (isIndexesSuitable(k, 1) && isIndexesSuitable(p, 1)) {
            for (int i = 0; i < matrix[0].length; ++i) {
                Double temp = matrix[k-1][i];
                matrix[k-1][i] = matrix[p-1][i];
                matrix[p-1][i] = temp;
            }
            Double temp = getFreeCoef(k);
            setFreeCoef(k, getFreeCoef(p));
            setFreeCoef(p, temp);
            return true;
        }
        return false;
    }

    public Matrix makeCopy() {
        Double[][] newMatrix = new Double[matrix.length][];
        Vector newFreeCoefs = freeCoefs.makeCopy();
        for(int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i].clone();
        }
        return new Matrix(newMatrix, newFreeCoefs);
    }
    public int getSize() {
        return matrix.length;
    }

    @Override
    public void drawMatrix() {
        int i = 1;
        for (Double[] doubles : matrix) {
            Arrays.stream(doubles).forEach(c -> System.out.print(c + "\t"));
            System.out.print(" | " + getFreeCoef(i));
            i++;
            System.out.println();
        }
    }

    public Double getFreeCoef(int n) {
        return freeCoefs.getCoef(n);
    }

    public Boolean setFreeCoef(int n, Double x) {
        return freeCoefs.setCoef(n, x);
    }
}
