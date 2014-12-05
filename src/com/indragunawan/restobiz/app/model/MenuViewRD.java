/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.model;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author igoens
 */
public class MenuViewRD extends JLabel implements TableCellRenderer {
    
    private static final long serialVersionUID = 1042109215155458587L;

    private NumberFormat nf;
    private ListSelectionModel selectionModel;

    public MenuViewRD() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(Color.lightGray);
            setForeground(Color.black);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        switch (column) {
            case 0:
                setHorizontalAlignment(CENTER);
                table.getColumnModel().getColumn(column).setMaxWidth(50);
                nf = new DecimalFormat("0000");
                setText(nf.format(value));
                break;
            case 1:
                setHorizontalAlignment(LEADING);
                table.getColumnModel().getColumn(column).setMaxWidth(80);
                setText(String.valueOf(value));
                break;
            case 2:
                setHorizontalAlignment(LEADING);
                setText(String.valueOf(value));
                break;
            case 3:
                setHorizontalAlignment(TRAILING);
                table.getColumnModel().getColumn(column).setMaxWidth(100);
                nf = new DecimalFormat("#,##0.00");
                setText(nf.format(value));
                break;
            case 4:
                setHorizontalAlignment(CENTER);
                table.getColumnModel().getColumn(column).setMaxWidth(50);
                setText(String.valueOf(value));
                break;
            case 5:
                setHorizontalAlignment(CENTER);
                table.getColumnModel().getColumn(column).setMaxWidth(50);
                if (Boolean.parseBoolean(String.valueOf(value))) {
                    setText("V");
                } else {
                    setText("X");
                }
                break;
            case 6:
                setHorizontalAlignment(CENTER);
                table.getColumnModel().getColumn(column).setMaxWidth(50);
                if (Boolean.parseBoolean(String.valueOf(value))) {
                    setText("V");
                } else {
                    setText("X");
                }
                break;
        }
        table.getColumnModel().getSelectionModel().setSelectionMode(selectionModel.SINGLE_SELECTION);
        return this;
    }
}
