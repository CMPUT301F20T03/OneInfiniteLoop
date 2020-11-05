package com.example.booksies;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.NavigationActivity;
import com.example.booksies.controller.ViewPhotoActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;


public class NavigationActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<NavigationActivity>(NavigationActivity.class, true, true);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void checkMyBooks(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));
        solo.enterText((EditText) solo.getView(R.id.titleEditText), "Calculus");
        solo.enterText((EditText) solo.getView(R.id.authorEditText), "Stewart");
        solo.enterText((EditText) solo.getView(R.id.ISBNEditText), "34555631");
        solo.enterText((EditText) solo.getView(R.id.commentEditText), "Good Condition");


        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.addButton));
        assertTrue(solo.waitForText("Calculus",1,2000));
        assertTrue(solo.waitForText("Stewart",1,2000));
    }
}
