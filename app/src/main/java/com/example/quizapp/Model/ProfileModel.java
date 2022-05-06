package com.example.quizapp.Model;

public class ProfileModel {

    private String name;
    private String email;
    private String phoneNumber;
    private int bookmarkCount;

    public ProfileModel(String name, String email,String phoneNumber,int bookmarkCount) {
        this.name = name;
        this.email = email;
        this.phoneNumber  =phoneNumber;
        this.bookmarkCount = bookmarkCount;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
