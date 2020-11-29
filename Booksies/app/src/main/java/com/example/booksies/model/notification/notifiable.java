package com.example.booksies.model.notification;

/**
 * Every notification class must implement interface to be identifiable
 */
public interface notifiable {

    String getTitle();

    void setTitle(String title);

    String getBody();

    void setBody(String body);
}
