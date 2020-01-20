package com.example.likelok.models;

public class Company {
    private String name, pic, alamat, email, pegawai, industri;

    public Company() {
    }

    public Company(String name, String pic, String alamat, String email, String pegawai, String industri) {
        this.name = name;
        this.pic = pic;
        this.alamat = alamat;
        this.email = email;
        this.pegawai = pegawai;
        this.industri = industri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPegawai() {
        return pegawai;
    }

    public void setPegawai(String pegawai) {
        this.pegawai = pegawai;
    }

    public String getIndustri() {
        return industri;
    }

    public void setIndustri(String industri) {
        this.industri = industri;
    }
}
