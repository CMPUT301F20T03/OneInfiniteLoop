package com.example.booksies;

import com.example.booksies.model.notification.Notification;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationTest {

    String title = "user123";
    String body = "book123";

    @Test
    public void getSetTitle(){
        Notification testNotification = new Notification(title, body);
        String testTitle = "abbdfnekgqa";
        assertEquals(testNotification.getTitle(), title );
        testNotification.setTitle(testTitle);
        assertEquals(testNotification.getTitle(), testTitle);
    }

    @Test
    public void getSetBody(){
        Notification testNotification = new Notification(title,body);
        String testBody = "testbook1234512123";
        assertEquals(testNotification.getBody(),body);
        testNotification.setBody(testBody);
        assertEquals(testNotification.getBody(), testBody);
    }

}
