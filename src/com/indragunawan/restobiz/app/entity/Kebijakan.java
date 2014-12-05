/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author indra
 */
@Entity
@Table(name = "kebijakan")
@NamedQueries({@NamedQuery(name = "Kebijakan.findByAkses", query = "SELECT k FROM Kebijakan k WHERE k.kebijakanPK.akses = :akses"), @NamedQuery(name = "Kebijakan.findByNamauser", query = "SELECT k FROM Kebijakan k WHERE k.kebijakanPK.namauser = :namauser"), @NamedQuery(name = "Kebijakan.findByIzin", query = "SELECT k FROM Kebijakan k WHERE k.kebijakanPK.izin = :izin")})
public class Kebijakan implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KebijakanPK kebijakanPK;
    @JoinColumn(name = "namauser", referencedColumnName = "namauser", insertable = false, updatable = false)
    @ManyToOne
    private Operator operator;

    public Kebijakan() {
    }

    public Kebijakan(KebijakanPK kebijakanPK) {
        this.kebijakanPK = kebijakanPK;
    }

    public Kebijakan(String akses, String namauser, boolean izin) {
        this.kebijakanPK = new KebijakanPK(akses, namauser, izin);
    }

    public KebijakanPK getKebijakanPK() {
        return kebijakanPK;
    }

    public void setKebijakanPK(KebijakanPK kebijakanPK) {
        this.kebijakanPK = kebijakanPK;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kebijakanPK != null ? kebijakanPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kebijakan)) {
            return false;
        }
        Kebijakan other = (Kebijakan) object;
        if ((this.kebijakanPK == null && other.kebijakanPK != null) || (this.kebijakanPK != null && !this.kebijakanPK.equals(other.kebijakanPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Kebijakan[kebijakanPK=" + kebijakanPK + "]";
    }
}
