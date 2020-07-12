package com.example.android.spaghettiproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ListsActivity extends AppCompatActivity implements  ServerActivity.AsyncResponse {

    private final LinkedList<String> mListsList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar progressBar;

    GlobalActivity global = (GlobalActivity)getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(GroupsActivity.EXTRA_MESSAGE));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new RecyclerAdapter(this, mListsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String sessionId = getIntent().getStringExtra(GroupsActivity.EXTRA_MESSAGE); //group item
        //new ServerActivity(ListsActivity.this, getIntent().getStringExtra("email"), sessionId, progressBar).execute();

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
                Intent intent = new Intent(ListsActivity.this,
                        LoginActivity.class);
                Toast.makeText(this,"Successfully logged out", Toast.LENGTH_LONG).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_settings:
//                Intent settingsIntent = new Intent(this,
//                        SettingsActivity.class);
//                startActivity(settingsIntent);
                Toast.makeText(this, "Make a settings activity", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_contact:
                Toast.makeText(this, "Make a contact us page", Toast.LENGTH_SHORT).show();
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

                        case "Successfully fetched lists":
                            //Get groups
                            JSONArray dataArray = response.getJSONArray("data");
                            mListsList.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                mListsList.add(data.getString("listName"));
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                    }
                    break;
                case "error":
                    switch (response.getString("errorMessage")) {
                        case "Email does not exist":
                            new AlertDialog.Builder(ListsActivity.this)
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
