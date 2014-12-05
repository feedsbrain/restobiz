/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author indra
 */
@Embeddable
public class SkemaPK implements Serializable {

    @Column(name = "modul", nullable = false)
    private int modul;
    @Column(name = "akses", nullable = false)
    private String akses;

    public SkemaPK() {
    }

    public SkemaPK(int modul, String akses) {
        this.modul = modul;
        this.akses = akses;
    }

    public int getModul() {
        return modul;
    }

    public void setModul(int modul) {
        this.modul = modul;
    }

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) modul;
        hash += (akses != null ? akses.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SkemaPK)) {
            return false;
        }
        SkemaPK other = (SkemaPK) object;
        if (this.modul != other.modul) {
            return false;
        }
        if ((this.akses == null && other.akses != null) || (this.akses != null && !this.akses.equals(other.akses))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.SkemaPK[modul=" + modul + ", akses=" + akses + "]";
    }
}
