package com.example.android.spaghettiproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass1;
    private EditText pass2;
    private Button setup;
    private TextView login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = (EditText)findViewById(R.id.editTextEmail);
        pass1 = (EditText)findViewById(R.id.editTextPassword1);
        pass2 = (EditText)findViewById(R.id.editTextPassword2);
        login = (TextView)findViewById(R.id.textViewLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        setup = (Button)findViewById(R.id.btnSetup);

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass1.getText().toString().equals(pass2.getText().toString())){
                    //isValidEmail(email.getText().toString());
                    //Send user email to verify
                    //Can add usernames later
                    password = pass1.getText().toString();
                }
            }
        });
    }


}
