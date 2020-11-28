package com.example.booksies;

import android.widget.EditText;

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

public class TranscationTest {

    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void bookTransaction()
    {
        setupBooks();
        logout();
        requestBook();
        logout();
        acceptRequest();
        logout();
        bookAccepted();
        logout();

    }

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

    }

    public void logout()
    {

        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnText("Logout");
    }


    public void setupBooks(){
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "lenderTest");
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
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.titleEditText), "UI Test LendBook");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.authorEditText), "UI Test LendAuthor");
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.ISBNEditText), "1234");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.addButton)));

    }


    public void requestBook()
    {
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.enterText((EditText) solo.getView(R.id.username), "borrowerTest");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");

        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        assertTrue(solo.waitForText("Find books ...", 1, 2000 ));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_bar));
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);
        solo.enterText(0, "UI Test LendBook");
        assertTrue(solo.waitForText("UI Test LendAuthor".toUpperCase(),1,3000));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_layout));
        assertTrue(solo.waitForText("Yes",1,5000));
        solo.clickOnText("Yes");
        solo.goBack();
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_request));
        assertTrue(solo.waitForText("requested", 1, 5000 ));
    }


    public void acceptRequest()
    {
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "lenderTest");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        assertTrue(solo.waitForText("UI Test LendAuthor".toUpperCase(),1,3000));
        assertTrue(solo.waitForText("requested", 1, 5000 ));
        solo.clickOnText("UI Test LendBook".toUpperCase());
        assertTrue(solo.waitForText("borrowerTest".toLowerCase(),1,3000));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.accept));
        assertTrue(solo.waitForText("accepted", 1, 3000 ));

    }

    public void bookAccepted()
    {
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.enterText((EditText) solo.getView(R.id.username), "borrowerTest");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");

        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_request));
        assertTrue(solo.waitForText("accepted", 1, 5000 ));
        assertTrue(solo.waitForText("lenderTest".toLowerCase(), 1, 5000 ));

    }

    @After
    public void TearDown(){

        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "lenderTest");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.waitForText("Title",1,2000);

        solo.clickLongOnScreen(240,800,3000);
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.delete_button)));


    }
}
