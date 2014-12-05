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
@Table(name = "detiltransaksi")
@NamedQueries({@NamedQuery(name = "DetilTransaksi.findByNomor", query = "SELECT d FROM DetilTransaksi d WHERE d.detilTransaksiPK.nomor = :nomor"), @NamedQuery(name = "DetilTransaksi.findByTanggal", query = "SELECT d FROM DetilTransaksi d WHERE d.detilTransaksiPK.tanggal = :tanggal"), @NamedQuery(name = "DetilTransaksi.findByMenu", query = "SELECT d FROM DetilTransaksi d WHERE d.detilTransaksiPK.menu = :menu"), @NamedQuery(name = "DetilTransaksi.findByJumlah", query = "SELECT d FROM DetilTransaksi d WHERE d.jumlah = :jumlah"), @NamedQuery(name = "DetilTransaksi.findByHarga", query = "SELECT d FROM DetilTransaksi d WHERE d.harga = :harga"), @NamedQuery(name = "DetilTransaksi.findByDiskon", query = "SELECT d FROM DetilTransaksi d WHERE d.diskon = :diskon"), @NamedQuery(name = "DetilTransaksi.findByPajak", query = "SELECT d FROM DetilTransaksi d WHERE d.pajak = :pajak"), @NamedQuery(name = "DetilTransaksi.findByBerat", query = "SELECT d FROM DetilTransaksi d WHERE d.berat = :berat")})
public class DetilTransaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetilTransaksiPK detilTransaksiPK;
    @Column(name = "jumlah", nullable = false)
    private float jumlah;
    @Column(name = "harga", nullable = false)
    private float harga;
    @Column(name = "diskon", nullable = false)
    private float diskon;
    @Column(name = "pajak", nullable = false)
    private float pajak;
    @Column(name = "berat", nullable = false)
    private float berat;
    @JoinColumn(name = "namauser", referencedColumnName = "namauser")
    @ManyToOne
    private Operator namauser;
    @JoinColumn(name = "menu", referencedColumnName = "menu", insertable = false, updatable = false)
    @ManyToOne
    private Menu menu1;
    @JoinColumns({@JoinColumn(name = "nomor", referencedColumnName = "nomor", insertable = false, updatable = false), @JoinColumn(name = "tanggal", referencedColumnName = "tanggal", insertable = false, updatable = false)})
    @ManyToOne
    private Transaksi transaksi;

    public DetilTransaksi() {
    }

    public DetilTransaksi(DetilTransaksiPK detilTransaksiPK) {
        this.detilTransaksiPK = detilTransaksiPK;
    }

    public DetilTransaksi(DetilTransaksiPK detilTransaksiPK, float jumlah, float harga, float diskon, float pajak, float berat) {
        this.detilTransaksiPK = detilTransaksiPK;
        this.jumlah = jumlah;
        this.harga = harga;
        this.diskon = diskon;
        this.pajak = pajak;
        this.berat = berat;
    }

    public DetilTransaksi(short nomor, Date tanggal, short menu) {
        this.detilTransaksiPK = new DetilTransaksiPK(nomor, tanggal, menu);
    }

    public DetilTransaksiPK getDetilTransaksiPK() {
        return detilTransaksiPK;
    }

    public void setDetilTransaksiPK(DetilTransaksiPK detilTransaksiPK) {
        this.detilTransaksiPK = detilTransaksiPK;
    }

    public float getJumlah() {
        return jumlah;
    }

    public void setJumlah(float jumlah) {
        this.jumlah = jumlah;
    }

    public float getHarga() {
        return harga;
    }

    public void setHarga(float harga) {
        this.harga = harga;
    }

    public float getDiskon() {
        return diskon;
    }

    public void setDiskon(float diskon) {
        this.diskon = diskon;
    }

    public float getPajak() {
        return pajak;
    }

    public void setPajak(float pajak) {
        this.pajak = pajak;
    }

    public float getBerat() {
        return berat;
    }

    public void setBerat(float berat) {
        this.berat = berat;
    }

    public Operator getNamauser() {
        return namauser;
    }

    public void setNamauser(Operator namauser) {
        this.namauser = namauser;
    }

    public Menu getMenu1() {
        return menu1;
    }

    public void setMenu1(Menu menu1) {
        this.menu1 = menu1;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detilTransaksiPK != null ? detilTransaksiPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetilTransaksi)) {
            return false;
        }
        DetilTransaksi other = (DetilTransaksi) object;
        if ((this.detilTransaksiPK == null && other.detilTransaksiPK != null) || (this.detilTransaksiPK != null && !this.detilTransaksiPK.equals(other.detilTransaksiPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.DetilTransaksi[detilTransaksiPK=" + detilTransaksiPK + "]";
    }
}
