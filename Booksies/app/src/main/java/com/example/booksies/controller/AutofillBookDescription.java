package com.example.booksies.controller;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.booksies.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * AutofillBookDescription handles the api that retrieves author, title, comments of a book through
 * a google api by using ISBN code. It is called from AddBookFragment while adding a new book.
 */
public class AutofillBookDescription extends AsyncTask<String, Void, String> {
    private final String TAG = getClass().getSimpleName();
    private String ISBN;
    private View mView;
    private EditText title;
    private EditText author;
    private EditText description;

    /**
     * Constructor for AutofillBookDescription
     * @param ISBN: ISBN code to be used to look for a book
     * @param title: title of book
     * @param author: author of book
     * @param description: description of book
     * @param mView: parent View of AddBookFragment
     */
    public AutofillBookDescription(String ISBN, EditText title, EditText author, EditText description, View mView) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.mView = mView;
    }

    /**
     * Start task to be done in background
     * @param strings: ISBN
     * @return book info in JSON
     */
    @Override
    protected String doInBackground(String... strings) {
        return getBookInfo(strings[0]);
    }

    /**
     * Converts results of API call in JSON to separate strings and binds to UI
     * @param bookDescriptionJSON: Result JSON of API call
     */
    @Override
    protected void onPostExecute(String bookDescriptionJSON) {
        super.onPostExecute(bookDescriptionJSON);
        try {
            JSONObject jsonObject = new JSONObject(bookDescriptionJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            JSONObject volumeInfo = jsonArray.getJSONObject(0).getJSONObject("volumeInfo");

            EditText titleEditText = mView.findViewById(R.id.titleEditText);
            EditText authorEditText = mView.findViewById(R.id.authorEditText);
            EditText commentsEditText = mView.findViewById(R.id.commentEditText);

            titleEditText.setText(volumeInfo.getString("title"));
            authorEditText.setText(volumeInfo.getJSONArray("authors").getString(0));
            commentsEditText.setText(volumeInfo.getString("description"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get Book Information like author, title, comments using ISBN of book
     * @param ISBN: ISBN of book to be searched
     * @return book description in JSON
     */
    public static String getBookInfo(String ISBN){
        HttpURLConnection httpUrlConn = null;
        BufferedReader bufferedReader = null;
        String bookDescriptionJSON = null;
        String line;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            Uri uri = Uri.parse("https://www.googleapis.com/books/v1/volumes?").buildUpon()
                    .appendQueryParameter("q", "=isbn:" + ISBN).appendQueryParameter("printType", "books").build();

            URL url = new URL(uri.toString());
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();
            InputStream inputStream = httpUrlConn.getInputStream();

            if (inputStream == null) return null;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) stringBuffer.append(line + "\n");
            if (stringBuffer.length() == 0) return null;
            bookDescriptionJSON = stringBuffer.toString();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if (httpUrlConn != null) httpUrlConn.disconnect();

            if (bufferedReader!=null){
                try{
                    bufferedReader.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            return bookDescriptionJSON;
        }

    }

    /**
     * Get author of book
     * @return author of book
     */
    public EditText getAuthor() {
        return author;
    }

    /**
     * Set author of book
     * @param author: author of book
     */
    public void setAuthor(EditText author) {
        this.author = author;
    }

    /**
     * Get title of book
     * @return title of book
     */
    public EditText getTitle() {
        return title;
    }

    /**
     * Set title of book
     * @param title: title of book
     */
    public void setTitle(EditText title) {
        this.title = title;
    }

    /**
     * Get ISBN of book
     * @return ISBN of book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Set ISBN of book
     * @param ISBN: ISBN of book
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Get description of book
     * @return description of book
     */
    public EditText getDescription() {
        return description;
    }

    /**
     * Set description of book
     * @param description: description of book
     */
    public void setDescription(EditText description) {
        this.description = description;
    }
}


