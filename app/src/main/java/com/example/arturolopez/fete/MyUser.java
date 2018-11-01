package com.example.arturolopez.fete;

public class MyUser {
    public String username;
    public String email;
    public String phone;
    public String bio;
    public String uid;
    public String userkey;

    public MyUser() {}

    public MyUser(String username, String email, String phone, String bio, String uid, String userkey) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.uid = uid;
        this.userkey = userkey;
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() { return phone; }
    public String getUid() {
        return uid;
    }
    public String getUserkey() {
        return userkey;
    }
    public String getBio(){
        return bio;
    }

}
