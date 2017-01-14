package com.comp3717.readingrainbow;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp3717.readingrainbow.model.Book;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchBookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book_list);

        Intent intent = getIntent();
        ArrayList<Book> bookList = intent.getParcelableArrayListExtra("bookList");

        ListView listView = (ListView) findViewById(R.id.searchBookList);

        ArrayAdapter<Book> bookAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, bookList);
        listView.setAdapter(bookAdapter);

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {book.getOwner()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Reading Rainbow] Hi, I am interested in borrowing: " + book.getTitle());
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi fellow Reading Rainbow user, I am interested in borrowing:\n\n" + book.toString());
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            }
        });
    }
}
