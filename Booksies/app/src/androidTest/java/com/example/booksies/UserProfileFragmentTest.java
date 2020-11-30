package com.example.booksies;

import android.widget.EditText;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.EditUserProfileActivity;
import com.example.booksies.controller.MainActivity;
import com.example.booksies.controller.NavigationActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * US 02.02.01
 */
public class UserProfileFragmentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "test");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");

        solo.clickOnText("Login");
    }

    @Test
    public void checkUserProfileFragment(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_profile));

        assertTrue(solo.waitForText("Cell:", 1, 2000 ));
        assertTrue(solo.waitForText("Email:", 1, 2000 ));
        assertTrue(solo.waitForText("Notifications", 1, 2000 ));

    }

    @Test
    public void checkEditProfile()  {
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_profile));
        solo.clickOnText("Edit");

        solo.waitForActivity(EditUserProfileActivity.class);
        solo.clearEditText((EditText) solo.getCurrentActivity().findViewById(R.id.user_email_edit));
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.user_email_edit), "UI Test username edit");
        solo.clearEditText((EditText) solo.getCurrentActivity().findViewById(R.id.phone_number_edit));
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.phone_number_edit), "UI Test phone edit");
        solo.clickOnText("Ok");


        assertTrue(solo.waitForText("UI Test username edit", 1, 2000));
        assertTrue(solo.waitForText("UI Test phone edit", 1, 2000));
    }

    @After
    public void TearDown(){


    }
}
