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
@Table(name = "jenisbayar")
@NamedQueries({@NamedQuery(name = "JenisBayar.findByJenis", query = "SELECT j FROM JenisBayar j WHERE j.jenis = :jenis"), @NamedQuery(name = "JenisBayar.findByNegasi", query = "SELECT j FROM JenisBayar j WHERE j.negasi = :negasi")})
public class JenisBayar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "jenis", nullable = false)
    private String jenis;
    @Column(name = "negasi", nullable = false)
    private boolean negasi;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jenis")
    private Collection<Pembayaran> pembayaranCollection;

    public JenisBayar() {
    }

    public JenisBayar(String jenis) {
        this.jenis = jenis;
    }

    public JenisBayar(String jenis, boolean negasi) {
        this.jenis = jenis;
        this.negasi = negasi;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public boolean getNegasi() {
        return negasi;
    }

    public void setNegasi(boolean negasi) {
        this.negasi = negasi;
    }

    public Collection<Pembayaran> getPembayaranCollection() {
        return pembayaranCollection;
    }

    public void setPembayaranCollection(Collection<Pembayaran> pembayaranCollection) {
        this.pembayaranCollection = pembayaranCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jenis != null ? jenis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JenisBayar)) {
            return false;
        }
        JenisBayar other = (JenisBayar) object;
        if ((this.jenis == null && other.jenis != null) || (this.jenis != null && !this.jenis.equals(other.jenis))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.JenisBayar[jenis=" + jenis + "]";
    }
}
