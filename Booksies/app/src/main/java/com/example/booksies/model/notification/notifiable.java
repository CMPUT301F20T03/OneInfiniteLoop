package com.example.booksies.model.notification;

/**
 * Every notification class must implement interface to be identifiable
 */
public interface notifiable {

    /**
     * Interface for getTitle
     * @return String
     */
    String getTitle();

    /**
     * Interface for setTitle
     * @param title: String
     */
    void setTitle(String title);

    /**
     * Interface for getBody
     * @return String
     */
    String getBody();

    /**
     * Interface for setBody
     * @param body: String
     */
    void setBody(String body);
}
