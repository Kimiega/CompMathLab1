package com.kimiega;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;

import com.kimiega.linal.Matrix;
import com.kimiega.linal.MatrixGenerator;
import com.kimiega.linal.MatrixSolver;
import com.kimiega.linal.Vector;
import com.kimiega.utils.Error;

public class Main {
    private static final String RANDOM_TAG = "--random";
    private static final String COMMA_TAG = "--comma";

    private static final Predicate<String> validateArgs = (String s) -> !s.equals(RANDOM_TAG) && !s.equals(COMMA_TAG);
    public static void main(String[] args) {
        boolean isFromFile = false;
        boolean isRandom = Arrays.asList(args).contains(RANDOM_TAG);
        boolean isWithComma = Arrays.asList(args).contains(COMMA_TAG);
        int n;
        double eps;
        Double[][] x;
        Double[] b;
        Scanner input;
        try {
            if (Arrays.stream(args).anyMatch(validateArgs)) {
                    input = new Scanner(new File(Arrays.stream(args).filter(validateArgs).findFirst().get()));
                isFromFile = true;
            } else
                input = new Scanner(System.in);
            if (isWithComma)
                input.useLocale(Locale.FRENCH);
            else
                input.useLocale(Locale.US);
            if (!isFromFile)
                System.out.print("Введите размер матрицы: ");
            n = input.nextInt();
            if (!isFromFile)
                System.out.print("Введите точность: ");
            eps = input.nextDouble();
            if (eps <= 0)
                throw new InputMismatchException();
            if (!isFromFile && !isRandom)
                System.out.println("Введите матрицу: ");
            if (!isRandom) {
                x = new Double[n][n];
                b = new Double[n];
                for (int i = 0; i < n; ++i) {
                    for (int j = 0; j < n; ++j)
                        x[i][j] = input.nextDouble();
                    b[i] = input.nextDouble();
                }
            }
            else {
                x = MatrixGenerator.generateMatrix(n);
                b = MatrixGenerator.generateVector(n);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            return;
        } catch (InputMismatchException ex) {
            System.err.println("Неправильный формат ввода");
            return;
        } catch (NoSuchElementException ex) {
            System.err.println("Введены не все данные");
            return;
        }
        input.close();


        Matrix matrix = new Matrix(x, new Vector(b));
        System.out.println("Введенная матрица");
        matrix.drawMatrix();
        System.out.println();
        Vector solution = new Vector(matrix.getSize());
        Vector inaccuracy = new Vector(matrix.getSize());
        MatrixSolver matrixSolver = new MatrixSolver();
        List<Error> errors = new ArrayList<>();
        int k = matrixSolver.solve(matrix, eps, solution, inaccuracy, errors);
        if (errors.size() > 0)
            errors.forEach(s -> System.err.println(s.getMessage()));
        else {
            System.out.println("Вектор неизвестных:");
            solution.draw();
            System.out.println("Количество итераций:");
            System.out.println(k);
            System.out.println("Вектор погрешностей:");
            inaccuracy.draw();
        }
    }
}