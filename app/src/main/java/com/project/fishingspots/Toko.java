package com.project.fishingspots;

public class Toko {
    String namaToko, nohp, pemilik, alamat, fotoToko, uid, jamBuka, buka;
    Double latitude, longitude;

    public Toko() {
    }

    public Toko(String namaToko, String nohp, String pemilik, String alamat, String fotoToko, String uid, String jamBuka, String buka, Double latitude, Double longitude) {
        this.namaToko = namaToko;
        this.nohp = nohp;
        this.pemilik = pemilik;
        this.alamat = alamat;
        this.fotoToko = fotoToko;
        this.uid = uid;
        this.jamBuka = jamBuka;
        this.buka = buka;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFotoToko() {
        return fotoToko;
    }

    public void setFotoToko(String fotoToko) {
        this.fotoToko = fotoToko;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJamBuka() {
        return jamBuka;
    }

    public void setJamBuka(String jamBuka) {
        this.jamBuka = jamBuka;
    }

    public String getBuka() {
        return buka;
    }

    public void setBuka(String buka) {
        this.buka = buka;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
