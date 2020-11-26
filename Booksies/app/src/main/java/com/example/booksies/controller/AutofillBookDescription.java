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
 * This class is used to get the books description, it works with the BookWebAPIHelper
 */
public class AutofillBookDescription extends AsyncTask<String, Void, String> {
    private final String TAG = getClass().getSimpleName();
    private String ISBN;
    private View mView;
    private EditText title;
    private EditText author;
    private EditText description;

    /**
     * The constructor
     * @param ISBN: the book's ISBN
     * @param title: the book's title
     * @param author: the book's author
     * @param description: the book's description
     */
    public AutofillBookDescription(String ISBN, EditText title, EditText author, EditText description, View mView) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.mView = mView;
    }

    /**
     * Start the task that will be done in the background
     * @param strings: an unspecified amount of parameters, in this case it is the ISBN
     * @return the book info in JSON
     */
    @Override
    protected String doInBackground(String... strings) {
        return getBookInfo(strings[0]);
    }

    /**
     * Converts the results of the API call in JSON to separate strings and binds them to UI
     * @param bookDescriptionJSON: The results in JSON of the API call
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

    public static String getBookInfo(String ISBN){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String bookDescriptionJSON = null;

        try {

            Uri uri = Uri.parse("https://www.googleapis.com/books/v1/volumes?").buildUpon()
                    .appendQueryParameter("q", "=isbn:" + ISBN)
                    .appendQueryParameter("printType", "books").build();

            URL url = new URL(uri.toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) return null;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0) return null;

            bookDescriptionJSON = stringBuffer.toString();

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();

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
     * Get the ISBN
     * @return the ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Set the ISBN
     * @param ISBN: the ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Get the title
     * @return the title
     */
    public EditText getTitle() {
        return title;
    }

    /**
     * Set the title
     * @param title: the title
     */
    public void setTitle(EditText title) {
        this.title = title;
    }

    /**
     * Get the author
     * @return the author
     */
    public EditText getAuthor() {
        return author;
    }

    /**
     * Set the author
     * @param author: the author
     */
    public void setAuthor(EditText author) {
        this.author = author;
    }

    /**
     * Get the description
     * @return the description
     */
    public EditText getDescription() {
        return description;
    }

    /**
     * Sets the description
     * @param description: the description
     */
    public void setDescription(EditText description) {
        this.description = description;
    }
}


