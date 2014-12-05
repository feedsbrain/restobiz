/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author igoens
 */
public class ModulePrivilages {

    public ModulePrivilages() {
    }

    public List getAvailableModules() {
        ArrayList<String> r = new ArrayList<String>();

        r.add("----- Menu File ------");
        r.add("1100 | Transaksi");
        r.add("1101 | Open Posted");
        r.add("1200 | Kas Harian");
        r.add("1300 | Menu Edit");
        r.add("---- Menu Laporan ----");
        /** r.add("2100 | Daftar Menu"); */
        r.add("2200 | Transaksi");
        r.add("2300 | Jurnal Kas");
        r.add("2400 | Penjualan");
        r.add("2500 | Top 10");
        r.add("----- Menu Tools -----");
        r.add("3100 | Database");
        r.add("3200 | Konfigurasi");
        /** r.add("3300 | Password"); */
        return r;
    }

    public String getModuleName(Integer Module) {
        int i;
        String r = null, s = null;

        List tmp = getAvailableModules();

        for (i = 0; i <= tmp.size() - 1; i++) {
            s = tmp.get(i).toString();
            if (!s.isEmpty()) {
                if (s.substring(0, 4).equals(String.valueOf(Module))) {
                    r = s.substring(7, s.length());
                }
            }
        }

        return r;
    }
}
