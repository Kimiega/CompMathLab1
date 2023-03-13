package com.kimiega.linal;

public class MatrixGenerator {
    private static final double max = 1000d;
    private static final double min = -1000d;

    public static Double[][] generateMatrix(int n) {
        Double[][] m = new Double[n][n];
        for (int i = 0; i < n; ++i) {
            double sum = 0d;
            for (int j = 0; j < n; ++j) {
                m[i][j] = (Math.random() * ((max - min) + 1)) + min;
                sum += Math.abs(m[i][j]);
            }
            m[i][i] = sum;
        }
        return m;
    }
    public static Double[] generateVector(int n) {
        Double[] b = new Double[n];
        for (int i = 0; i < n; ++i) {
            b[i] = (Math.random() * ((max - min) + 1)) + min;
        }
        return b;
    }
}
