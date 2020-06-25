package com.example.android.spaghettiproject;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.spaghettiproject.Retrofit.IMyService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

//import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements ServerActivity.AsyncResponse {
    private EditText mEmail;
    private TextInputEditText mPassword;
    private Button mLoginButton;
    private TextView mGoToRegister;
    private ProgressBar mProgressBar;
    private boolean emailIsValid = false;
    private boolean passwordIsValid = false;


    IMyService iMyService;

    //Animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup for checkmark animation upon successful login
        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.checkmark640000);
        i.setContentDescription(getResources().getString(R.string.login));
        i.setAdjustViewBounds(true);
        i.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        //Initialize Service
        //Retrofit  = RetrofitClient.getInstance();
        //iMyService = retrofitClient.create(IMyService.class);

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmail = (EditText) findViewById(R.id.editTextEmail);
        mPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        mLoginButton = (Button) findViewById(R.id.btnLogin);
        mGoToRegister = (TextView) findViewById(R.id.textViewProfile);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        String colorPart = "Register here";
        String greyPart = "Don't have an account? ";

        SpannableString greyColoredString = new SpannableString(greyPart);
        SpannableString coloredString = new SpannableString(colorPart);
        greyColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, greyPart.length(), 0);
        coloredString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, colorPart.length(), 0);
        builder.append(greyColoredString);
        builder.append(coloredString);

        mGoToRegister.setText(builder);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailIsValid)
                    Toast.makeText(LoginActivity.this, R.string.register_email_invalid, Toast.LENGTH_LONG).show();
                else if (!passwordIsValid)
                    Toast.makeText(LoginActivity.this, R.string.register_password_invalid, Toast.LENGTH_LONG).show();
                else
                    new ServerActivity(LoginActivity.this, mEmail.getText().toString(), mPassword.getText().toString(), mProgressBar).execute();
            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailIsValid = android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
                if (emailIsValid && passwordIsValid) {
                    mLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mLoginButton.setTextColor(Color.WHITE);
                } else {
                    mLoginButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mLoginButton.setTextColor(Color.BLACK);
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

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordIsValid = s.length() > 5;
                if (emailIsValid && passwordIsValid) {
                    mLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mLoginButton.setTextColor(Color.WHITE);
                } else {
                    mLoginButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mLoginButton.setTextColor(Color.BLACK);
                }

                if (passwordIsValid)
                    mPassword.setTextColor(Color.BLACK);
                else
                    mPassword.setTextColor(Color.parseColor("#FF353A"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Intent intent = getIntent();
        mEmail.setText(intent.getStringExtra("email"));  //Null case is checked by Android SDK for .setText()
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivityForResult(intent, AppCodes.ACTIVITY_FINISH_RESULT);
        finish();
    }

    @Override
    //Once user selects the options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    //Once an options menu is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                Intent contactIntent = new Intent(LoginActivity.this, ContactUsActivity.class);
                startActivity(contactIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToProfile(View view) {
        Intent profileIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        profileIntent.putExtra("email", mEmail.getText().toString());
        startActivityForResult(profileIntent, AppCodes.ACTIVITY_FINISH_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppCodes.ACTIVITY_FINISH_RESULT) {
            mEmail.setText(data.getStringExtra("email"));
        }
    }

    public void keepLoggedIn(View view) {
    }

    @Override
    public void processFinish(String output) {
        try {
            JSONObject response = new JSONObject(output);
            switch (response.getString("statusMessage")) {
                case "success":
                    Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Success!")
                            .setMessage("You're now logged in")
                            .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() { //can probably change to .setNeutralButton
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("email", mEmail.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setIcon(getResources().getDrawable(R.drawable.ic_checkmark))
                            .show();
                    break;
                case "error":
                    switch (response.getString("errorMessage")) {
                        case "Email does not exist":
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Login Error")
                                    .setMessage("Email does not exist.")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case "Wrong password":
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Login Error")
                                    .setMessage("Password is incorrect.")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case "Missing password":
                            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                            break;
                        case "Request failed to send":
                            Toast.makeText(LoginActivity.this, "Technical error, please try again", Toast.LENGTH_LONG).show();
                            break;
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

