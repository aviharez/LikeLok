package com.example.likelok.models;

public class User {

    public String email, password, user_type;

    public User() {
    }

    public User(String email, String password, String user_type) {
        this.email = email;
        this.password = password;
        this.user_type = user_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
