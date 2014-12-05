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
public class KasPK implements Serializable {

    @Column(name = "transaksi", nullable = false)
    private short transaksi;
    @Column(name = "tanggal", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tanggal;

    public KasPK() {
    }

    public KasPK(short transaksi, Date tanggal) {
        this.transaksi = transaksi;
        this.tanggal = tanggal;
    }

    public short getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(short transaksi) {
        this.transaksi = transaksi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) transaksi;
        hash += (tanggal != null ? tanggal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KasPK)) {
            return false;
        }
        KasPK other = (KasPK) object;
        if (this.transaksi != other.transaksi) {
            return false;
        }
        if ((this.tanggal == null && other.tanggal != null) || (this.tanggal != null && !this.tanggal.equals(other.tanggal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.KasPK[transaksi=" + transaksi + ", tanggal=" + tanggal + "]";
    }
}
