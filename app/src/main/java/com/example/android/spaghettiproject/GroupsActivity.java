package com.example.android.spaghettiproject;

import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.LinkedList;

public class GroupsActivity extends AppCompatActivity implements ServerActivity.AsyncResponse {

    public static final String EXTRA_MESSAGE = "com.example.android.spaghettiproject.extra.MESSAGE";
    private static Button button;
    private final LinkedList<String> mGroupList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private SharedPreferences mPreferences;
    private final String sharedPrepFile = "com.example.android.spaghettiproject";
    private ProgressBar progressBar;
    private JSONArray dataArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        mPreferences = getSharedPreferences(sharedPrepFile, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new RecyclerAdapter(this, mGroupList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //new ServerActivity(GroupsActivity.this, getIntent().getStringExtra("email"), progressBar).execute();


        FloatingActionButton fabAdd = findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupsActivity.this);
                builder.setTitle("Add Group");

                EditText input = new EditText(GroupsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if(m_Text.length() > 0) {
                            //mGroupList.add(m_Text);
                            //mAdapter.notifyDataSetChanged();
                            //new ServerActivity(GroupsActivity.this, getIntent().getStringExtra("email"), m_Text, progressBar).execute();

                        }else{
                            dialog.cancel();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        mGroupList.addLast("Sample 1");
        mGroupList.addLast("test???");


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new RecyclerAdapter(this, mGroupList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       // ItemTouchHelper helper = new ItemTouchHelper(new My ItemTouchCallback(mAdapter));
        //helper.attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String autoLogin = intent.getStringExtra("autoLogin");
        if (autoLogin != null && autoLogin.equals("true")) {
            Toast.makeText(GroupsActivity.this, "Logged in as " + email, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                Intent loginIntent = new Intent(GroupsActivity.this,
                        LoginActivity.class);
                Toast.makeText(this, "Successfully logged out", Toast.LENGTH_LONG).show();
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SharedPreferences.Editor preferenceEditor = mPreferences.edit();
                preferenceEditor.clear();
                preferenceEditor.apply();
                startActivity(loginIntent);
                finish();
                return true;
            case R.id.action_settings:
//                Intent settingsIntent = new Intent(this,
//                        SettingsActivity.class);
//                startActivity(settingsIntent);
                Toast.makeText(this, "Make a settings activity", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_contact:
                Intent contactIntent = new Intent(GroupsActivity.this, ContactUsActivity.class);
                startActivity(contactIntent);
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    public void processFinish(JSONObject response) {
        try {
            switch (response.getString("statusMessage")) {
                case "success":
                    switch (response.getString("successMessage")) {
                        case "Created group successfully":
                            Toast.makeText(this, "added group", Toast.LENGTH_SHORT).show();
                            break;
                        case "Deleted group successfully":
                            Toast.makeText(this, "deleted group", Toast.LENGTH_SHORT).show();
                            break;
                        case "Successfully fetched groups":
                            //Get groups
                            dataArray = response.getJSONArray("data");
                            mGroupList.clear();
                            for(int i = 0; i < dataArray.length(); i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                mGroupList.add(data.getString("name"));
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                    }
                    break;
                case "error":
                    switch (response.getString("errorMessage")) {
                        case "Email does not exist":
                            new AlertDialog.Builder(GroupsActivity.this)
                                    .setTitle("Login Error")
                                    .setMessage("Email does not exist.")
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
