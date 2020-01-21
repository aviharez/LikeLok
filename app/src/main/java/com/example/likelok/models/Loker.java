package com.example.likelok.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Loker implements Parcelable {

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

    protected Loker(Parcel in) {
        nama_loker = in.readString();
        email = in.readString();
        pic = in.readString();
        jenis = in.readString();
        gaji = in.readString();
        deskripsi = in.readString();
        lowongan = in.readString();
        tingkat = in.readString();
        deadline = in.readString();
    }

    public static final Creator<Loker> CREATOR = new Creator<Loker>() {
        @Override
        public Loker createFromParcel(Parcel in) {
            return new Loker(in);
        }

        @Override
        public Loker[] newArray(int size) {
            return new Loker[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama_loker);
        dest.writeString(email);
        dest.writeString(pic);
        dest.writeString(jenis);
        dest.writeString(gaji);
        dest.writeString(deskripsi);
        dest.writeString(lowongan);
        dest.writeString(tingkat);
        dest.writeString(deadline);
    }
}
