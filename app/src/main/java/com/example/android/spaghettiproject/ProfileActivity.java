package com.example.android.spaghettiproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass1;
    private EditText pass2;
    private Button setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = (EditText)findViewById(R.id.editTextEmail);
        pass1 = (EditText)findViewById(R.id.editTextPassword1);
        pass2 = (EditText)findViewById(R.id.editTextPassword2);
        setup = (Button)findViewById(R.id.btnSetup);

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass1.getText().toString().equals(pass2.getText().toString())){
                    //isValidEmail(email.getText().toString());
                }
            }
        });
    }


}
