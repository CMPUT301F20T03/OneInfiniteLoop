package com.example.booksies;

import android.app.DownloadManager;

import java.util.ArrayList;

public abstract class User {

    private String userid;
    private String username;
    private String email;
    private String phone;
    private ArrayList<Books> myBooks;
    //private ArrayList<Requests> myRequests;


    public User(String userid, String username, String email, String phone, ArrayList<Books> myBooks) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.myBooks = myBooks;
    }

    public User(String userid, String username, String email) {
        this.userid = userid;
        this.username = username;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Books> getMyBooks() {
        return myBooks;
    }

    public void setMyBooks(ArrayList<Books> myBooks) {
        if (myBooks == null){
            this.myBooks = new ArrayList<Books>() ;
        }else{
            this.myBooks = myBooks;
        }

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //public void sendNotification(ArrayList<Requests> myRequests)
}
