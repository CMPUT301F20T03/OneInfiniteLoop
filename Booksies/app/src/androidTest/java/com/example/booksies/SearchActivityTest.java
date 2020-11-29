package com.example.booksies;

import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.MainActivity;
import com.example.booksies.controller.NavigationActivity;
import com.example.booksies.controller.SearchActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * US 3.01.01/3.02.01
 */

public class SearchActivityTest {

    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);


    @Before
    public void setUp(){


        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "test");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");

    }

    @Test
    public void testSearch(){
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));
        assertTrue(solo.waitForText("Title", 1, 2000));
        assertTrue(solo.waitForText("Author", 1, 2000));
        assertTrue(solo.waitForText("ISBN", 1, 2000));
        assertTrue(solo.waitForText("Comments", 1, 2000));
        assertTrue(solo.waitForText("Add Photo", 1, 2000));

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test SearchBook");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test SearchAuthor");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "12345678901234");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));

        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnText("Logout");
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        assertTrue(solo.waitForText("Find books ...", 1, 2000 ));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_bar));
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);
        solo.enterText(0, "UI Test SearchBook");
        assertTrue(solo.waitForText("UI Test SearchAuthor".toUpperCase(),1,3000));
        solo.goBack();
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnText("Logout");
    }

    @After
    public void TearDown(){
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "test");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.waitForText("Title",1,2000);

        solo.clickLongOnText("UI TEST SEARCHBOOK");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.delete_button)));
        solo.clickOnView(solo.getView(android.R.id.button1));


    }



}
