package com.example.booksies;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
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
    public void checkList(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_home));
        assertTrue(solo.waitForText("War and Peace",1,2000));
        assertTrue(solo.waitForText("Les Miserable",1,2000));

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

        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));
        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test Book");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));
        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test Author");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));
        solo.waitForText("Adding a book requires", 1, 2000);

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "1234");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));

    }

    @Test
    public void checkAddBookWithCameraImage() {
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

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.addImageButton));
        solo.waitForText("Take Photo", 1, 2000);
        solo.waitForText("Upload From Gallery", 1, 2000);
        solo.waitForText("Cancel", 1, 2000);

        solo.clickOnText("Take Photo");

        solo.waitForActivity(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    @Test
    public void checkAddBookWithGalleryImage() {
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

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.addImageButton));
        solo.waitForText("Take Photo", 1, 2000);
        solo.waitForText("Upload From Gallery", 1, 2000);
        solo.waitForText("Cancel", 1, 2000);

        solo.clickOnText("Upload From Gallery");

        solo.waitForActivity(Intent.ACTION_PICK);
    }
}
