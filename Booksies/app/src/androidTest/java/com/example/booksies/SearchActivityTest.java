package com.example.booksies;

import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.NavigationActivity;
import com.example.booksies.controller.SearchActivity;
import com.robotium.solo.Solo;

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
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<NavigationActivity>(NavigationActivity.class, true, true);

    public IntentsTestRule<NavigationActivity> intentsRule = new IntentsTestRule<>(NavigationActivity.class);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void testSearch(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_bar));
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);
        solo.enterText(0, "Sir Arthur");
        assertTrue(solo.waitForText("SHERLOCK HOLMES",1,3000));


    }

}
