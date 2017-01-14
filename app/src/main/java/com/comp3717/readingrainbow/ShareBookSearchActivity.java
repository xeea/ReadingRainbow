package com.comp3717.readingrainbow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.comp3717.readingrainbow.model.Book;
import com.comp3717.readingrainbow.util.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ShareBookSearchActivity extends AppCompatActivity {
    private static final String URL = "http://openlibrary.org/search.json?";
    private static final String URL_TITLE = URL + "title=";
    private static final String URL_AUTHOR = URL + "author=";
    private static final String URL_ISBN = URL + "isbn=";

    JSONObject json;
    JSONArray docsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_book_search);
    }

    public void searchByTitle(View view) {
        EditText title = (EditText) findViewById(R.id.searchBookTitleInput);
        String titleString = title.getText().toString();
        if (titleString.equals("")) {
            Toast.makeText(this, "Enter a title", Toast.LENGTH_LONG).show();
        } else {
            try {
                search(URL_TITLE + URLEncoder.encode(titleString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
        }
    }

    public void searchByAuthor(View view) {
        EditText author = (EditText) findViewById(R.id.searchBookAuthorInput);
        String authorString = author.getText().toString();
        if (authorString.equals("")) {
            Toast.makeText(this, "Enter an author", Toast.LENGTH_LONG).show();
        } else {
            try {
                search(URL_AUTHOR + URLEncoder.encode(authorString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
        }
    }

    public void searchByISBN(View view) {
        EditText isbn = (EditText) findViewById(R.id.searchBookISBNInput);
        String isbnString = isbn.getText().toString();
        if (isbnString.equals("")) {
            Toast.makeText(this, "Enter an author", Toast.LENGTH_LONG).show();
        } else {
            try {
                search(URL_ISBN + URLEncoder.encode(isbnString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
        }
    }

    public void search(String url) {

        RequestQueue queue = Singleton.getInstance(ShareBookSearchActivity.this.getApplicationContext()).getRequestQueue();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        ArrayList<Book> resultList = new ArrayList<Book>();
                        try {
                            json = new JSONObject(response);

                            // Books are split into array called "docs"
                            docsJson = json.getJSONArray("docs");
                            int number = docsJson.length();

                            for (int i = 0; i < docsJson.length() && i <= 25; i++) {
                                JSONObject bookObj = docsJson.getJSONObject(i);

                                String title = bookObj.getString("title");

                                if (bookObj.has("subtitle")) {
                                    title += " - " + bookObj.getString("subtitle");
                                }

                                String[] authorsArray;

                                if (bookObj.has("author_name")) {
                                    JSONArray authors = bookObj.getJSONArray("author_name");
                                    authorsArray = new String[authors.length()];
                                    for (int j = 0; j < authors.length(); j++) {
                                        authorsArray[j] = authors.getString(j);
                                    }
                                } else {
                                    authorsArray = new String[1];
                                    authorsArray[0] = "Unknown Author";
                                }

                                String[] yearsArr;

                                if (bookObj.has("publish_year")) {
                                    JSONArray years = bookObj.getJSONArray("publish_year");
                                    yearsArr = new String[years.length()];
                                    int z = 0;
                                    for (int j = 0; j < years.length(); j++) {
                                        yearsArr[z++] = years.getInt(j) + "";
                                    }
                                } else {
                                    yearsArr = new String[1];
                                    yearsArr[0] = "";
                                }


                                resultList.add(new Book(title, authorsArray, yearsArr));
                            }
                            Intent intent = new Intent(ShareBookSearchActivity.this, ShareBookSearchListActivity.class);
                            intent.putParcelableArrayListExtra("bookList", resultList);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        Singleton.getInstance(ShareBookSearchActivity.this.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
