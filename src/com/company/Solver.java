package com.company;

import java.util.*;

public class  Solver {

    private final Field toSolve;
    private final PriorityQueue<PossibleMove> possibilities;
    private final PossibleMove[][] possibleMovesArr;
    private final Deque<Move> moves;

    public Solver(Field toSolve) {
        this.toSolve = toSolve;
        if (!toSolve.isValid()) {
            throw new IllegalArgumentException("Can not solve invalid field");
        }
        moves = new ArrayDeque<>();
        possibilities = new PriorityQueue<>(9 * 9);
        possibleMovesArr = new PossibleMove[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (toSolve.getNumber(j, i) == 0) {
                    PossibleMove pm = new PossibleMove(j, i, true);
                    for (int k = 0; k < 9; k++) {
                        int r = toSolve.getNumber(k, i);
                        int c = toSolve.getNumber(j, k);
                        pm.removePossibleDigit(r);
                        pm.removePossibleDigit(c);
                    }
                    int lsqH = j / 3;
                    int lsqV = i / 3;
                    for (int k = 3 * lsqV; k < 3 * (lsqV + 1); k++) {
                        for (int l = 3 * lsqH; l < 3 * (lsqH + 1); l++) {
                            pm.removePossibleDigit(toSolve.getNumber(l, k));
                        }
                    }
                    possibleMovesArr[i][j] = pm;
                    possibilities.add(possibleMovesArr[i][j]);
                }
            }
        }
    }

    public void nextMove() {
        if (toSolve.isWon()) {
            return;
        }
        if (!toSolve.isValid()) {
            return;
        }
        PossibleMove pm = possibilities.poll();
        if (pm.isEmpty()) {
            possibilities.add(pm);
            back();
        } else {
            int d = pm.getDigit();
            toSolve.set(d, pm.x, pm.y);
            List<Point> updated = updateOthers(d, pm.x, pm.y);
            addMove(pm, d, updated);
        }
    }

    private void addMove(PossibleMove pm, int d, List<Point> updated) {
        if (!moves.isEmpty()) {
            Move prev = moves.getLast();
            prev.addOnTop(new Point(pm.x, pm.y), d);
            // moves.addLast(prev);
        }
//        else {
//            Move first = new Move(null, 0, null);
//            first.addOnTop(new Point(pm.x, pm.y), d);
//        }
        Move move = new Move(pm, d, updated);
        moves.addLast(move);
//        System.out.println("----MOVE----");
//        System.out.println(move);
    }

    private List<Point> updateOthers(int d, int x, int y) {
        List<Point> updated = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            updateAtPos(d, updated, y, i);
            updateAtPos(d, updated, i, x);

        }
        int lsqH = x / 3;
        int lsqV = y / 3;
        for (int v = 3 * lsqV; v < 3 * (lsqV + 1); v++) {
            for (int h = 3 * lsqH; h < 3 * (lsqH + 1); h++) {
                updateAtPos(d, updated, v, h);
            }
        }
        return updated;
    }

    private void updateAtPos(int d, List<Point> updated, int y, int x) {
        if (toSolve.isEmpty(x, y)) {
            PossibleMove pm = possibleMovesArr[y][x];
            if (pm.isPossible(d)) {
                possibilities.remove(pm);
                pm.removePossibleDigit(d);
                updated.add(new Point(x, y));
                possibilities.add(pm);
            }
        }
    }

    private void back() {
        if (moves.isEmpty()) {
            throw new IllegalStateException("there is no moves to reverse");
        }
        Move move = moves.removeLast();
//        System.out.println("-----BACK----");
//        System.out.println(move);
        toSolve.set(0, move.pm.x, move.pm.y);
        int d = move.d;
        for (Point p : move.blocked) {
            PossibleMove pm = possibleMovesArr[p.y][p.x];
            possibilities.remove(pm);
            pm.addPossibleDigit(d);
            possibilities.add(pm);
        }
        possibilities.add(move.pm); // check
        for (Map.Entry<Point, List<Integer>> entry: move.getOnTop().entrySet()) {
            for (int dOnTop: entry.getValue()) {
                int x = entry.getKey().x;
                int y = entry.getKey().y;
                possibleMovesArr[y][x].addPossibleDigit(dOnTop);
            }
        }
    }

    private static class Move {
        private final PossibleMove pm;
        private final int d;
        private final List<Point> blocked;
        private final Map<Point, List<Integer>> onTop;

        public Move(PossibleMove pm, int d, List<Point> blocked) {
            this.pm = pm;
            this.d = d;
            this.blocked = blocked;
            onTop = new HashMap<>();
        }

        public void addOnTop(Point p, int d) {
            List<Integer> list = onTop.getOrDefault(p, new ArrayList<>());
            list.add(d);
            onTop.put(p, list);
        }

        public Map<Point, List<Integer>> getOnTop() {
            return onTop;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != Move.class) {
                return false;
            }
            Move m = (Move) obj;
            return this.pm.equals(m.pm);
        }

        @Override
        public String toString() {
            return "Move{" +
                    "pm=" + pm +
                    ", d=" + d +
                    ", blocked=" + blocked +
                    ", onTop=" + onTop +
                    '}';
        }
    }



    private static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != Point.class) {
                return false;
            }
            Point p = (Point) obj;
            return this.x == p.x && this.y == p.y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static class PossibleMove implements Comparable<PossibleMove> {
        private final int x;
        private final int y;
        private final Set<Integer> digits = new HashSet<>();

        public PossibleMove(int x, int y, boolean full) {
            this.x = x;
            this.y = y;
            if (full) {
                for (int i = 1; i <= 9; i++) {
                    digits.add(i);
                }
            }
        }

        public int getDigit() {
            for (int i = 1; i <= 9; i++) {
                if (digits.contains(i)) {
                    digits.remove(i);
                    return i;
                }
            }
            throw new IllegalArgumentException("Zero possible moves");
        }

        public boolean isPossible(int d) {
            return digits.contains(d);
        }

        public boolean isEmpty() {
            return digits.size() == 0;
        }


        public void addPossibleDigit(int digit) {
            digits.add(digit);
        }

        public void removePossibleDigit(int digit) {
            digits.remove(digit);
        }

        @Override
        public int compareTo(PossibleMove possibleMove) {
            return Integer.compare(this.digits.size(), possibleMove.digits.size());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
            return false;
            }
            if (obj.getClass() != PossibleMove.class) {
                return false;
            }
            PossibleMove pm = (PossibleMove) obj;
            return this.x == pm.x && this.y == pm.y;
        }

        @Override
        public String toString() {
            return "PossibleMove{" +
                    "x=" + x +
                    ", y=" + y +
                    ", digits=" + digits +
                    '}';
        }
    }

    private void printSituation() {
        StringBuilder sb = new StringBuilder(90);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (possibleMovesArr[i][j] == null) {
                   if (toSolve.isImmutable(j ,i)) {
                       sb.append('c');
                   } else {
                       sb.append('@');
                   }
                } else {
                    if (toSolve.getNumber(j, i) == 0) {
                        sb.append(' ');
                    } else {
                        sb.append(toSolve.getNumber(j, i));
                    }
                }
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());
    }

}
