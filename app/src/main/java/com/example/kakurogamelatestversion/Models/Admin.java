package com.example.kakurogamelatestversion.Models;

public class Admin {
    public String uid;
    public String email;

    public Admin() {} // Required for Firebase

    public Admin(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }
}
