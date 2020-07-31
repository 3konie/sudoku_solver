package com.company;


import java.util.HashSet;
import java.util.Set;

public class Field {

    public static int[][] exampleArr_9 = {{5, 3, 0, 0, 7, 0, 0, 0, 0},
                                        {6, 0, 0, 1, 9, 5, 0, 0, 0},
                                        {0, 9, 8, 0, 0, 0, 0, 6, 0},
                                        {8, 0, 0, 0, 6, 0, 0, 0, 3},
                                        {4, 0, 0, 8, 0, 3, 0, 0, 1},
                                        {7, 0, 0, 0, 2, 0, 0, 0, 6},
                                        {0, 6, 0, 0, 0, 0, 2, 8, 0},
                                        {0, 0, 0, 4, 1, 9, 0, 0, 5},
                                        {0, 0, 0, 0, 8, 0, 0, 7, 9}};

    private final int[][] digits;
    private final int n;
    private final int sqrtN;
    private final boolean[][] constant;
    private final boolean[][] invalid;
    private boolean isRecentMoveInvalidated;
    private int empty;

    public Field(int n) {
        this.n = n;
        this.sqrtN = (int) Math.sqrt(n);
        this.digits = new int[n][n];
        this.constant = new boolean[n][n];
        this.invalid = new boolean[n][n];
        empty = n * n;
    }

    private void fillConst() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (digits[i][j] != 0) {
                    constant[i][j] = true;
                }
            }
        }
    }

//    public Field(Field toBeCopied) {
//        digits = new int[9][9];
//        for (int i = 0; i < 9; i++) {
//            System.arraycopy(toBeCopied.digits[i], 0, digits[i], 0, 9);
//        }
//        empty = toBeCopied.empty;
//        constant = new boolean[9][9];
//        for (int i = 0; i < 9; i++) {
//            System.arraycopy(toBeCopied.constant[i], 0, constant[i], 0, 9);
//        }
//    }

    public Field(int[][] field) {
        this.n = field.length;
        this.sqrtN = (int) Math.sqrt(n);
        digits = new int[n][n];
//        if (field.length != 9 && field[0].length != 9) {
//            throw new IllegalArgumentException("field must be 9 x 9 size");
//        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int d = field[i][j];
                if (d < 0 || d > n) {
                    throw new IllegalArgumentException("digit out of range found");
                }
                digits[i][j] = d;
            }
        }
        empty = countZeros();
        constant = new boolean[n][n];
        this.invalid = new boolean[n][n];
        fillConst();
    }

    public void set(int digit, int x, int y) {
        if (digit < 0 || digit > n) {
            throw new IllegalArgumentException("Digit must be not smaller than 0 and not greater than 9");
        }
        if (x < 0 || x >= n
            || y < 0 || y >= n) {
            throw new IllegalArgumentException("Field coordinates must be in range 0 - " + (n - 1) + " inclusive");
        }
        if (constant[y][x]) {
            return;
        }
        int oldDigit = digits[y][x];
        if (oldDigit == 0 && digit != 0) {
            empty--;
        } else if (oldDigit != 0 && digit == 0) {
            empty++;
        }
        digits[y][x] = digit;
        isRecentMoveInvalidated = false;
    }

    public boolean isValid() {
        for (int i = 0; i < n; i++) {
            if (!checkRow(i)) {
                return false;
            }
            if (!checkCol(i)) {
                return false;
            }
        }
        for (int i = 0; i < sqrtN; i++) {
            for (int j = 0; j < sqrtN; j++) {
                if (!checkLittleSquare(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getNumber(int x, int y) {
        return digits[y][x];
    }

    public int emptySpaces() {
        return empty;
    }

    public boolean isWon() {
        return emptySpaces() == 0 && isValid();
    }

    private int countZeros() {
        int z = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (digits[i][j] == 0) {
                    z++;
                }
            }
        }
        return z;
    }

    private void markAllInvalid() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int d = digits[i][j];
                if (d != 0) {
                    markInvalid(i, j);
                }
            }
        }
        isRecentMoveInvalidated = true;
    }

    public boolean isInvalid(int x, int y) {
        if (!isRecentMoveInvalidated) {
            markAllInvalid();
        }
        return invalid[y][x];
    }

    public boolean isImmutable(int x, int y) {
        return constant[y][x];
    }


    public void clear() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!constant[i][j]) {
                    digits[i][j] = 0;
                }
            }
        }
        empty = countZeros();
    }

    private void markInvalid(int y, int x) {
        int d = digits[y][x];
        for (int i = 0; i < n; i++) {
            if (digits[i][x] == d && i != y) {
                invalid[y][x] = true;
                invalid[i][x] = true;
            }
            if (digits[y][i] == d && i != x) {
                invalid[y][x] = true;
                invalid[y][i] = true;
            }
        }
        int lsqH = x / sqrtN;
        int lsqV = y / sqrtN;
        for (int i = sqrtN * lsqV; i < sqrtN * (lsqV + 1); i++) {
            for (int j = sqrtN * lsqH; j < sqrtN * (lsqH + 1); j++) {
                if (digits[i][j] == d && (i != y && j != x)) {
                    invalid[y][x] = true;
                    invalid[i][j] = true;
                }
            }
        }
    }


    public boolean isEmpty(int x, int y) {
        return digits[y][x] == 0;
    }

    private boolean checkSquare(int x0, int x1, int y0, int y1) {
        Set<Integer> set = new HashSet<>();
        for (int i = y0; i < y1; i++) {
            for (int j = x0; j < x1; j++) {
                int n = digits[i][j];
                if (n != 0) {
                    if (set.contains(n)) {
                        return false;
                    }
                    set.add(n);
                }
            }
        }
        return true;
    }

    private boolean checkRow(int r) {
        return checkSquare(0, n, r, r + 1);
    }

    private boolean checkCol(int c) {
        return checkSquare(c, c + 1, 0, n);
    }

    private boolean checkLittleSquare(int x, int y) {
        return checkSquare(sqrtN * x, sqrtN * x + 1, sqrtN * y, sqrtN * y + 1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int d = digits[i][j];
                if (d != 0) {
                    sb.append(d);
                } else {
                    sb.append(' ');
                }
                if ((j + 1) % sqrtN == 0 && (j + 1) != n){
                    sb.append('|');
                }
            }
            sb.append('\n');
            if ((i + 1)  % sqrtN == 0 && i + 1 != n) {
                for (int j = 0; j < sqrtN; j++) {
                    for (int k = 0; k < sqrtN; k++) {
                        sb.append('-');
                    }
                    if ((j + 1) != sqrtN){
                        sb.append('+');
                    }
                }
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public int getN() {
        return n;
    }

    public int getSqrtN() {
        return sqrtN;
    }

    public static void main(String[] args) {
        System.out.println(new Field(new int[9][9]));
        System.out.println("###############");
        System.out.println(new Field(new int[16][16]));
        System.out.println("###############");
        System.out.println(new Field(new int[25][25]));
    }

    public static Field createFieldWithImmutableDigits(Field field) {
        return new Field(field.digits);
    }
}
