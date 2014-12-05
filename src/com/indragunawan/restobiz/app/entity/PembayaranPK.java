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
public class PembayaranPK implements Serializable {

    @Column(name = "nobukti", nullable = false)
    private short nobukti;
    @Column(name = "nomor", nullable = false)
    private short nomor;
    @Column(name = "tanggal", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @Column(name = "dibayar", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dibayar;

    public PembayaranPK() {
    }

    public PembayaranPK(short nobukti, short nomor, Date tanggal, Date dibayar) {
        this.nobukti = nobukti;
        this.nomor = nomor;
        this.tanggal = tanggal;
        this.dibayar = dibayar;
    }

    public short getNobukti() {
        return nobukti;
    }

    public void setNobukti(short nobukti) {
        this.nobukti = nobukti;
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

    public Date getDibayar() {
        return dibayar;
    }

    public void setDibayar(Date dibayar) {
        this.dibayar = dibayar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) nobukti;
        hash += (int) nomor;
        hash += (tanggal != null ? tanggal.hashCode() : 0);
        hash += (dibayar != null ? dibayar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PembayaranPK)) {
            return false;
        }
        PembayaranPK other = (PembayaranPK) object;
        if (this.nobukti != other.nobukti) {
            return false;
        }
        if (this.nomor != other.nomor) {
            return false;
        }
        if ((this.tanggal == null && other.tanggal != null) || (this.tanggal != null && !this.tanggal.equals(other.tanggal))) {
            return false;
        }
        if ((this.dibayar == null && other.dibayar != null) || (this.dibayar != null && !this.dibayar.equals(other.dibayar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.PembayaranPK[nobukti=" + nobukti + ", nomor=" + nomor + ", tanggal=" + tanggal + ", dibayar=" + dibayar + "]";
    }
}
