package com.example.booksies.model.books;

import java.util.ArrayList;

/**
 * Every book class must implement interface to be identifiable
 */

public interface Identifiable {

    String getISBN();

    void setISBN(String ISBN);

    String getTitle();

    void setTitle(String title);


    String getOwner();

    void setOwner(String owner);


    String getDocID();

    void setDocID(String docID);

}
