package com.example.android.spaghettiproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity implements ServerActivity.AsyncResponse {

    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;
    private Button mSendEmailButton;
    private Button mCancelButton;
    private ProgressBar mProgressBar;
    private boolean emailIsValid = false;
    private boolean passwordIsValid = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup for checkmark animation upon successful login
        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.checkmark640000);
        i.setContentDescription(getResources().getString(R.string.login));
        i.setAdjustViewBounds(true);
        i.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mEmailEditText = (TextInputEditText) findViewById(R.id.editTextEmail);
        mPasswordEditText = (TextInputEditText) findViewById(R.id.editTextPassword);
        mSendEmailButton = (Button) findViewById(R.id.btnChangePassword);
        mCancelButton = (Button) findViewById(R.id.btnCancel);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        mEmailEditText.setText(intent.getStringExtra("email"));
        emailIsValid = android.util.Patterns.EMAIL_ADDRESS.matcher(((mEmailEditText.getText() != null) ? mEmailEditText.getText() : "").toString()).matches();
        if (emailIsValid)
            mEmailEditText.setTextColor(Color.BLACK);
        else
            mEmailEditText.setTextColor(Color.parseColor("#FF353A"));

        mSendEmailButton.setOnClickListener(v -> {
            if (!emailIsValid)
                Toast.makeText(ForgotPasswordActivity.this, R.string.register_email_invalid, Toast.LENGTH_LONG).show();
            else if (!passwordIsValid)
                Toast.makeText(ForgotPasswordActivity.this, R.string.register_password_invalid, Toast.LENGTH_LONG).show();
            else {
                mProgressBar.setVisibility(View.VISIBLE);
                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("email", Objects.requireNonNull(mEmailEditText.getText()).toString());
                    requestBody.put("newPassword", Objects.requireNonNull(mPasswordEditText.getText()).toString());
                    new ServerActivity(ForgotPasswordActivity.this, AppCodes.forgotPassword).execute(requestBody);
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ForgotPasswordActivity.this, "Technical error, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        mCancelButton.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("email", ((mEmailEditText.getText() != null) ? mEmailEditText.getText() : "").toString());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = (CharSequence) s.toString().trim();
                emailIsValid = android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
                if (emailIsValid && passwordIsValid) {
                    mSendEmailButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mSendEmailButton.setTextColor(Color.WHITE);
                } else {
                    mSendEmailButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mSendEmailButton.setTextColor(Color.BLACK);
                }

                if (emailIsValid)
                    mEmailEditText.setTextColor(Color.BLACK);
                else
                    mEmailEditText.setTextColor(Color.parseColor("#FF353A"));

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trimmedString = s.toString().trim();
                if (!trimmedString.equals(s.toString())) {
                    mEmailEditText.setText(trimmedString);
                    mEmailEditText.setSelection(trimmedString.length());
                }
            }
        });

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordIsValid = s.length() > 5;
                if (emailIsValid && passwordIsValid) {
                    mSendEmailButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mSendEmailButton.setTextColor(Color.WHITE);
                } else {
                    mSendEmailButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
                    mSendEmailButton.setTextColor(Color.BLACK);
                }

                if (passwordIsValid)
                    mPasswordEditText.setTextColor(Color.BLACK);
                else
                    mPasswordEditText.setTextColor(Color.parseColor("#FF353A"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("email", ((mEmailEditText.getText() != null) ? mEmailEditText.getText() : "").toString());
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
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
                Intent contactIntent = new Intent(ForgotPasswordActivity.this, ContactUsActivity.class);
                startActivity(contactIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void processFinish(JSONObject response) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (response != null) {
            try {
                switch (response.getString("statusMessage")) {
                    case "success":
                        //can probably change to .setNeutralButton
                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setTitle("Success!")
                                .setMessage("Password change email has been sent successfully")
                                .setNegativeButton(android.R.string.ok, (dialogInterface, i) -> {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("email", ((mEmailEditText.getText() != null) ? mEmailEditText.getText() : "").toString());
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                })
                                .setIcon(getResources().getDrawable(R.drawable.ic_checkmark))
                                .show();
                        break;
                    case "error": {
                        switch (response.getString("errorMessage")) {
                            case "Account does not exist": {
                                new AlertDialog.Builder(ForgotPasswordActivity.this)
                                        .setTitle("Password Change Error")
                                        .setMessage("The specified email has no associated account.")
                                        .setNegativeButton(android.R.string.ok, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;
                            }
                            case "Password change email failed to send":
                                Toast.makeText(ForgotPasswordActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                                break;
                            default:    // Also covers case: "Could not update database entry for resetting password"
                                Toast.makeText(ForgotPasswordActivity.this, "Technical error occurred, please try again", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    default:
                        Toast.makeText(ForgotPasswordActivity.this, "Technical error occurred, please try again", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
