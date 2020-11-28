package com.example.booksies.model.user;

import com.example.booksies.model.books.Books;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Class models a book object.
 * This is the class that keep track of user object
 */

public class User implements userIdentifiable {

    private String userid;
    private String username;
    private String email;
    private String phone;
    private ArrayList<Books> myBooks;
    FirebaseFirestore db;
    //DocumentReference docRef;


    /**
     * Constructor for Books class
     * @param userid string
     * @param username  string
     * @param email string
     * @param phone string
     * @param myBooks ArrayList<Books>
     */
    public User(String userid, String username, String email, String phone, ArrayList<Books> myBooks) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.myBooks = myBooks;
    }


    /**
     * Constructor for Books class
     * @param userid string
     * @param username  string
     * @param email string
     * @param phone string
     */
    public User(String userid, String username, String email, String phone) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.myBooks = null;
    }


    /**
     * get the userID
     * @return document id
     */
    public String getUserid() {
        return userid;
    }

    /**
     * set the userID
     * @param userid document id
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }


    /**
     * get the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * set the username
     * @param username string
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get the email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set the email
     * @param email string
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get the list of my books
     * @return list of my books
     */
    public ArrayList<Books> getMyBooks() {
        return myBooks;
    }

    /**
     * set the list of my books
     * @param myBooks ArrayList<Books>
     */
    public void setMyBooks(ArrayList<Books> myBooks) {
        if (myBooks == null){
            this.myBooks = new ArrayList<Books>() ;
        }else{
            this.myBooks = myBooks;
        }

    }

    /**
     * get the phone number
     * @return phone number string
     */
    public String getPhone() {
        return phone;
    }

    /**
     * set the phone number
     * @param phone string
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
