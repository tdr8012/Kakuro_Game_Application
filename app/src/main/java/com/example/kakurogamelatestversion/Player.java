package com.example.kakurogamelatestversion;

import com.google.firebase.Timestamp;

public class Player {
    public String uid;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public Timestamp createdAt;
    public String imgUrl;
    public Integer score;
    public String role;

    public Player() {}

    public Player(String uid, String firstName, String lastName, String email,
                  String phoneNumber, Timestamp createdAt, String imgUrl, Integer score, String role) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.imgUrl = imgUrl;
        this.score = score;
        this.role = role;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
