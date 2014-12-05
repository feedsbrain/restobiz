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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author indra
 */
@Entity
@Table(name = "operator")
@NamedQueries({@NamedQuery(name = "Operator.findByNamauser", query = "SELECT o FROM Operator o WHERE o.namauser = :namauser"), @NamedQuery(name = "Operator.findByPassword", query = "SELECT o FROM Operator o WHERE o.password = :password"), @NamedQuery(name = "Operator.findByNamaasli", query = "SELECT o FROM Operator o WHERE o.namaasli = :namaasli")})
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "namauser", nullable = false)
    private String namauser;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "namaasli", nullable = false)
    private String namaasli;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "namauser")
    private Collection<Kas> kasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "namauser")
    private Collection<Transaksi> transaksiCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operator")
    private Collection<Kebijakan> kebijakanCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "namauser")
    private Collection<Pembayaran> pembayaranCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "namauser")
    private Collection<DetilTransaksi> detilTransaksiCollection;
    @JoinColumn(name = "akses", referencedColumnName = "akses")
    @ManyToOne
    private HakAkses akses;

    public Operator() {
    }

    public Operator(String namauser) {
        this.namauser = namauser;
    }

    public Operator(String namauser, String password, String namaasli) {
        this.namauser = namauser;
        this.password = password;
        this.namaasli = namaasli;
    }

    public String getNamauser() {
        return namauser;
    }

    public void setNamauser(String namauser) {
        this.namauser = namauser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaasli() {
        return namaasli;
    }

    public void setNamaasli(String namaasli) {
        this.namaasli = namaasli;
    }

    public Collection<Kas> getKasCollection() {
        return kasCollection;
    }

    public void setKasCollection(Collection<Kas> kasCollection) {
        this.kasCollection = kasCollection;
    }

    public Collection<Transaksi> getTransaksiCollection() {
        return transaksiCollection;
    }

    public void setTransaksiCollection(Collection<Transaksi> transaksiCollection) {
        this.transaksiCollection = transaksiCollection;
    }

    public Collection<Kebijakan> getKebijakanCollection() {
        return kebijakanCollection;
    }

    public void setKebijakanCollection(Collection<Kebijakan> kebijakanCollection) {
        this.kebijakanCollection = kebijakanCollection;
    }

    public Collection<Pembayaran> getPembayaranCollection() {
        return pembayaranCollection;
    }

    public void setPembayaranCollection(Collection<Pembayaran> pembayaranCollection) {
        this.pembayaranCollection = pembayaranCollection;
    }

    public Collection<DetilTransaksi> getDetilTransaksiCollection() {
        return detilTransaksiCollection;
    }

    public void setDetilTransaksiCollection(Collection<DetilTransaksi> detilTransaksiCollection) {
        this.detilTransaksiCollection = detilTransaksiCollection;
    }

    public HakAkses getAkses() {
        return akses;
    }

    public void setAkses(HakAkses akses) {
        this.akses = akses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (namauser != null ? namauser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operator)) {
            return false;
        }
        Operator other = (Operator) object;
        if ((this.namauser == null && other.namauser != null) || (this.namauser != null && !this.namauser.equals(other.namauser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Operator[namauser=" + namauser + "]";
    }
}
