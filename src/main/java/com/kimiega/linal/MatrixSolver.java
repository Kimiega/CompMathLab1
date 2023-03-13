package com.kimiega.linal;

import java.util.List;

import com.kimiega.utils.Error;

public class MatrixSolver {
    private final MatrixPreSolver matrixPreSolver;
    public MatrixSolver() {
        matrixPreSolver = new MatrixPreSolver();
    }

    public int solve(Matrix matrix, double eps, Vector solution, Vector inaccuracy, List<Error> errors) {
        if (!preSolve(matrix, errors))
            return -1;
        else
            System.out.println("Перестановка для диагонального преобладания найдена");
        for (int i = 0; i < matrix.getSize(); ++i)
            solution.setCoef(i+1, matrix.getFreeCoef(i+1));
        int k = iterate(matrix, solution, inaccuracy, 0, eps);
        return k;
    }

    public boolean preSolve(Matrix matrix, List<Error> errors) {
        int k = matrixPreSolver.bringMatrix(matrix);
        if (k == -1) {
            errors.add(new Error("Перестановка для диагонального преобладания не найдена"));
            return false;
        }
//        Double det = matrixPreSolver.calcDet(matrix);
//        if (k % 2 == 1)
//            det *= -1;
//        if (det == 0) {
//            errors.add(new Error("Нет решений или решений бесконечно много"));
//            return false;
//        }
        return true;
    }
    private int iterate(Matrix matrix, Vector solution, Vector inaccuracy, int k, double eps) {
        if (inaccuracy.max() < eps && k > 0)
            return k;
        Vector newSolution = calcNewSolution(matrix, solution);
        fillInaccuracy(solution, newSolution, inaccuracy);
        fillSolution(solution, newSolution);
        return iterate(matrix, solution, inaccuracy, k+1, eps);
    }

    private Vector calcNewSolution(Matrix matrix, Vector solution) {
        Vector newSolution = new Vector(solution.getSize());
        for (int i = 0; i < newSolution.getSize(); ++i) {
            double xi = matrix.getFreeCoef(i+1) / matrix.getCoef(i+1, i+1);
            for (int j = 0; j < newSolution.getSize(); ++j) {
                if (i == j)
                    continue;
                xi -= matrix.getCoef(i+1, j+1) * solution.getCoef(j+1) / matrix.getCoef(i+1, i+1);
            }
            newSolution.setCoef(i+1, xi);
        }
        return newSolution;
    }

    private void fillInaccuracy(Vector solution, Vector newSolution, Vector inaccuracy) {
        for (int i = 0; i < inaccuracy.getSize(); ++i) {
            inaccuracy.setCoef(i+1, Math.abs(solution.getCoef(i+1)- newSolution.getCoef(i+1)));
        }
    }
    private void fillSolution(Vector solution, Vector newSolution) {
        for (int i = 0; i <solution.getSize(); ++i) {
            solution.setCoef(i+1, newSolution.getCoef(i+1));
        }
    }
}
