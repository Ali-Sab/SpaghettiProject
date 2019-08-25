package com.example.android.spaghettiproject;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;

public class ListsActivity extends AppCompatActivity {

    private final LinkedList<String> mListsList = new LinkedList<>();

    private RecyclerView mRecyclerView;
    private NextButtonListAdapter mAdapter;

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

        mListsList.addLast("afag");
        mListsList.addLast("njll;,");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new NextButtonListAdapter(this, mListsList);
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
}
