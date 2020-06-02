package com.example.android.spaghettiproject;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

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


//swipe left to delete an item
public class MyItemTouchCallback extends ItemTouchHelper.Callback {
    private final NextButtonListAdapter adapter;

    public MyItemTouchCallback(NextButtonListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        return makeMovementFlags(ItemTouchHelper.DOWN |ItemTouchHelper.RIGHT|ItemTouchHelper.UP|ItemTouchHelper.LEFT,
                ItemTouchHelper.START |ItemTouchHelper.END);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction){
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.END || direction == ItemTouchHelper.START){
            return;
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int startPosition = viewHolder.getAdapterPosition();
        int endPosition = target.getAdapterPosition();
        //the item to swap
        int index = startPosition;

        //drag direction
        int dir = startPosition - endPosition > 0 ? -1 : 1;
        while (index < endPosition) {
            Collections.swap(adapter.getmGroupLists(), index, index + dir);
            return true;
        }
        recyclerView.getAdapter().notifyItemChanged(startPosition, endPosition);
        return true;
    }
}