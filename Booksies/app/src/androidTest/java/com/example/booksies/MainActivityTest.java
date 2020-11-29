package com.example.booksies;

import android.widget.EditText;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.MainActivity;
import com.example.booksies.controller.NavigationActivity;
import com.example.booksies.controller.RegisterActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * US 02.01.01
 */
public class MainActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void signInTest() {
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnText("Login");
        solo.waitForText("You didn't fill in the all the fields!",
                1, 2000);
        solo.enterText((EditText) solo.getView(R.id.username), "test");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");

        solo.clickOnText("Login");

        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);

    }

    @Test
    public void registerAccountTest() {
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnText("Create a User");

        solo.waitForActivity(RegisterActivity.class);
        solo.assertCurrentActivity("Wrong activity", RegisterActivity.class);

        solo.enterText((EditText) solo.getView(R.id.emailEditText), "TestUsername@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.passwordEditText), "123456");
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "TestUsername");
        solo.enterText((EditText) solo.getView(R.id.phoneEditText), "7801233451");

        solo.clickOnText("Register");

        if(solo.waitForActivity(MainActivity.class, 3000)){
            solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        } else {
            solo.assertCurrentActivity("Wrong activity", RegisterActivity.class);
            solo.waitForText("Username taken.",
                    1, 2000);

        }

    }
}
