package com.example.booksies;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.bumptech.glide.Glide;
import com.example.booksies.controller.EditUserProfileActivity;
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

    public IntentsTestRule<NavigationActivity> intentsRule = new IntentsTestRule<>(NavigationActivity.class);


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

    @Test
    public void checkFilter(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);

        Spinner spinner = (Spinner)solo.getView(R.id.filter);
        spinner.setSelection(1, true);

        assertFalse(solo.waitForText("accepted",1,2000));
        assertFalse(solo.waitForText("requested",1,2000));
        assertFalse(solo.waitForText("borrowed",1,2000));



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
    public void checkAddBookOpenCamera() {
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test Book");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test Author");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "UI Test Author");

        solo.clickOnImageButton(0);

        solo.waitForText("Take Photo", 1, 2000);
        solo.waitForText("Upload From Gallery", 1, 2000);
        solo.waitForText("Cancel", 1, 2000);

        solo.clickOnText("Take Photo");

        solo.waitForActivity(MediaStore.ACTION_IMAGE_CAPTURE);

    }

    @Test
    public void checkAddBookOpenGallery() {
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));

        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test Book");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test Author");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "UI Test Author");

        solo.clickOnImageButton(0);
        solo.waitForText("Take Photo", 1, 2000);
        solo.waitForText("Upload From Gallery", 1, 2000);
        solo.waitForText("Cancel", 1, 2000);

        solo.clickOnText("Upload From Gallery");

        solo.waitForActivity(Intent.ACTION_PICK);
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
        solo.clearEditText((EditText) solo.getCurrentActivity().findViewById(R.id.username_edit));
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.username_edit), "UI Test username edit");
        solo.clearEditText((EditText) solo.getCurrentActivity().findViewById(R.id.phone_number_edit));
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.phone_number_edit), "UI Test phone edit");
        solo.clickOnText("Ok");


        assertTrue(solo.waitForText("UI Test username edit", 1, 2000));
        assertTrue(solo.waitForText("UI Test phone edit", 1, 2000));
    }

}
