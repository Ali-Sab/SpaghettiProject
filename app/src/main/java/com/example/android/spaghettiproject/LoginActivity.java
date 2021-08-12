package com.example.android.spaghettiproject;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.spaghettiproject.Retrofit.IMyService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.Executor;

import static com.example.android.spaghettiproject.AppCodes.ACTIVITY_FINISH_RESULT;

//import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements ServerActivity.AsyncResponse {
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mLoginButton;
    private TextView mGoToRegister;
    private ProgressBar mProgressBar;
    private CheckBox mKeepLoggedIn;
    private Button mbiometricLoginButton;
    private boolean emailIsValid = false;
    private boolean passwordIsValid = false;
    private boolean keepLoggedIn = false;
    private boolean biometricEnabled = false;
    private boolean attempedAutoLogin = false;
    private SharedPreferences mPreferences;
    private final String sharedPrepFile = "com.example.android.spaghettiproject";

    // Biometric authentication
    private Executor executor;
    private androidx.biometric.BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;


    GlobalActivity global = (GlobalActivity)getApplication(); //creates var ga to set and get email

    IMyService iMyService;

    //Animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return;
        }

        //Setup for checkmark animation upon successful login
        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.checkmark640000);
        i.setContentDescription(getResources().getString(R.string.login));
        i.setAdjustViewBounds(true);
        i.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        mPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        mLoginButton = (Button) findViewById(R.id.btnLogin);
        mGoToRegister = (TextView) findViewById(R.id.textViewProfile);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mKeepLoggedIn = (CheckBox) findViewById(R.id.checkBoxLogin);
        mbiometricLoginButton = findViewById(R.id.biometric_login);

        mPreferences = getSharedPreferences(sharedPrepFile, MODE_PRIVATE);
        if (mPreferences != null) {
            keepLoggedIn = mPreferences.getBoolean("keepLoggedIn", false);
            biometricEnabled = mPreferences.getBoolean("useBiometric", false);

            if (biometricEnabled)
                mbiometricLoginButton.setVisibility(View.VISIBLE);
            else
                mbiometricLoginButton.setVisibility(View.INVISIBLE);

            if (keepLoggedIn) {
                String email = mPreferences.getString("savedEmail", "");
                String password = mPreferences.getString("savedPassword", "");

                if (email.isEmpty())
                    Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
                else if (password.isEmpty())
                    Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    attempedAutoLogin = true;
                    try {
                        JSONObject requestBody = new JSONObject();
                        requestBody.put("email", email);
                        requestBody.put("password", password);
                        new ServerActivity(LoginActivity.this, AppCodes.login).execute(requestBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        if (biometricEnabled && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new androidx.biometric.BiometricPrompt(LoginActivity.this, executor,
                    new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(
                        @NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    String email = mPreferences.getString("savedEmail", "");
                    String password = mPreferences.getString("savedPassword", "");

                    if (email.isEmpty())
                        Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
                    else if (password.isEmpty())
                        Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
                    else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        attempedAutoLogin = true;
                        try {
                            JSONObject requestBody = new JSONObject();
                            requestBody.put("email", email);
                            requestBody.put("password", password);
                            new ServerActivity(LoginActivity.this, AppCodes.login).execute(requestBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });

            promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build();


            mbiometricLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BiometricManager biometricManager = BiometricManager.from(LoginActivity.this);
                    switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                        case BiometricManager.BIOMETRIC_SUCCESS:
                            biometricPrompt.authenticate(promptInfo);
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                            Toast.makeText(LoginActivity.this, "No biometric features available on this device.", Toast.LENGTH_LONG).show();
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                            Toast.makeText(LoginActivity.this, "Biometric features are currently unavailable.", Toast.LENGTH_LONG).show();
                            break;
                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                            // Prompts the user to create credentials that your app accepts.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                        BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                                startActivityForResult(enrollIntent, ACTIVITY_FINISH_RESULT);
                            } else {
                                Toast.makeText(LoginActivity.this, "No fingerprints enrolled. Please enroll a fingerprint in the Settings of your phone.", Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }
            });
        }

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
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    try {
                        JSONObject requestBody = new JSONObject();
                        requestBody.put("email", Objects.requireNonNull(mEmail.getText()).toString());
                        requestBody.put("password", Objects.requireNonNull(mPassword.getText()).toString());
                        new ServerActivity(LoginActivity.this, AppCodes.login).execute(requestBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Technical error, please try again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = (CharSequence) s.toString().trim();
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
                String trimmedString = s.toString().trim();
                if (!trimmedString.equals(s.toString())) {
                    mEmail.setText(trimmedString);
                    mEmail.setSelection(trimmedString.length());
                }
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

        mKeepLoggedIn.setChecked(keepLoggedIn);
        mKeepLoggedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepLoggedIn = mKeepLoggedIn.isChecked();
                if (!keepLoggedIn) {
                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                    preferencesEditor.putBoolean("keepLoggedIn", keepLoggedIn);
                    preferencesEditor.apply();
                }
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

    @Override
    public void processFinish(JSONObject response) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (response != null) {
            try {
                switch (response.getString("statusMessage")) {
                    case "success": {
                        if (!attempedAutoLogin) {
                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            preferencesEditor.putBoolean("keepLoggedIn", keepLoggedIn);
                            if (keepLoggedIn) {
                                preferencesEditor.putString("savedEmail", Objects.requireNonNull(mEmail.getText()).toString());
                                preferencesEditor.putString("savedPassword", Objects.requireNonNull(mPassword.getText()).toString());
                            }
                            preferencesEditor.apply();

                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Success!")
                                    .setMessage("You're now logged in")
                                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() { //can probably change to .setNeutralButton
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("email", Objects.requireNonNull(mEmail.getText()).toString());
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setIcon(getResources().getDrawable(R.drawable.ic_checkmark))
                                    .show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("email", mPreferences.getString("savedEmail", ""));
                            intent.putExtra("autoLogin", "true");
                            startActivity(intent);
                            finish();
                        }

                        break;
                    }
                    case "error": {
                        if (attempedAutoLogin && !biometricEnabled)
                            Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
                        switch (response.getString("errorMessage")) {
                            case "Email does not exist":
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("Login Error")
                                        .setMessage("Email does not exist.")
                                        .setNegativeButton(android.R.string.ok, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;
                            case "Wrong password": {
                                String message = (attempedAutoLogin) ? "Your password has changed. Please login again." : "Password is incorrect.";
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("Login Error")
                                        .setMessage(message)
                                        .setNegativeButton(android.R.string.ok, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;
                            }
                            case "Missing password":
                                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                                break;
                            case "Request failed to send":
                                Toast.makeText(LoginActivity.this, "Technical error occurred, please try again", Toast.LENGTH_LONG).show();
                                break;
                        }
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            if (attempedAutoLogin && !biometricEnabled)
                Toast.makeText(LoginActivity.this, "Failed to login automatically. Please try logging in manually.", Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Login Error")
                    .setMessage("Technical error occurred, please try again")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        attempedAutoLogin = false;
    }
}

