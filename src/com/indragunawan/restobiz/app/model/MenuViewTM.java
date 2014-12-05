/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author igoens
 */
public class MenuViewTM extends AbstractTableModel {
    
    private static final long serialVersionUID = -7969144553305071140L;

    private int colnum = 7;
    private int rownum;
    private String[] colNames = {"Menu", "Kelompok", "Nama", "Harga", "Satuan", "Pajak", "Aktif"};
    private ArrayList<Object[]> ResultSets;

    @SuppressWarnings("unchecked")
    public MenuViewTM(List ls) {
        ResultSets = new ArrayList<Object[]>();
        ResultSets.addAll(ls);
    }

    public int getRowCount() {
        rownum = ResultSets.size();
        return rownum;
    }

    public int getColumnCount() {
        return colnum;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] row = ResultSets.get(rowIndex);
        return row[columnIndex];
    }

    @Override
    public String getColumnName(int param) {
        return colNames[param];
    }
}
