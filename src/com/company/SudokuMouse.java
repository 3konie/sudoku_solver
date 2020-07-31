package com.company;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SudokuMouse extends MouseAdapter {

    private final List<ButtonModel> buttonModels;
    private final ButtonGroup buttonGroup;
    private final Display d;

    public SudokuMouse(ButtonGroup buttonGroup, Display d) {
        this.buttonGroup = buttonGroup;
        buttonModels = new ArrayList<>(10);
        Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
        while (enumeration.hasMoreElements()) {
            ButtonModel bm = enumeration.nextElement().getModel();
            buttonModels.add(bm);
        }
        this.d = d;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        d.setOnField(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println(e.getWheelRotation());
        boolean up = e.getWheelRotation() < 0;
        if (up) {
            selectPrev();
        } else {
            selectNext();
        }
    }



    private void selectPrev() {
        int sel = findSelected();
        buttonModels.get(selectNumber(sel - 1)).setSelected(true);
    }

    private void selectNext() {
        int sel = findSelected();
        buttonModels.get(selectNumber(sel + 1)).setSelected(true);
    }

    private int findSelected() {
        ButtonModel bm = buttonGroup.getSelection();
        for (int i = 0; i < 10; i++) {
            if (buttonModels.get(i).equals(bm)) {
                return i;
            }
        }
        return -1;
    }

    private int selectNumber(int sel) {
        int n = buttonModels.size();
        return (sel + n) % n;
    }

}
