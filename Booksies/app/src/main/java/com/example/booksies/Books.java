package com.example.booksies;

import android.app.DownloadManager; // potentially temporary for     ArrayList<DownloadManager.Request> bookRequests        to not throw any errors

import java.util.ArrayList;
// enum for status
enum book_status {
    UNUSED,
    WAITING_FOR_PICKUP,
    BORROWED,
    WAITING_FOR_DROPOFF
}

public class Books {

    private String ISBN;
    private String author;
    private String title;
    private book_status status;
    private String owner;
    ArrayList<DownloadManager.Request> bookRequests;




    // Book constructor
    public Books(String ISBN, String author, String title) {
        this.ISBN = ISBN;
        this.author = author;
        this.title = title;
        this.status = book_status.UNUSED;
//        this.owner = owner;   // TODO: assign this owner to a book when creating instance
    }

    // Getters and Setters
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public book_status getStatus() {
        return status;
    }

    public void setStatus(book_status status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
