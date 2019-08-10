package com.example.android.spaghettiproject;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.spaghettiproject.Retrofit.IMyService;
import com.example.android.spaghettiproject.Retrofit.RetrofitClient;

import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Button login;
    private TextView loginText;
    private ProgressBar progressBar;


    IMyService iMyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (EditText) findViewById(R.id.editTextUser);
        password = (EditText) findViewById(R.id.editTextPassword);
        login = (Button) findViewById(R.id.btnLogin);
        loginText = (TextView) findViewById(R.id.textViewLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                Toast.makeText(this, "Make a contact us page", Toast.LENGTH_SHORT).show();
                Intent contactIntent = new Intent(LoginActivity.this, ContactUsActivity.class);
                startActivity(contactIntent);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        //Run below only if user account and password matches
        new ServerActivity(LoginActivity.this, user.getText().toString(), password.getText().toString(), progressBar).execute();
    }

    public void goToProfile(View view) {
        Intent profileIntent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void keepLoggedIn(View view) {

    }
}

