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
public class KasHarianRD extends JLabel implements TableCellRenderer {
    
    private static final long serialVersionUID = 5778284738557842726L;

    private ListSelectionModel selectionModel;
    private NumberFormat transDisplay = new DecimalFormat("0000");
    private NumberFormat floatDisplay = new DecimalFormat("#,##0.00");

    public KasHarianRD() {
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
                table.getColumnModel().getColumn(column).setMaxWidth(100);
                setText(transDisplay.format(value));
                break;
            case 1:
                setHorizontalAlignment(LEADING);
                table.getColumnModel().getColumn(column).setMaxWidth(200);
                setText(String.valueOf(value));
                break;
            case 2:
                setHorizontalAlignment(LEADING);
                setText(String.valueOf(value));
                break;
            case 3:
                setHorizontalAlignment(TRAILING);
                table.getColumnModel().getColumn(column).setMaxWidth(200);
                setText(floatDisplay.format(value));
                break;
        }
        table.getColumnModel().getSelectionModel().setSelectionMode(selectionModel.SINGLE_SELECTION);
        return this;
    }
}
