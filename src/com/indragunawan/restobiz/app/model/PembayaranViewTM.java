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
public class PembayaranViewTM extends AbstractTableModel {
    
    private static final long serialVersionUID = -8654450338582235923L;

    private int colnum = 4;
    private int rownum;
    private String[] colNames = {"No. Bukti", "Tgl. Bayar", "Jenis", "Jumlah"};
    private ArrayList<Object[]> ResultSets;
    private int i;

    @SuppressWarnings("unchecked")
    public PembayaranViewTM(List ls) {
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
