package com.comp3717.readingrainbow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view) {
        EditText username = (EditText) findViewById(R.id.login_username);
        EditText email = (EditText) findViewById(R.id.register_email);
        EditText password = (EditText) findViewById(R.id.register_password1);

        String usernameStr = username.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        if (usernameStr.trim().equals("") || emailStr.trim().equals("") || passwordStr.trim().equals("")) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        } else {
            JsonObject userObj = new JsonObject();
            userObj.addProperty("username", usernameStr);
            userObj.addProperty("email", emailStr);
            userObj.addProperty("password", passwordStr);

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering...");
            progressDialog.show();

            Ion.with(this)
                    .load("POST", "https://api.mlab.com/api/1/databases/comp3717test/collections/users?apiKey=fDi3fzZiSwqI71W92Ghk4ULIvXBzQEgM")
                    .progressDialog(progressDialog)
                    .setJsonObjectBody(userObj)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            if (!result.has("message")) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }
}
