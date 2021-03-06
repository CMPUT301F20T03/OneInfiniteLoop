package com.example.booksies;

import android.widget.EditText;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.MainActivity;
import com.example.booksies.controller.NavigationActivity;
import com.example.booksies.controller.SearchActivity;
import com.example.booksies.controller.ViewProfileActivity;
import com.example.booksies.controller.SetLocationActivity;
import com.example.booksies.controller.ViewMapsActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * US 02.03.01 / 04.01.01 / 04.02.01 / 04.03.01 / 05.01.01 / 05.02.01 / 05.04.01 / US 09.01.01 / US 09.02.01
 */
public class TransactionTest {

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
        requestNotification();
        logout();
        acceptRequest();
        setLocation();
        logout();
        acceptNotification();
        logout();
        bookAccepted();
        mapOpens();
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

        //View user profile Test
        solo.clickOnText("lendertest");
        solo.waitForActivity(ViewProfileActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewProfileActivity.class);
        assertTrue(solo.waitForText("lendertest", 1, 3000));
        assertTrue(solo.waitForText("123456789", 1 ,3000));
        assertTrue(solo.waitForText("lendtest@gmail.com", 1, 3000));
        solo.goBack();
        solo.waitForActivity(SearchActivity.class);
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.req_button));
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

        //View user profile Test
        solo.clickOnText("borrowertest");
        solo.waitForActivity(ViewProfileActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewProfileActivity.class);
        assertTrue(solo.waitForText("borrowertest", 1, 3000));
        assertTrue(solo.waitForText("123456789", 1 ,3000));
        assertTrue(solo.waitForText("borrowtest@gmail.com", 1, 3000));
        solo.goBack();
        assertTrue(solo.waitForText("borrowerTest".toLowerCase(),1,3000));

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.accept));
        assertTrue(solo.waitForText("accepted", 1, 5000));
    }

    public void setLocation() {
        solo.clickOnText("1234");
        assertTrue(solo.waitForText("borrowertest", 1, 3000));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.map));
        solo.waitForActivity(SetLocationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));
        solo.clickOnScreen(200,800, 1);
        solo.sleep(5000);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));
        solo.goBack();
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

        //View user profile Test
        solo.clickOnText("lendertest");
        solo.waitForActivity(ViewProfileActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewProfileActivity.class);
        assertTrue(solo.waitForText("lendertest", 1, 3000));
        assertTrue(solo.waitForText("123456789", 1 ,3000));
        assertTrue(solo.waitForText("lendtest@gmail.com", 1, 3000));
        solo.goBack();
        assertTrue(solo.waitForText("accepted", 1, 5000 ));

    }

    public void mapOpens()
    {
        solo.clickOnText("UI TEST LENDBOOK");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.map2));
        solo.waitForActivity(ViewMapsActivity.class);
        solo.goBack();
    }

    public void requestNotification(){
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "lenderTest");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");

        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_profile));
        assertTrue(solo.waitForText("lenderTest", 1, 2000));
        assertTrue(solo.waitForText("123456789", 1, 2000));
        assertTrue(solo.waitForText("lendtest@gmail.com", 1, 2000));
        assertTrue(solo.waitForText("Notifications", 1, 2000));
        assertTrue(solo.waitForText("borrowertest", 1, 2000));
        assertTrue(solo.waitForText("has requested UI Test LendBook", 1, 2000));

        //View user profile Test
        solo.clickOnText("borrowertest");
        solo.waitForActivity(ViewProfileActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewProfileActivity.class);
        assertTrue(solo.waitForText("borrowertest", 1, 3000));
        assertTrue(solo.waitForText("123456789", 1 ,3000));
        assertTrue(solo.waitForText("borrowtest@gmail.com", 1, 3000));
        solo.goBack();
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
    }

    public void acceptNotification() {
        assertTrue(solo.waitForText("Username", 1, 2000 ));
        assertTrue(solo.waitForText("Password", 1, 2000 ));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "borrowerTest");
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnText("Login");

        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_profile));
        assertTrue(solo.waitForText("borrowerTest", 1, 2000));
        assertTrue(solo.waitForText("123456789", 1, 2000));
        assertTrue(solo.waitForText("borrowtest@gmail.com", 1, 2000));
        assertTrue(solo.waitForText("Notifications", 1, 2000));
        assertTrue(solo.waitForText("lendertest", 1, 2000));
        assertTrue(solo.waitForText("has accepted your requests for UI Test LendBook", 1, 2000));

        //View user profile Test
        solo.clickOnText("lendertest");
        solo.waitForActivity(ViewProfileActivity.class);
        solo.assertCurrentActivity("Wrong activity", ViewProfileActivity.class);
        assertTrue(solo.waitForText("lendertest", 1, 3000));
        assertTrue(solo.waitForText("123456789", 1 ,3000));
        assertTrue(solo.waitForText("lendtest@gmail.com", 1, 3000));
        solo.goBack();
        solo.waitForActivity(NavigationActivity.class);
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
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

        solo.clickLongOnText("UI TEST LENDBOOK");
        solo.clickOnView(solo.getCurrentActivity().findViewById((R.id.delete_button)));
        solo.clickOnView(solo.getView(android.R.id.button1));

    }
}
