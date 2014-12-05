/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author indra
 */
@Entity
@Table(name = "pembayaran")
@NamedQueries({@NamedQuery(name = "Pembayaran.findByNobukti", query = "SELECT p FROM Pembayaran p WHERE p.pembayaranPK.nobukti = :nobukti"), @NamedQuery(name = "Pembayaran.findByNomor", query = "SELECT p FROM Pembayaran p WHERE p.pembayaranPK.nomor = :nomor"), @NamedQuery(name = "Pembayaran.findByTanggal", query = "SELECT p FROM Pembayaran p WHERE p.pembayaranPK.tanggal = :tanggal"), @NamedQuery(name = "Pembayaran.findByJumlah", query = "SELECT p FROM Pembayaran p WHERE p.jumlah = :jumlah"), @NamedQuery(name = "Pembayaran.findByDibayar", query = "SELECT p FROM Pembayaran p WHERE p.pembayaranPK.dibayar = :dibayar"), @NamedQuery(name = "Pembayaran.findByShift", query = "SELECT p FROM Pembayaran p WHERE p.shift = :shift")})
public class Pembayaran implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PembayaranPK pembayaranPK;
    @Column(name = "jumlah", nullable = false)
    private float jumlah;
    @Column(name = "shift", nullable = false)
    private short shift;
    @JoinColumns({@JoinColumn(name = "nomor", referencedColumnName = "nomor", insertable = false, updatable = false), @JoinColumn(name = "tanggal", referencedColumnName = "tanggal", insertable = false, updatable = false)})
    @ManyToOne
    private Transaksi transaksi;
    @JoinColumn(name = "namauser", referencedColumnName = "namauser")
    @ManyToOne
    private Operator namauser;
    @JoinColumn(name = "jenis", referencedColumnName = "jenis")
    @ManyToOne
    private JenisBayar jenis;

    public Pembayaran() {
    }

    public Pembayaran(PembayaranPK pembayaranPK) {
        this.pembayaranPK = pembayaranPK;
    }

    public Pembayaran(PembayaranPK pembayaranPK, float jumlah, short shift) {
        this.pembayaranPK = pembayaranPK;
        this.jumlah = jumlah;
        this.shift = shift;
    }

    public Pembayaran(short nobukti, short nomor, Date tanggal, Date dibayar) {
        this.pembayaranPK = new PembayaranPK(nobukti, nomor, tanggal, dibayar);
    }

    public PembayaranPK getPembayaranPK() {
        return pembayaranPK;
    }

    public void setPembayaranPK(PembayaranPK pembayaranPK) {
        this.pembayaranPK = pembayaranPK;
    }

    public float getJumlah() {
        return jumlah;
    }

    public void setJumlah(float jumlah) {
        this.jumlah = jumlah;
    }

    public short getShift() {
        return shift;
    }

    public void setShift(short shift) {
        this.shift = shift;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public Operator getNamauser() {
        return namauser;
    }

    public void setNamauser(Operator namauser) {
        this.namauser = namauser;
    }

    public JenisBayar getJenis() {
        return jenis;
    }

    public void setJenis(JenisBayar jenis) {
        this.jenis = jenis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pembayaranPK != null ? pembayaranPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pembayaran)) {
            return false;
        }
        Pembayaran other = (Pembayaran) object;
        if ((this.pembayaranPK == null && other.pembayaranPK != null) || (this.pembayaranPK != null && !this.pembayaranPK.equals(other.pembayaranPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Pembayaran[pembayaranPK=" + pembayaranPK + "]";
    }
}
