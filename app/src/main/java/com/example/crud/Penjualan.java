package com.example.crud;

public class Penjualan {
    int id;
    String tanggal, sektor, joiningDate;
    double pendapatan, pengeluaran;

    public Penjualan(int id, String tanggal, String sektor, String joiningDate, double pendapatan, double pengeluaran) {
        this.id = id;
        this.tanggal = tanggal;
        this.sektor = sektor;
        this.joiningDate = joiningDate;
        this.pendapatan = pendapatan;
        this.pengeluaran = pengeluaran;
    }

    public int getId() {
        return id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getSektor() {
        return sektor;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public double getPendapatan() {
        return pendapatan;
    }

    public double getPengeluaran() { return pengeluaran; }
}