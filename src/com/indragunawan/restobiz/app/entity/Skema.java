/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import javax.persistence.Column;
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
@Table(name = "skema")
@NamedQueries({@NamedQuery(name = "Skema.findByModul", query = "SELECT s FROM Skema s WHERE s.skemaPK.modul = :modul"), @NamedQuery(name = "Skema.findByAkses", query = "SELECT s FROM Skema s WHERE s.skemaPK.akses = :akses"), @NamedQuery(name = "Skema.findByNegasi", query = "SELECT s FROM Skema s WHERE s.negasi = :negasi")})
public class Skema implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SkemaPK skemaPK;
    @Column(name = "negasi", nullable = false)
    private boolean negasi;
    @JoinColumn(name = "akses", referencedColumnName = "akses", insertable = false, updatable = false)
    @ManyToOne
    private HakAkses hakAkses;

    public Skema() {
    }

    public Skema(SkemaPK skemaPK) {
        this.skemaPK = skemaPK;
    }

    public Skema(SkemaPK skemaPK, boolean negasi) {
        this.skemaPK = skemaPK;
        this.negasi = negasi;
    }

    public Skema(int modul, String akses) {
        this.skemaPK = new SkemaPK(modul, akses);
    }

    public SkemaPK getSkemaPK() {
        return skemaPK;
    }

    public void setSkemaPK(SkemaPK skemaPK) {
        this.skemaPK = skemaPK;
    }

    public boolean getNegasi() {
        return negasi;
    }

    public void setNegasi(boolean negasi) {
        this.negasi = negasi;
    }

    public HakAkses getHakAkses() {
        return hakAkses;
    }

    public void setHakAkses(HakAkses hakAkses) {
        this.hakAkses = hakAkses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (skemaPK != null ? skemaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Skema)) {
            return false;
        }
        Skema other = (Skema) object;
        if ((this.skemaPK == null && other.skemaPK != null) || (this.skemaPK != null && !this.skemaPK.equals(other.skemaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Skema[skemaPK=" + skemaPK + "]";
    }
}
