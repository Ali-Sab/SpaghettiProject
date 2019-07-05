package com.example.android.spaghettiproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setTitle(getIntent().getStringExtra(GroupsActivity.EXTRA_MESSAGE));

        mListsList.addLast("Sample 1");
        mListsList.addLast("test???");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new NextButtonListAdapter(this, mListsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
