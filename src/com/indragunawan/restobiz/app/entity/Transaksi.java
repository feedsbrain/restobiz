/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "transaksi")
@NamedQueries({@NamedQuery(name = "Transaksi.findByNomor", query = "SELECT t FROM Transaksi t WHERE t.transaksiPK.nomor = :nomor"), @NamedQuery(name = "Transaksi.findByTanggal", query = "SELECT t FROM Transaksi t WHERE t.transaksiPK.tanggal = :tanggal"), @NamedQuery(name = "Transaksi.findByCustomer", query = "SELECT t FROM Transaksi t WHERE t.customer = :customer"), @NamedQuery(name = "Transaksi.findByShift", query = "SELECT t FROM Transaksi t WHERE t.shift = :shift"), @NamedQuery(name = "Transaksi.findByPosted", query = "SELECT t FROM Transaksi t WHERE t.posted = :posted")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TransaksiPK transaksiPK;
    @Column(name = "customer", nullable = false)
    private String customer;
    @Column(name = "shift", nullable = false)
    private short shift;
    @Column(name = "posted", nullable = false)
    private boolean posted;
    @JoinColumn(name = "namauser", referencedColumnName = "namauser")
    @ManyToOne
    private Operator namauser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaksi")
    private Collection<Pembayaran> pembayaranCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaksi")
    private Collection<DetilTransaksi> detilTransaksiCollection;

    public Transaksi() {
    }

    public Transaksi(TransaksiPK transaksiPK) {
        this.transaksiPK = transaksiPK;
    }

    public Transaksi(TransaksiPK transaksiPK, String customer, short shift, boolean posted) {
        this.transaksiPK = transaksiPK;
        this.customer = customer;
        this.shift = shift;
        this.posted = posted;
    }

    public Transaksi(short nomor, Date tanggal) {
        this.transaksiPK = new TransaksiPK(nomor, tanggal);
    }

    public TransaksiPK getTransaksiPK() {
        return transaksiPK;
    }

    public void setTransaksiPK(TransaksiPK transaksiPK) {
        this.transaksiPK = transaksiPK;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public short getShift() {
        return shift;
    }

    public void setShift(short shift) {
        this.shift = shift;
    }

    public boolean getPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public Operator getNamauser() {
        return namauser;
    }

    public void setNamauser(Operator namauser) {
        this.namauser = namauser;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transaksiPK != null ? transaksiPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.transaksiPK == null && other.transaksiPK != null) || (this.transaksiPK != null && !this.transaksiPK.equals(other.transaksiPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Transaksi[transaksiPK=" + transaksiPK + "]";
    }
}
