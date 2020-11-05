/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.booksies;

import com.example.booksies.model.Books;
import com.example.booksies.model.book_status;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooksTest {
    String ISBN = "978-0345339737";
    String author = "JRR Tolkien";
    String title = "The Lord of the Rings: The Return of the King";
    Books book_1 = new Books(ISBN, author, title);

    @Test
    public void getISBN() {
        Books book_1 = new Books(ISBN, author, title);
        String temp_ISBN = "978-0345339730";
        assertEquals(book_1.getISBN(), ISBN);
        book_1.setISBN(temp_ISBN);
        assertEquals(book_1.getISBN(), temp_ISBN);
    }

    @Test
    public void getAuthor() {
        Books book_1 = new Books(ISBN, author, title);
        String temp_author = "Michael Elgie";
        assertEquals(book_1.getAuthor(), author);
        book_1.setAuthor(temp_author);
        assertEquals(book_1.getAuthor(), temp_author);
    }

    @Test
    public void getTitle() {
        Books book_1 = new Books(ISBN, author, title);
        String temp_title = "The Lord of the Rings: The Two Towers";
        assertEquals(book_1.getTitle(), title);
        book_1.setTitle(temp_title);
        assertEquals(book_1.getTitle(), temp_title);
    }

    @Test
    public void getStatus() {
        Books book_1 = new Books(ISBN, author, title);
        book_status temp_status = book_status.AVAILABLE;
        assertEquals(book_1.getStatus(), temp_status);
        temp_status = book_status.REQUESTED;
        book_1.setStatus(temp_status);
        assertEquals(book_1.getStatus(), temp_status);
        temp_status = book_status.BORROWED;
        book_1.setStatus(temp_status);
        assertEquals(book_1.getStatus(), temp_status);
        temp_status = book_status.ACCEPTED;
        book_1.setStatus(temp_status);
        assertEquals(book_1.getStatus(), temp_status);
    }

    @Test
    public void getOwner() {
    }
}