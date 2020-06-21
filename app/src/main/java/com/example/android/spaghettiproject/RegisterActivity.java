package com.example.android.spaghettiproject;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity implements ServerActivity.AsyncResponse {

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
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

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
                finish();
            }
        });

        //Registering once button pressed
        setup = (Button) findViewById(R.id.btnSetup);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass1.getText().toString().equals(pass2.getText().toString())) {
                    password = pass1.getText().toString();

                    if (TextUtils.isEmpty(email.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(name.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Run below only if user account and password matches
                    new ServerActivity(RegisterActivity.this, email.getText().toString(), name.getText().toString(), pass1.getText().toString(), progressBar).execute(name.getText().toString());

                }
                else
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void processFinish(String output) {
        try {
            JSONObject response = new JSONObject(output);
            switch (response.getString("statusMessage")) {
                case "success":
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Success!")
                            .setMessage("Please login with your new account")
                            .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
                case "error":
                    switch (response.getString("errorMessage")) {
                        case "Email already exists":
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Registration Error")
                                    .setMessage("Email already exists")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case "Email already waiting for confirmation":
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Registration Error")
                                    .setMessage("Email already awaiting confirmation. If this wasn't you, please wait 24 hours and try again.")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case "Failed to insert account data into register confirmation database":
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Registration Error")
                                    .setMessage("Technical error occurred. Please contact us for assistance")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case "Missing password":
                            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
                            break;
                        case "Confirmation email failed to send":
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Registration Error")
                                    .setMessage("Confirmation email failed to send. Please try again after 24 hours.")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
