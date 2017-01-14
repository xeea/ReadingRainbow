package com.comp3717.readingrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void login(View view) {

        EditText username = (EditText) findViewById(R.id.login_username);
        EditText password = (EditText) findViewById(R.id.login_password);

        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();

        if (usernameStr.trim().equals("") || passwordStr.trim().equals("")) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing In...");
            progressDialog.show();

           Ion.with(this)
                   .load("https://api.mlab.com/api/1/databases/comp3717test/collections/users?q=%7B%22username%22%20%3A%20%22" + usernameStr + "%22%2C%20%22password%22%20%3A%20%22" + passwordStr + "%22%7D&apiKey=fDi3fzZiSwqI71W92Ghk4ULIvXBzQEgM")
                   .progressDialog(progressDialog)
                   .asJsonArray().setCallback(new FutureCallback<JsonArray>() {
               @Override
               public void onCompleted(Exception e, JsonArray result) {
                   progressDialog.dismiss();
                   if (result.size() == 0) {
                       Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_LONG).show();
                   } else {
                       JsonObject userObj = (JsonObject) result.get(0);

                       SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("readingrainbowprefs", Context.MODE_PRIVATE);
                       SharedPreferences.Editor editor = sharedPref.edit();
                       editor.putString("loggedInEmail", userObj.get("email").getAsString());
                       editor.commit();

                       Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                       startActivity(intent);
                   }
               }
           });

        }

    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
