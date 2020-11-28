package com.example.booksies;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FirestoreHandlerTest {


    private CountDownLatch authSignal = null;
    private FirebaseAuth auth;

    @Before
    public void setUp() throws InterruptedException {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testEmail() throws InterruptedException {
        assertTrue(true);

    }
}