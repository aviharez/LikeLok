package com.example.likelok.models;

public class Loker {

    private String nama_loker, email, pic, jenis, gaji, deskripsi, lowongan, tingkat, deadline;

    public Loker() {
    }

    public Loker(String nama_loker, String email, String pic, String jenis, String gaji, String deskripsi, String lowongan, String tingkat, String deadline) {
        this.nama_loker = nama_loker;
        this.email = email;
        this.pic = pic;
        this.jenis = jenis;
        this.gaji = gaji;
        this.deskripsi = deskripsi;
        this.lowongan = lowongan;
        this.tingkat = tingkat;
        this.deadline = deadline;
    }

    public String getNama_loker() {
        return nama_loker;
    }

    public void setNama_loker(String nama_loker) {
        this.nama_loker = nama_loker;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getGaji() {
        return gaji;
    }

    public void setGaji(String gaji) {
        this.gaji = gaji;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLowongan() {
        return lowongan;
    }

    public void setLowongan(String lowongan) {
        this.lowongan = lowongan;
    }

    public String getTingkat() {
        return tingkat;
    }

    public void setTingkat(String tingkat) {
        this.tingkat = tingkat;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
