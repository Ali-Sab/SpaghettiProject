package com.example.android.spaghettiproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.acl.Group;
import java.util.LinkedList;

public class GroupsActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.android.spaghettiproject.extra.MESSAGE";
    private static Button button;

    private final LinkedList<String> mGroupList = new LinkedList<>();

    private RecyclerView mRecyclerView;
    private NextButtonListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

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
        mAdapter = new NextButtonListAdapter(this, mGroupList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent = new Intent(GroupsActivity.this,
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
}
