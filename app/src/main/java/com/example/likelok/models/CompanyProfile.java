package com.example.likelok.models;

public class CompanyProfile {

    private String name, email, pic, alamat, industri, telepon, pegawai;

    public CompanyProfile() {
    }

    public CompanyProfile(String name, String email, String pic, String alamat, String industri, String telepon, String pegawai) {
        this.name = name;
        this.email = email;
        this.pic = pic;
        this.alamat = alamat;
        this.industri = industri;
        this.telepon = telepon;
        this.pegawai = pegawai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getIndustri() {
        return industri;
    }

    public void setIndustri(String industri) {
        this.industri = industri;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getPegawai() {
        return pegawai;
    }

    public void setPegawai(String pegawai) {
        this.pegawai = pegawai;
    }
}
