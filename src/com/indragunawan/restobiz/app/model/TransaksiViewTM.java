/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author igoens
 */
public class TransaksiViewTM extends AbstractTableModel {

    private static final long serialVersionUID = 1717151340312265754L;

    private int colnum = 4;
    private int rownum;
    private String[] colNames = {"Menu", "Nama", "Jumlah", "Harga"};
    private ArrayList<Object[]> ResultSets;
    private int i;

    @SuppressWarnings("unchecked")
    public TransaksiViewTM(List ls) {
        ResultSets = new ArrayList<Object[]>();
        for (i = 0; i <= ls.size() - 1; i++) {
            Vector result = (Vector) ls.get(i);
            ResultSets.add(result.toArray());
        }
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
