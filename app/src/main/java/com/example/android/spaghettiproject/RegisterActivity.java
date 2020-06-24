package com.example.android.spaghettiproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import org.w3c.dom.Text;

import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity implements ServerActivity.AsyncResponse {

    private EditText mEmail;
    private EditText mPassword1;
    private EditText mPassword2;
    private EditText mPhoneNumber;
    private Button mRegisterButton;
    private TextView mLogin;
    private String password;
    private EditText mName;
    private ProgressBar mProgressBar;
    private boolean emailIsValid = false;
    private boolean nameIsValid = false;
    private boolean password1IsValid = false;
    private boolean password2IsValid = false;
    private boolean phoneIsValid = true;

    IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        mEmail = (EditText) findViewById(R.id.editTextEmail);
        mPassword1 = (EditText) findViewById(R.id.editTextPassword1);
        mPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        mLogin = (TextView) findViewById(R.id.register_textViewLogin);
        mName = (EditText) findViewById(R.id.editTextName);
        mPhoneNumber = (EditText) findViewById(R.id.editTextPhone);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRegisterButton = (Button) findViewById(R.id.register_btnSetup);

        String redPart = "* ";
        String greyPart = "Name";

        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString redColoredString = new SpannableString(redPart);
        redColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, redPart.length(), 0);
        builder.append(redColoredString);

        SpannableString greyColoredString = new SpannableString(greyPart);
        greyColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, greyPart.length(), 0);
        builder.append(greyColoredString);

        mName.setHint(builder);

        builder = new SpannableStringBuilder();
        greyPart = "Email";
        greyColoredString = new SpannableString(greyPart);
        greyColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, greyPart.length(), 0);
        builder.append(redColoredString);
        builder.append(greyColoredString);

        mEmail.setHint(builder);

        builder = new SpannableStringBuilder();
        greyPart = "Password";
        greyColoredString = new SpannableString(greyPart);
        greyColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, greyPart.length(), 0);
        builder.append(redColoredString);
        builder.append(greyColoredString);

        mPassword1.setHint(builder);

        builder = new SpannableStringBuilder();
        greyPart = "Confirm Password";
        greyColoredString = new SpannableString(greyPart);
        greyColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, greyPart.length(), 0);
        builder.append(redColoredString);
        builder.append(greyColoredString);

        mPassword2.setHint(builder);


        Intent intent = getIntent();
        mEmail.setText(intent.getStringExtra("email"));

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("email", mEmail.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailIsValid)
                    Toast.makeText(RegisterActivity.this, "Email is invalid", Toast.LENGTH_LONG).show();
                else if (!nameIsValid)
                    Toast.makeText(RegisterActivity.this, "Name must be longer then 2 characters", Toast.LENGTH_LONG).show();
                else if (!password1IsValid)
                    Toast.makeText(RegisterActivity.this, "Password must be longer than 5 characters", Toast.LENGTH_LONG).show();
                else if (!password2IsValid)
                    Toast.makeText(RegisterActivity.this, "Passwords must match", Toast.LENGTH_LONG).show();
                else if (!phoneIsValid)
                    Toast.makeText(RegisterActivity.this, "Phone number is invalid", Toast.LENGTH_LONG).show();
                else
                    new ServerActivity(RegisterActivity.this, mEmail.getText().toString(), mName.getText().toString(), mPassword1.getText().toString(), mProgressBar).execute(mName.getText().toString());
            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailIsValid = android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
                if (emailIsValid && nameIsValid && password1IsValid && password2IsValid && phoneIsValid) {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mRegisterButton.setTextColor(Color.WHITE);
                } else {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mRegisterButton.setTextColor(Color.BLACK);
                }

                if (emailIsValid)
                    mEmail.setTextColor(Color.BLACK);
                else
                    mEmail.setTextColor(Color.parseColor("#FF353A"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameIsValid = s.length() > 2;
                if (emailIsValid && nameIsValid && password1IsValid && password2IsValid && phoneIsValid) {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mRegisterButton.setTextColor(Color.WHITE);
                } else {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mRegisterButton.setTextColor(Color.BLACK);
                }

                if (nameIsValid)
                    mName.setTextColor(Color.BLACK);
                else
                    mName.setTextColor(Color.parseColor("#FF353A"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneIsValid = android.util.Patterns.PHONE.matcher(s).matches() || s.length() == 0;
                if (emailIsValid && nameIsValid && password1IsValid && password2IsValid && phoneIsValid) {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mRegisterButton.setTextColor(Color.WHITE);
                } else {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mRegisterButton.setTextColor(Color.BLACK);
                }

                if (phoneIsValid)
                    mPhoneNumber.setTextColor(Color.BLACK);
                else
                    mPhoneNumber.setTextColor(Color.parseColor("#FF353A"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password1IsValid = s.length() > 5;
                if (emailIsValid && nameIsValid && password1IsValid && password2IsValid && phoneIsValid) {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mRegisterButton.setTextColor(Color.WHITE);
                } else {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mRegisterButton.setTextColor(Color.BLACK);
                }

                if (password1IsValid)
                    mPassword1.setTextColor(Color.BLACK);
                else
                    mPassword1.setTextColor(Color.parseColor("#FF353A"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password2IsValid = s.toString().equals(mPassword1.getText().toString());
                if (emailIsValid && nameIsValid && password1IsValid && password2IsValid && phoneIsValid) {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mRegisterButton.setTextColor(Color.WHITE);
                } else {
                    mRegisterButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mRegisterButton.setTextColor(Color.BLACK);
                }

                if (password2IsValid)
                    mPassword2.setTextColor(Color.BLACK);
                else
                    mPassword2.setTextColor(Color.parseColor("#FF353A"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("email", mEmail.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
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
