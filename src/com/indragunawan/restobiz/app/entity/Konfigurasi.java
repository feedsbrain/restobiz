/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author indra
 */
@Entity
@Table(name = "konfigurasi")
@NamedQueries({@NamedQuery(name = "Konfigurasi.findByNama", query = "SELECT k FROM Konfigurasi k WHERE k.nama = :nama"), @NamedQuery(name = "Konfigurasi.findByNilai", query = "SELECT k FROM Konfigurasi k WHERE k.nilai = :nilai")})
public class Konfigurasi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "nama", nullable = false)
    private String nama;
    @Column(name = "nilai", nullable = false)
    private String nilai;

    public Konfigurasi() {
    }

    public Konfigurasi(String nama) {
        this.nama = nama;
    }

    public Konfigurasi(String nama, String nilai) {
        this.nama = nama;
        this.nilai = nilai;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nama != null ? nama.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Konfigurasi)) {
            return false;
        }
        Konfigurasi other = (Konfigurasi) object;
        if ((this.nama == null && other.nama != null) || (this.nama != null && !this.nama.equals(other.nama))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Konfigurasi[nama=" + nama + "]";
    }
}
