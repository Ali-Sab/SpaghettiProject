package com.example.android.spaghettiproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.spaghettiproject.Retrofit.IMyService;
import com.example.android.spaghettiproject.Retrofit.RetrofitClient;

import org.w3c.dom.Text;

import java.util.function.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass1;
    private EditText pass2;
    private Button setup;
    private TextView login;
    private String password;
    private EditText name;
    private ProgressBar progressBar;

    IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        email = (EditText) findViewById(R.id.editTextEmail);
        pass1 = (EditText) findViewById(R.id.editTextPassword1);
        pass2 = (EditText) findViewById(R.id.editTextPassword2);
        login = (TextView) findViewById(R.id.textViewLogin);
        name = (EditText) findViewById(R.id.editTextName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        setup = (Button) findViewById(R.id.btnSetup);

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass1.getText().toString().equals(pass2.getText().toString())) {
                    password = pass1.getText().toString();

                    if (TextUtils.isEmpty(email.getText().toString())) {
                        Toast.makeText(ProfileActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(ProfileActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(name.getText().toString())) {
                        Toast.makeText(ProfileActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Run below only if user account and password matches
                    new ServerActivity(ProfileActivity.this, email.getText().toString(), name.getText().toString(), pass1.getText().toString(), progressBar).execute(name.getText().toString());

                }
            }
        });
    }


    public void onClick(View view) {
    }
}
