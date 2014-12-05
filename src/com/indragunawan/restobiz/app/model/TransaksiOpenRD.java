/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.model;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author igoens
 */
public class TransaksiOpenRD extends JLabel implements TableCellRenderer {
    
    private static final long serialVersionUID = 3993342273949273303L;

    private ListSelectionModel selectionModel;

    public TransaksiOpenRD() {
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                setText(sdf.format(value));
                break;
            case 1:
                setHorizontalAlignment(CENTER);
                table.getColumnModel().getColumn(column).setMaxWidth(50);
                NumberFormat nf = new DecimalFormat("0000");
                setText(nf.format(value));
                break;
            case 2:
                setHorizontalAlignment(LEADING);
                setText(String.valueOf(value).trim());
                break;

        }
        table.getColumnModel().getSelectionModel().setSelectionMode(selectionModel.SINGLE_SELECTION);
        return this;
    }
}
