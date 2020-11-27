package com.example.booksies;

import android.app.Instrumentation;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.booksies.model.FirestoreHandler;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

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