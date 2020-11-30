/*
*
 */

package com.example.booksies.model.notification;
/**
 * Configure a notification
 */
public class Notification implements notifiable {
    public String title;
    public String body;

    /**
     * Constructor for Notification class
     * @param title: String
     * @param body: String
     */
    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    /**
     * Get title of Notification
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title of Notification
     * @param title: String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get body of Notification
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     * Set body of Notification
     * @param body: String
     */
    public void setBody(String body) {
        this.body = body;
    }

}
