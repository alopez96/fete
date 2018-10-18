package com.example.arturolopez.fete;

public class MyUser {
    public String username;
    public String email;
    public Integer phone;
    public String uid;
    public String userkey;

    public MyUser() {}


    public MyUser(String username, String email, Integer phone, String id, String userkey) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
        this.userkey = userkey;
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public Integer getPhone() { return phone; }
    public String getUid() {
        return uid;
    }
    public String getUserkey() {
        return userkey;
    }

}
