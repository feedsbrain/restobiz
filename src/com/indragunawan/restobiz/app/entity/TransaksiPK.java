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
public class TransaksiPK implements Serializable {

    @Column(name = "nomor", nullable = false)
    private short nomor;
    @Column(name = "tanggal", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tanggal;

    public TransaksiPK() {
    }

    public TransaksiPK(short nomor, Date tanggal) {
        this.nomor = nomor;
        this.tanggal = tanggal;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) nomor;
        hash += (tanggal != null ? tanggal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaksiPK)) {
            return false;
        }
        TransaksiPK other = (TransaksiPK) object;
        if (this.nomor != other.nomor) {
            return false;
        }
        if ((this.tanggal == null && other.tanggal != null) || (this.tanggal != null && !this.tanggal.equals(other.tanggal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.TransaksiPK[nomor=" + nomor + ", tanggal=" + tanggal + "]";
    }
}
