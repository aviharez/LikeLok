package com.example.likelok.models;

public class SeekerProfile {

    private String name, email, pic, alamat, jk, hp;

    public SeekerProfile() {
    }

    public SeekerProfile(String name, String email, String pic, String alamat, String jk, String hp) {
        this.name = name;
        this.email = email;
        this.pic = pic;
        this.alamat = alamat;
        this.jk = jk;
        this.hp = hp;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
