package com.example.android.spaghettiproject;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.LinkedList;

public class GroupsActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.android.spaghettiproject.extra.MESSAGE";
    private static Button button;
    private final LinkedList<String> mGroupList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private SharedPreferences mPreferences;
    private final String sharedPrepFile = "com.example.android.spaghettiproject";

    GlobalActivity global = (GlobalActivity)getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        mPreferences = getSharedPreferences(sharedPrepFile, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fabAdd = findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,
//                        OrderActivity.class);
//                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
//                startActivity(intent);
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
}
