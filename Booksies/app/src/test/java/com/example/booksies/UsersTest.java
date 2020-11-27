package com.example.booksies;

import android.util.Log;

import com.example.booksies.model.User;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UsersTest {
    String userid = "12345abcde";
    String username = "user123";
    String email = "123@gmail.com";
    String phone = "1231231234";


    @Test
    public void getSetUserid() {
        User testUser = new User(userid, username, email, phone);
        String testUserid = "abcde12345baghkjlafgbaksgbl";
        assertEquals(testUser.getUserid(), userid);
        testUser.setUserid(testUserid);
        assertEquals(testUser.getUserid(), testUserid);
    }

    @Test
    public void getSetUsername() {
        User testUser = new User(userid, username, email, phone);
        String testUsername = "bkagbjkdagb";
        assertEquals(testUser.getUsername(), username);
        testUser.setUsername(testUsername);
        assertEquals(testUser.getUsername(), testUsername);
    }

    @Test
    public void getSetEmail() {
        User testUser = new User(userid, username, email, phone);
        String testEmail = "sladfbuibvb@gmail.com";
        assertEquals(testUser.getEmail(), email);
        testUser.setEmail(testEmail);
        assertEquals(testUser.getEmail(), testEmail);
    }

    @Test
    public void getSetPhone() {
        User testUser = new User(userid, username, email, phone);
        String testPhone = "12345643145311";
        assertEquals(testUser.getPhone(), phone);
        testUser.setPhone(testPhone);
        assertEquals(testUser.getPhone(), testPhone);
    }

}
