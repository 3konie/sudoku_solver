package com.company;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;

public class Display extends JFrame {

    public int selected;
    private Field field;
    private SudokuPanel sp;
    private ButtonGroup buttonGroup;
    private Solver solver;

    public Display() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(makeButtonsPanel());
        sp = new SudokuPanel();
        MouseAdapter ml = new SudokuMouse(buttonGroup, this);
        sp.addMouseListener(ml);
        sp.addMouseWheelListener(ml);
        add(sp);
        setMinimumSize(new Dimension(650, 650));
        setSize(new Dimension(650, 650));
    }

    private JPanel makeButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        buttonGroup = new ButtonGroup();
        JRadioButton zeroButton = new JRadioButton("x");
        zeroButton.addActionListener(actionEvent -> selected = 0);
        panel.add(zeroButton);
        buttonGroup.add(zeroButton);
        zeroButton.setSelected(true);
        for (int i = 0; i < 9; i++) {
            JRadioButton button = new JRadioButton("" + (i + 1));
            int finalI = i;
            button.addActionListener(actionEvent -> selected = finalI + 1);
            buttonGroup.add(button);
            panel.add(button);
        }
        JButton clear = new JButton("clear");
        panel.add(clear);
        clear.addActionListener(actionEvent -> {
            field.clear();
            solver = new Solver(field);
            sp.repaint();
        });
        JButton next = new JButton("->");
        panel.add(next);
        next.addActionListener(actionEvent -> {
            solver.nextMove();
            sp.repaint();
        });
        JButton solve = new JButton("solve");
        panel.add(solve);
        solve.addActionListener(ae -> {
            new Thread( () -> {
                while (!field.isWon()) {
                    solver.nextMove();
                    sp.repaint();
                }
            }).start();
        });
        JButton save = new JButton("save");
        panel.add(save);
        save.addActionListener(ae -> {
            setField(Field.createFieldWithImmutableDigits(field));
        });
        JButton empty = new JButton("empty");
        panel.add(empty);
        empty.addActionListener( ae -> {
            setField(new Field(new int[0][0]));
        });
        return panel;
    }

    public void setOnField(int x, int y) {
        sp.set(selected, x, y);
    }

    public void setField(Field field) {
        this.field = field;
        sp.setField(field);
        solver = new Solver(field);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Display display = new Display();
                display.setField(new Field(Field.exampleArr_9));
                display.setVisible(true);
            }
        });
    }
}
