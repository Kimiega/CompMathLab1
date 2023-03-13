package com.kimiega.linal;

import java.util.ArrayList;
import java.util.List;

public class AvailableDiagonals {
    List<Integer>[] availableDiagonals;

    public AvailableDiagonals(int n) {
        availableDiagonals = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i<n; ++i)
            availableDiagonals[i] = new ArrayList<>();
    }
    private AvailableDiagonals(List<Integer>[] availableDiagonals) {
        this.availableDiagonals = availableDiagonals;
    }

    public void push(int i, int val) {
        availableDiagonals[i].add(val);
    }

    public Integer pop(int i) {
        if (availableDiagonals[i].isEmpty())
            return null;
        int x = availableDiagonals[i].get(0);
        availableDiagonals[i].remove(0);
        return x;
    }
    public Integer top(int i) {
        if (availableDiagonals[i].isEmpty())
            return null;
        return availableDiagonals[i].get(0);
    }
    public Integer getSize(int i) {
        return availableDiagonals[i].size();
    }

    public boolean swapLists(int k, int p) {
        if (k > availableDiagonals.length || p > availableDiagonals.length || k <= 0 || p <= 0)
            return false;
        List<Integer> temp = availableDiagonals[k-1];
        availableDiagonals[k-1] = availableDiagonals[p-1];
        availableDiagonals[p-1] = temp;
        return true;
    }
    public AvailableDiagonals makeCopy() {
        List<Integer>[] copy =(ArrayList<Integer>[]) new ArrayList[availableDiagonals.length];
        for (int i = 0; i < availableDiagonals.length; ++i) {
            copy[i] = new ArrayList<>(availableDiagonals[i].size());
            for (var x : availableDiagonals[i])
                copy[i].add(x);
        }
        return new AvailableDiagonals(copy);
    }
}
