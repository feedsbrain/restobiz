/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author indra
 */
@Embeddable
public class DetilTransaksiPK implements Serializable {

    @Column(name = "nomor", nullable = false)
    private short nomor;
    @Column(name = "tanggal", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @Column(name = "menu", nullable = false)
    private short menu;

    public DetilTransaksiPK() {
    }

    public DetilTransaksiPK(short nomor, Date tanggal, short menu) {
        this.nomor = nomor;
        this.tanggal = tanggal;
        this.menu = menu;
    }

    public short getNomor() {
        return nomor;
    }

    public void setNomor(short nomor) {
        this.nomor = nomor;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public short getMenu() {
        return menu;
    }

    public void setMenu(short menu) {
        this.menu = menu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) nomor;
        hash += (tanggal != null ? tanggal.hashCode() : 0);
        hash += (int) menu;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetilTransaksiPK)) {
            return false;
        }
        DetilTransaksiPK other = (DetilTransaksiPK) object;
        if (this.nomor != other.nomor) {
            return false;
        }
        if ((this.tanggal == null && other.tanggal != null) || (this.tanggal != null && !this.tanggal.equals(other.tanggal))) {
            return false;
        }
        if (this.menu != other.menu) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.DetilTransaksiPK[nomor=" + nomor + ", tanggal=" + tanggal + ", menu=" + menu + "]";
    }
}
