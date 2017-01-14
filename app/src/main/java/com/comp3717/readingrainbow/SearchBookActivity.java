package com.comp3717.readingrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.comp3717.readingrainbow.model.Book;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
    }

    public void searchByTitle(View view) {
        EditText title = (EditText) findViewById(R.id.searchBookTitleInput);
        String titleString = title.getText().toString();
        if (titleString.equals("")) {
            Toast.makeText(this, "Enter a title", Toast.LENGTH_LONG).show();
        } else {
            try {
                search(URLEncoder.encode("{title:{$regex:\"" + titleString + "\",$options:\"i\"}, borrowed: false}", "UTF-8"));
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
                search(URLEncoder.encode("{author:{$regex:\"" + authorString + "\",$options:\"i\"}, borrowed: false}", "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
        }
    }

    public void search(String query) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        Ion.with(this)
                .load("https://api.mlab.com/api/1/databases/comp3717test/collections/books?q=" + query + "&apiKey=fDi3fzZiSwqI71W92Ghk4ULIvXBzQEgM")
                .progressDialog(progressDialog)
                .asJsonArray().setCallback(new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray result) {
                progressDialog.dismiss();
                if (result.size() == 0) {
                    Toast.makeText(SearchBookActivity.this, "Book not found.", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<Book> resultList = new ArrayList<Book>();

                    for (JsonElement book : result) {

                        String title = book.getAsJsonObject().get("title").getAsString();
                        String author = book.getAsJsonObject().get("author").getAsString();
                        String year = book.getAsJsonObject().get("year").getAsString();
                        String owner = book.getAsJsonObject().get("owner").getAsString();

                        Book newBook = new Book(title, author, year, owner);
                        resultList.add(newBook);
                    }
                    Intent intent = new Intent(SearchBookActivity.this, SearchBookListActivity.class);
                    intent.putParcelableArrayListExtra("bookList", resultList);
                    startActivity(intent);
                }

            }
        });
    }
}
