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
public class KebijakanPK implements Serializable {

    @Column(name = "akses", nullable = false)
    private String akses;
    @Column(name = "namauser", nullable = false)
    private String namauser;
    @Column(name = "izin", nullable = false)
    private boolean izin;

    public KebijakanPK() {
    }

    public KebijakanPK(String akses, String namauser, boolean izin) {
        this.akses = akses;
        this.namauser = namauser;
        this.izin = izin;
    }

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }

    public String getNamauser() {
        return namauser;
    }

    public void setNamauser(String namauser) {
        this.namauser = namauser;
    }

    public boolean getIzin() {
        return izin;
    }

    public void setIzin(boolean izin) {
        this.izin = izin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (akses != null ? akses.hashCode() : 0);
        hash += (namauser != null ? namauser.hashCode() : 0);
        hash += (izin ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KebijakanPK)) {
            return false;
        }
        KebijakanPK other = (KebijakanPK) object;
        if ((this.akses == null && other.akses != null) || (this.akses != null && !this.akses.equals(other.akses))) {
            return false;
        }
        if ((this.namauser == null && other.namauser != null) || (this.namauser != null && !this.namauser.equals(other.namauser))) {
            return false;
        }
        if (this.izin != other.izin) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.KebijakanPK[akses=" + akses + ", namauser=" + namauser + ", izin=" + izin + "]";
    }
}
