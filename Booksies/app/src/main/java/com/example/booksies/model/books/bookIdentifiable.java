package com.example.booksies.model.books;

import java.util.ArrayList;

/**
 * Every book class must implement interface to be identifiable
 * @author; Alireza Azimi (sazimi)
 */

public interface bookIdentifiable {

    /**
     * get the isbn code
     * @return isbn code as string
     */
    String getISBN();

    /**
     * set the isbn code
     * @param ISBN isbn value
     */
    void setISBN(String ISBN);

    /**
     * gets the book title
     * @return the book title
     */
    String getTitle();

    /**
     * set's the book title
     * @param title the book title
     */
    void setTitle(String title);


    /**
     * get book owner
     * @return book owner
     */
    String getOwner();

    /**
     * set the book owner
     * @param owner
     */
    void setOwner(String owner);

    /**
     * gets document id of book object
     * @return document id
     */
    String getDocID();


    /**
     * sets document id of book object
     * @param docID firestore document id of book
     */
    void setDocID(String docID);

}
