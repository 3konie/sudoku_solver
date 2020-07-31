package com.company;

import javax.swing.*;
import java.awt.*;

public class SudokuPanel extends JPanel {

    private Field field;
    private int max = 458;
    private int rect = 50;

    public SudokuPanel() {
        Dimension size = new Dimension(max, max);
        setMinimumSize(size);
        setMaximumSize(size);
        setBackground(Color.LIGHT_GRAY);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGrid(g);
        if (field != null) {
            paintNumbers(g);
            setBgColor();
        }
    }

    private void paintNumbers(Graphics g) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int d = field.getNumber(j, i);
                if (d != 0) {
                    if (field.isInvalid(j, i)) {
                        g.setColor(Color.RED);
                        g.fillRect((rect + 1) * j, (rect + 1) * i, 50, 50);
                    }
                    if (field.isImmutable(j, i)) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    int x = (rect + 1) * j + rect / 2;
                    int y = (rect + 1) * i + rect / 2;
                    g.drawString("" + d, x, y);
                }
            }
        }
    }

    private void paintGrid(Graphics g) {
        for (int i = 1; i < 9; i++) {
            if (i == 3 || i == 6) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.BLUE);
            }
            g.drawLine(0, i * (rect + 1), max, i * (rect + 1));
            g.drawLine(i * (rect + 1), 0, i * (rect + 1), max);
        }
    }

    private void setBgColor() {
        if (field.isWon()) {
            setBackground(Color.GREEN);
        } else {
            setBackground(Color.LIGHT_GRAY);
        }
    }

    public void set(int d, int x, int y) {
        if (x % 51 == 0 || y % 51 == 0) {
            System.out.println("line");
        } else {
            int i = y / 51;
            int j = x / 51;
            // System.out.println("i: " + i + ", j: " + j);
            field.set(d, j, i);
            repaint();
        }
    }

    public void setField(Field field) {
        this.field  = field;
        repaint();
    }
}
