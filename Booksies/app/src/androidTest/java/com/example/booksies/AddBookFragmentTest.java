package com.example.booksies;

import android.content.Intent;
import android.hardware.Camera;
import android.provider.MediaStore;
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

public class AddBookFragmentTest {
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

    }

    @Test
    public void checkAddBookRequiredFieldsAndAddNoImage() {
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));
        assertTrue(solo.waitForText("Title", 1, 2000));
        assertTrue(solo.waitForText("Author", 1, 2000));
        assertTrue(solo.waitForText("ISBN", 1, 2000));
        assertTrue(solo.waitForText("Comments", 1, 2000));
        assertTrue(solo.waitForText("Add Photo", 1, 2000));

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test Book");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));
        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test Author");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));
        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "1234");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));

        solo.waitForText("Title",1,2000);

        solo.clickLongOnScreen(240,800,3000);
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.delete_button)));

    }



    @Test
    public void checkAddBookOpenCameraOrGalleryOption() {
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));
        assertTrue(solo.waitForText("Title", 1, 2000));
        assertTrue(solo.waitForText("Author", 1, 2000));
        assertTrue(solo.waitForText("ISBN", 1, 2000));
        assertTrue(solo.waitForText("Comments", 1, 2000));
        assertTrue(solo.waitForText("Add Photo", 1, 2000));

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test Book");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test Author");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "UI Test Author");

        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.cameraImageView)));

        solo.waitForText("Take Photo", 1, 2000);
        solo.waitForText("Upload From Gallery", 1, 2000);
        solo.waitForText("Cancel", 1, 2000);



    }


    @After
    public void TearDown(){



    }
}
