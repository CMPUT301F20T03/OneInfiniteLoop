package com.example.booksies.model.user;

import com.example.booksies.model.books.Books;

import java.util.ArrayList;

public interface userIdentifiable {

    String getUserid();

    void setUserid(String userid);

    String getUsername();

    void setUsername(String username);

    String getEmail();

    void setEmail(String email) ;

    String getPhone();

    void setPhone(String phone);
}
