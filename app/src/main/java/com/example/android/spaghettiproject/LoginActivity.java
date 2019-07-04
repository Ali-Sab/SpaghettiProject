package com.example.android.spaghettiproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Button login;
    private TextView loginText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.editTextUser);
        password = (EditText)findViewById(R.id.editTextPassword);
        login = (Button) findViewById(R.id.btnLogin);
        loginText = (TextView)findViewById(R.id.textViewLogin);



    }

    public void onClick(View view) {
        //Run below only if user account and password matches
        Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent profileIntent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void keepLoggedIn(View view){

    }
}
