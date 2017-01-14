package com.comp3717.readingrainbow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp3717.readingrainbow.model.Book;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareBookSearchListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_book_search_list);

        Intent intent = getIntent();
        ArrayList<Book> bookList = intent.getParcelableArrayListExtra("bookList");

        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<Book> bookAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, bookList);
        listView.setAdapter(bookAdapter);

        final Intent shareBookActivityIntent = new Intent(this, ShareBookActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);
                shareBookActivityIntent.putExtra("book", (Serializable) book);
                startActivity(shareBookActivityIntent);
            }
        });
    }

    public void startShareBookActivity(View view) {
        Intent intent = new Intent(this, ShareBookActivity.class);
        startActivity(intent);
    }
}
