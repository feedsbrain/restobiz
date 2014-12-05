/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author igoens
 */
public class JenisBayarRD extends JLabel implements TableCellRenderer {
    
    private static final long serialVersionUID = -2177299743858257881L;

    private ListSelectionModel selectionModel;

    public JenisBayarRD() {
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
                setHorizontalAlignment(LEADING);
                setText(String.valueOf(value));
                break;
            case 1:
                setHorizontalAlignment(CENTER);
                table.getColumnModel().getColumn(column).setMaxWidth(100);
                if (Boolean.valueOf(String.valueOf(value))) {
                    setText("Ya");
                } else {
                    setText("Tidak");
                }
                break;
        }
        table.getColumnModel().getSelectionModel().setSelectionMode(selectionModel.SINGLE_SELECTION);
        return this;
    }
}