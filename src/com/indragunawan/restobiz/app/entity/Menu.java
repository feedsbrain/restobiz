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
@Table(name = "menu")
@NamedQueries({@NamedQuery(name = "Menu.findByMenu", query = "SELECT m FROM Menu m WHERE m.menu = :menu"), @NamedQuery(name = "Menu.findByKelompok", query = "SELECT m FROM Menu m WHERE m.kelompok = :kelompok"), @NamedQuery(name = "Menu.findByNama", query = "SELECT m FROM Menu m WHERE m.nama = :nama"), @NamedQuery(name = "Menu.findByHarga", query = "SELECT m FROM Menu m WHERE m.harga = :harga"), @NamedQuery(name = "Menu.findByAktif", query = "SELECT m FROM Menu m WHERE m.aktif = :aktif"), @NamedQuery(name = "Menu.findByPajak", query = "SELECT m FROM Menu m WHERE m.pajak = :pajak"), @NamedQuery(name = "Menu.findBySatuan", query = "SELECT m FROM Menu m WHERE m.satuan = :satuan")})
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "menu", nullable = false)
    private Short menu;
    @Column(name = "kelompok", nullable = false)
    private String kelompok;
    @Column(name = "nama", nullable = false)
    private String nama;
    @Column(name = "harga", nullable = false)
    private float harga;
    @Column(name = "aktif", nullable = false)
    private boolean aktif;
    @Column(name = "pajak", nullable = false)
    private boolean pajak;
    @Column(name = "satuan")
    private String satuan;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menu1")
    private Collection<DetilTransaksi> detilTransaksiCollection;

    public Menu() {
    }

    public Menu(Short menu) {
        this.menu = menu;
    }

    public Menu(Short menu, String kelompok, String nama, float harga, boolean aktif, boolean pajak) {
        this.menu = menu;
        this.kelompok = kelompok;
        this.nama = nama;
        this.harga = harga;
        this.aktif = aktif;
        this.pajak = pajak;
    }

    public Short getMenu() {
        return menu;
    }

    public void setMenu(Short menu) {
        this.menu = menu;
    }

    public String getKelompok() {
        return kelompok;
    }

    public void setKelompok(String kelompok) {
        this.kelompok = kelompok;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public float getHarga() {
        return harga;
    }

    public void setHarga(float harga) {
        this.harga = harga;
    }

    public boolean getAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public boolean getPajak() {
        return pajak;
    }

    public void setPajak(boolean pajak) {
        this.pajak = pajak;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
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
        hash += (menu != null ? menu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.menu == null && other.menu != null) || (this.menu != null && !this.menu.equals(other.menu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Menu[menu=" + menu + "]";
    }
}
