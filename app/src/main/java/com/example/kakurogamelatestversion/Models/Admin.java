package com.example.kakurogamelatestversion.Models;

public class Admin {
    public String uid;
    public String email;
    public String role;

    public Admin() {} // Required for Firebase

    public Admin(String uid, String email,String role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }
}
