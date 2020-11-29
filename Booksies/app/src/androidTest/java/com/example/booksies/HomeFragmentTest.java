package com.example.booksies;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.MainActivity;
import com.example.booksies.controller.NavigationActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * US 01.04.01 / US 01.05.01
 */
public class HomeFragmentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "test");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");

        solo.clickOnText("Login");

        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));
        assertTrue(solo.waitForText("Title", 1, 2000));
        assertTrue(solo.waitForText("Author", 1, 2000));
        assertTrue(solo.waitForText("ISBN", 1, 2000));
        assertTrue(solo.waitForText("Comments", 1, 2000));
        assertTrue(solo.waitForText("Add Photo", 1, 2000));

        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test Book");

        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test Author");

        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "1234");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));

    }

    // US 1.04.01
    @Test
    public void checkMyBooks(){

        assertTrue(solo.waitForText("UI Test Book".toUpperCase(),1,2000));
        assertTrue(solo.waitForText("UI Test Author".toUpperCase(),1,2000));
        assertTrue(solo.waitForText("1234",1,2000));

    }

    //US 1.05.01
    @Test
    public void checkFilter(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);

        Spinner spinner = (Spinner)solo.getView(R.id.filter);
        spinner.setSelection(1, true);

        assertFalse(solo.waitForText("accepted",1,2000));
        assertFalse(solo.waitForText("requested",1,2000));
        assertFalse(solo.waitForText("borrowed",1,2000));



    }

    @After
    public void TearDown(){
        solo.waitForText("Title",1,2000);

        solo.clickLongOnText("UI TEST BOOK");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.delete_button)));
        solo.clickOnView(solo.getView(android.R.id.button1));


    }

}
