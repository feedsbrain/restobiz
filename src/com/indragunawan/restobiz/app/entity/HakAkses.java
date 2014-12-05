/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author indra
 */
@Entity
@Table(name = "hakakses")
@NamedQueries({@NamedQuery(name = "HakAkses.findByAkses", query = "SELECT h FROM HakAkses h WHERE h.akses = :akses"), @NamedQuery(name = "HakAkses.findBySistem", query = "SELECT h FROM HakAkses h WHERE h.sistem = :sistem")})
public class HakAkses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "akses", nullable = false)
    private String akses;
    @Column(name = "sistem", nullable = false)
    private boolean sistem;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hakAkses")
    private Collection<Skema> skemaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "akses")
    private Collection<Operator> operatorCollection;

    public HakAkses() {
    }

    public HakAkses(String akses) {
        this.akses = akses;
    }

    public HakAkses(String akses, boolean sistem) {
        this.akses = akses;
        this.sistem = sistem;
    }

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }

    public boolean getSistem() {
        return sistem;
    }

    public void setSistem(boolean sistem) {
        this.sistem = sistem;
    }

    public Collection<Skema> getSkemaCollection() {
        return skemaCollection;
    }

    public void setSkemaCollection(Collection<Skema> skemaCollection) {
        this.skemaCollection = skemaCollection;
    }

    public Collection<Operator> getOperatorCollection() {
        return operatorCollection;
    }

    public void setOperatorCollection(Collection<Operator> operatorCollection) {
        this.operatorCollection = operatorCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (akses != null ? akses.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HakAkses)) {
            return false;
        }
        HakAkses other = (HakAkses) object;
        if ((this.akses == null && other.akses != null) || (this.akses != null && !this.akses.equals(other.akses))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.HakAkses[akses=" + akses + "]";
    }
}
