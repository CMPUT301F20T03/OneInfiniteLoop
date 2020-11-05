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


public class FirestoreHandlerTest {


    private CountDownLatch authSignal = null;
    private FirebaseAuth auth;

    @Before
    public void setUp() throws InterruptedException {
        authSignal = new CountDownLatch(1);
//        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword("sazimi@ualberta.ca", "123456").addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            final AuthResult result = task.getResult();
                            final FirebaseUser user = result.getUser();
                            authSignal.countDown();
                        }
                    });
        } else {
            authSignal.countDown();
        }
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        if(auth != null) {
            auth.signOut();
            auth = null;
        }
    }

    @Test
    public void testEmail() throws InterruptedException {

        assertEquals("sazimi@ualberta.ca", FirestoreHandler.getCurrentUserEmail());

    }
}