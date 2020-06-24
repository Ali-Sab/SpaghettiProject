package com.example.android.spaghettiproject;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.security.acl.Group;
import java.util.LinkedList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NextButtonListHolder> {

    //Initializing layout inflater and list of groups/items user is part of
    private final LinkedList<String> mGroupList;
    private final LayoutInflater mInflater;

    public class NextButtonListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final Button nextButtonView;
        public final RecyclerAdapter mAdapter;

        public NextButtonListHolder(@NonNull View itemView, RecyclerAdapter adapter) {
            super(itemView);
            nextButtonView = itemView.findViewById(R.id.button_group);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            Log.d("position:", Integer.toString(mPosition));//Tracks which element selected
            String element = mGroupList.get(mPosition);//element is group/item selected from list

            Intent intent;
            //Checks which activity is involved (groups or items in groups) to create appropriate intent
            if (this.getClass().getSimpleName().equals("GroupsActivity"))
                intent = new Intent(v.getContext(), ListsActivity.class);//Groups
            else
                intent = new Intent(v.getContext(), ContactUsActivity.class);//Lists
            Log.d("BEFORE PUTEXTRA:", "it works");//Logging
            intent.putExtra(GroupsActivity.EXTRA_MESSAGE, element);//retrieves map of extended data from intent, returning map of all other extras retrieved
            Log.d("BEFORE STARTACTIVITY", "it works");//Logging
            v.getContext().startActivity(intent);//change screens (activities)

        }
    }

    //Constructor with context, list
    public RecyclerAdapter(Context context, LinkedList<String> groupList) {
        mInflater = LayoutInflater.from(context);
        this.mGroupList = groupList;
    }

    //Used when RecyclerView needs a new ViewHolder for data
    @NonNull
    @Override
    public RecyclerAdapter.NextButtonListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.next_button_item, parent, false);
        return new NextButtonListHolder(mItemView, this);
    }

    //Used to display data at a specified position
    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.NextButtonListHolder holder, int position) {
        final String mCurrent = mGroupList.get(position);
        holder.nextButtonView.setText(mCurrent);
        holder.nextButtonView.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("position:", Integer.toString(mPosition));

                Intent intent;
                String currentActivity = holder.nextButtonView.getContext().getClass().getSimpleName();
                //Setting proper intent
                if (currentActivity.equals("GroupsActivity")) {//Dealing with groups
                    intent = new Intent(v.getContext(), ListsActivity.class);
                }
                else {//Dealing with lists
                    intent = new Intent(v.getContext(), ContactUsActivity.class);
                }
                intent.putExtra(GroupsActivity.EXTRA_MESSAGE, mCurrent);
                v.getContext().startActivity(intent);
            }
        });
    }

    //Returns size of group/list array
    @Override
    public int getItemCount() {
        return mGroupList.size();
    }


}
