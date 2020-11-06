package com.example.booksies.model;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * This is the class that keep track of user object
 */

public class User {

    private String userid;
    private String username;
    private String email;
    private String phone;
    private ArrayList<Books> myBooks;
    FirebaseFirestore db;
    //DocumentReference docRef;

    public User(String userid, String username, String email, String phone, ArrayList<Books> myBooks) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.myBooks = myBooks;
    }

    public User(String userid, String username, String email, String phone) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phone = phone;
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
