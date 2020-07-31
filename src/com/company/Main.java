package com.company;

import javax.swing.*;


public class Main {

    private static final int[][] arr = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 8, 5},
            {0, 0, 1, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 5, 0, 7, 0, 0, 0},
            {0, 0, 4, 0, 0, 0, 1, 0, 0},
            {0, 9, 0, 0, 0, 0, 0, 0, 0},
            {5, 0, 0, 0, 0, 0, 0, 7, 3},
            {0, 0, 2, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 4, 0, 0, 0, 9}
    };

    private static final int[][] arr2 = {
            {2, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 6, 0, 0, 0, 0, 9, 0, 3},
            {0, 0, 0, 6, 0, 9, 7, 5, 0},
            {0, 0, 5, 0, 0, 0, 0, 0, 4},
            {1, 2, 0, 0, 0, 0, 0, 9, 7},
            {8, 0, 0, 0, 0, 0, 5, 0, 0},
            {0, 9, 7, 3, 0, 8, 0, 0, 0},
            {3, 0, 2, 0, 0, 0, 0, 6, 0},
            {0, 0, 0, 0, 2, 0, 0, 0, 9}
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Display display = new Display();
            display.setField(new Field(arr));
            display.setVisible(true);
        });
    }

}
