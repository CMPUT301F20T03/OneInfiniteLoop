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
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        assertTrue(solo.waitForText("Find books ...", 1, 2000 ));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_bar));
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);
        solo.enterText(0, "Delirium");
        assertTrue(solo.waitForText("LAUREN OLIVER",1,3000));


    }

    @After
    public void TearDown(){

    }



}
