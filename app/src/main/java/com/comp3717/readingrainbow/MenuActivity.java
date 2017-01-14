package com.comp3717.readingrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void searchBook(View view) {
        Intent intent = new Intent(this, SearchBookActivity.class);
        startActivity(intent);
    }

    public void shareBook(View view) {
        Intent intent = new Intent(this, ShareBookSearchActivity.class);
        startActivity(intent);
    }

    public void seeBooksMadeAvailable(View view) {
        Intent intent = new Intent(this, BooksMadeAvailableToBorrowActivity.class);
        startActivity(intent);
    }

    public void seeBooksLentOut(View view) {
        Intent intent = new Intent(this, BooksLentOutActivity.class);
        startActivity(intent);
    }
}
