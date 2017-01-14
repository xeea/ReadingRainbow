package com.comp3717.readingrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3717.readingrainbow.model.Book;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BooksMadeAvailableToBorrowActivity extends AppCompatActivity {

    final ArrayList<Book> resultList = new ArrayList<Book>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_made_available_to_borrow);

        SharedPreferences sharedPref = this.getSharedPreferences("readingrainbowprefs", Context.MODE_PRIVATE);
        String owner = sharedPref.getString("loggedInEmail", "ERROR");

        String query = "{\"owner\":\"" + owner + "\", borrowed: false}";
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


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
                    Toast.makeText(BooksMadeAvailableToBorrowActivity.this, "No Books.", Toast.LENGTH_LONG).show();
                } else {

                    for (JsonElement book : result) {

                        String title = book.getAsJsonObject().get("title").getAsString();
                        String author = book.getAsJsonObject().get("author").getAsString();
                        String year = book.getAsJsonObject().get("year").getAsString();
                        String owner = book.getAsJsonObject().get("owner").getAsString();

                        Book newBook = new Book(title, author, year, owner);
                        resultList.add(newBook);
                    }

                    setListAdapter(resultList);
                }

            }
        });




    }

    public void setListAdapter(ArrayList<Book> booklist) {
        ListView listView = (ListView) findViewById(R.id.listView2);

        ArrayAdapter<Book> bookAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, booklist);
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog(parent, view, position);

            }
        });


    }


    private void openDialog(final AdapterView<?> parent, final View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BooksMadeAvailableToBorrowActivity.this);
        builder.setMessage("Book")
                .setTitle("Book");
        final Book boook = (Book) parent.getItemAtPosition(position);


        builder.setPositiveButton("Set book as borrowed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String query = "{\"title\":\"" + boook.getTitle() + "\", \"author\":\"" + boook.getAuthors()[0] + "\", \"year\":" + boook.getYears()[0] + ", \"owner\":\"" + boook.getOwner() + "\", \"borrowed\":false}";
                try {
                    query = URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JsonObject updateObj = (new JsonParser()).parse("{\"$set\" : {\"borrowed\" : true}}").getAsJsonObject();
                final ProgressDialog progressDialog = new ProgressDialog(BooksMadeAvailableToBorrowActivity.this);
                progressDialog.setMessage("Setting book as borrowed...");
                progressDialog.show();
                Ion.with(BooksMadeAvailableToBorrowActivity.this)
                        .load("PUT", "https://api.mlab.com/api/1/databases/comp3717test/collections/books?q=" + query + "&apiKey=fDi3fzZiSwqI71W92Ghk4ULIvXBzQEgM")
                        .setJsonObjectBody(updateObj)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialog.dismiss();
                    }
                });






            }
        });

        builder.setNeutralButton("Remove book from listing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String query = "{\"title\":\"" + boook.getTitle() + "\", \"author\":\"" + boook.getAuthors()[0] + "\", \"year\":" + boook.getYears()[0] + ", \"owner\":\"" + boook.getOwner() + "\", \"borrowed\":false}";
                try {
                    query = URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JsonArray updateObj = (new JsonParser()).parse("[]").getAsJsonArray();
                final ProgressDialog progressDialog = new ProgressDialog(BooksMadeAvailableToBorrowActivity.this);
                progressDialog.setMessage("Removing book from listing...");
                progressDialog.show();

                Ion.with(BooksMadeAvailableToBorrowActivity.this)
                        .load("PUT", "https://api.mlab.com/api/1/databases/comp3717test/collections/books?q=" + query + "&apiKey=fDi3fzZiSwqI71W92Ghk4ULIvXBzQEgM")
                        .setJsonArrayBody(updateObj)
                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialog.dismiss();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
