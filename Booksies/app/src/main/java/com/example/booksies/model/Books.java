package com.example.booksies.model;


import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Class models a book object.
 * This class keeps track of the attributes of a book object
 */
public class Books {
    private String title;
    private String author;
    private String ISBN;
    private book_status status;
    private String owner;
    public boolean expand;
    private String imageUrl;
    private String comments;
    private String docID;
    private ArrayList<String> borrower;

    private ArrayList<String> bookRequests;


    /**
     * Constructor for Books class
     * @param ISBN
     * @param author
     * @param title
     */
    public Books(String ISBN, String author, String title) {
        this.ISBN = ISBN;
        this.author = author;
        this.title = title;
        this.expand = false;
        this.status = book_status.AVAILABLE;

    }


    // Getters and Setters

    /**
     * get the isbn code
     * @return isbn code
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * set the isbn code
     * @param ISBN isbn value
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * gets the book author
     * @return author value
     */
    public String getAuthor() {
        return author;
    }

    /**
     * sets the book author
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * gets the book title
     * @return the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set's the book title
     * @param title the book title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return status of the book
     */
    public book_status getStatus() { return status; }

    /**
     * sets book status
     * @param status status of the book
     */
    public void setStatus(book_status status) {
        this.status = status;
    }

    /**
     * get book item expand view mode
     * @return bool for is it in expand view mode?
     */
    public boolean getExpand() { return expand; }

    /**
     * set book item expand mode
     * @param expand the expand mode
     */
    public void setExpand(boolean expand) { this.expand = expand; }

    /**
     * get book owner
     * @return book owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * set the book owner
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * get book image url
     * @return image url string
     */

    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * set image url
     * @param imageUrl image url string
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * get book comments
     * @return book comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * set book comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * gets document id of book object
     * @return document id
     */
    public String getDocID() {
        return docID;
    }

    /**
     * sets document id of book object
     * @param docID firestore document id of book
     */
    public void setDocID(String docID) {
        this.docID = docID;
    }

    /**
     *  Returns list of all requests
     * @return list of all book requests
     */
    public ArrayList<String> getBookRequests() {
        return bookRequests;
    }

    /**
     * set's the list of book requests
     * @param bookRequests list of book requests
     */
    public void setBookRequests(ArrayList<String> bookRequests) {
        this.bookRequests = bookRequests;
    }

    public ArrayList<String> getBorrower() {
        return borrower;
    }

    public void setBorrower(ArrayList<String> borrower) {
        this.borrower = borrower;
    }
}
