package com.kimiega.linal;

import java.util.HashSet;
import java.util.Set;

public class MatrixPreSolver {
    public MatrixPreSolver() {
    }

    public Double calcDet(Matrix matrix) {
        if (matrix.getSize() == 1)
            return matrix.getCoef(1, 1);
        if (matrix.getSize() == 2)
            return matrix.getCoef(1,1) + matrix.getCoef(2, 2) - matrix.getCoef(1, 2) - matrix.getCoef(2, 1);
        double det = 0d;
        for (int k = 0; k < matrix.getSize(); ++k) {
            //Создаем и заполняем минор
            Matrix newMatrix = new Matrix(matrix.getSize()-1);
            for (int p = 1; p < matrix.getSize(); ++p) {
                int d = 0;
                for (int h = 0; h < matrix.getSize(); ++h) {
                    if (h == k) {
                        d = 1;
                        continue;
                    }
                    newMatrix.setCoef(p, h+1-d, matrix.getCoef(p+1, h+1));
                }
            }
            double x = (1 + k + 1) % 2 == 0 ? 1d : -1d;
            x *=  matrix.getCoef(1, k+1) * calcDet(newMatrix);
            det += x;
        }
        return det;
    }
    public Boolean diagonalElementsPredominance(Matrix matrix) {
        boolean strictnessFlag = false;
        boolean isConverged = true;
        for (int i = 0; i < matrix.getSize(); ++i) {
            Double sum = 0D;
            for (int j = 0; j < matrix.getSize(); ++j) {
                if (i == j)
                    continue;
                sum += Math.abs(matrix.getCoef(i+1, j+1));
            }
            if (Math.abs(matrix.getCoef(i+1,i+1)) > sum)
                strictnessFlag = true;
            if (Math.abs(matrix.getCoef(i+1,i+1)) < sum) {
                isConverged = false;
                break;
            }
        }
        return strictnessFlag && isConverged;
    }

    private AvailableDiagonals recursiveSearch(Matrix matrix,AvailableDiagonals availableDiagonals, Double[] sums,  int k) {
        //Когда прошли все строчки
        if (k >= matrix.getSize()) {
            //Флаг на выполнение хотя бы одной строгости
            boolean strictFlag = false;
            //Множество позиций, куда переставится строка
            Set<Integer> set = new HashSet<>();
            //Проходим еще раз по всем строгам для проверки строгости и заполнения множества
            for (int i = 0; i < matrix.getSize(); ++i) {
                set.add(availableDiagonals.top(i));
                if (sums[i] - 2 * Math.abs(matrix.getCoef(i+1, availableDiagonals.top(i)+1)) < 0)
                    strictFlag = true;
            }
            //Проверка на все ли строки однозначно заданы и хотя бы одну строгость
            if (set.size() == matrix.getSize() && strictFlag)
                return availableDiagonals;
            else return null;
        }
        //Если в строке нет доминирующего элемента
        if (availableDiagonals.top(k) == null)
            return null;

        AvailableDiagonals copyAvailableDiagonals = availableDiagonals.makeCopy();
        //Очищаем итерируемую строку, чтобы проверить каждый элемент
        while (copyAvailableDiagonals.getSize(k) > 0)
            copyAvailableDiagonals.pop(k);
        int size = availableDiagonals.getSize(k);
        //Проходим по всем доступным позициям для данной строки и вызываем проверку по следующей строке
        for (int i = 0; i < size; ++i) {
            //Кладем одну(!) позицию для данной строки
            copyAvailableDiagonals.push(k, availableDiagonals.pop(k));
            //Идем по следующим строчкам
            AvailableDiagonals res = recursiveSearch(matrix, copyAvailableDiagonals.makeCopy(), sums, k+1);
            //Если перестановка не вышла - продолжаем, иначе победа
            if (res == null)
                copyAvailableDiagonals.pop(k);
            else {
                return res;
            }
        }
        return null;
    }

    public int bringMatrix(Matrix matrix) {
        int k = 0;
        //Проверка на изначальное соответствие
        if (diagonalElementsPredominance(matrix))
            return k;
        //Считаем суммы каждой строки
        Double[] sums = new Double[matrix.getSize()];
        for (int i = 0; i < matrix.getSize(); ++i) {
            Double sum = 0D;
            for (int j = 0; j < matrix.getSize(); ++j)
                sum += Math.abs(matrix.getCoef(i+1, j+1));
            sums[i] = sum;
        }
        //Находим позиции для каждой строки, куда можно будет ее переставить
        AvailableDiagonals availableDiagonals = new AvailableDiagonals(matrix.getSize());
        for (int i = 0; i < matrix.getSize(); ++i) {
            for (int j = 0; j < matrix.getSize(); ++j) {
                if (sums[i] <= 2 * Math.abs(matrix.getCoef(i+1, j+1)))
                    availableDiagonals.push(i, j);
            }
        }
        //Проходим по найденным значениям, чтобы оставить по одной для каждой строки,
        // тк может быть несколько преобладающих значений у одной строки
        AvailableDiagonals res = recursiveSearch(matrix, availableDiagonals.makeCopy(), sums, 0);

        //Если перестановка не найдена
        if (res == null)
            return -1;

        //Делаем перестановку и считаем количеству свапов
        for (int i = 0; i < matrix.getSize(); ++i) {
            for (int j = 0; j < matrix.getSize(); ++j) {
                int l = res.top(i);
                if (l + 1 == i+1)
                    continue;
                matrix.swapLines(i+1, l+1);
                res.swapLists(l+1, i+1);
                ++k;
            }
        }
        return k;
    }
}
