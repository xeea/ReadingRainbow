package com.comp3717.readingrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3717.readingrainbow.model.Book;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class ShareBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_book);

        Intent intent = getIntent();

        if (intent.hasExtra("book")) {
            Book book = (Book) intent.getSerializableExtra("book");

            TextView title = (TextView) findViewById(R.id.title_field);
            title.setText(book.getTitle());

            TextView author = (TextView) findViewById(R.id.author_field);
            author.setText(book.getAuthorsAsString());

            TextView year = (TextView) findViewById(R.id.year_field);
            year.setText(book.getYears()[0]);
        }

    }

    public void shareBook(View view) {
        EditText title = (EditText) findViewById(R.id.title_field);
        EditText author = (EditText) findViewById(R.id.author_field);
        EditText year = (EditText) findViewById(R.id.year_field);

        String titleName = title.getText().toString();
        String authorName = author.getText().toString();
        int yearName = Integer.parseInt(year.getText().toString());
//
//        JSONObject bookObj = new JSONObject();
//        try {
//            bookObj.put("title", titleName);
//            bookObj.put("author", authorName);
//            bookObj.put("year", yearName);
//            Toast.makeText(this, bookObj.toString(), Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//
//        }

        SharedPreferences sharedPref = this.getSharedPreferences("readingrainbowprefs", Context.MODE_PRIVATE);
        String owner = sharedPref.getString("loggedInEmail", "ERROR");

        JsonObject bookObj = new JsonObject();
        bookObj.addProperty("title", titleName);
        bookObj.addProperty("author", authorName);
        bookObj.addProperty("year", yearName);
        bookObj.addProperty("owner", owner);
        bookObj.addProperty("borrowed", false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");

        Ion.with(this)
                .load("POST", "https://api.mlab.com/api/1/databases/comp3717test/collections/books?apiKey=fDi3fzZiSwqI71W92Ghk4ULIvXBzQEgM")
                .progressDialog(progressDialog)
                .setJsonObjectBody(bookObj)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialog.dismiss();
                        if (!result.has("message")) {
                            Toast.makeText(ShareBookActivity.this, "Book shared successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ShareBookActivity.this, MenuActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ShareBookActivity.this, "Error adding book", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
