package com.example.android.spaghettiproject;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class ServerActivity extends AsyncTask<String, Void, String> {


    private WeakReference<TextView> email;
    private WeakReference<TextView> name;
    private WeakReference<TextView> password;



    ServerActivity(TextView email, TextView name, TextView password){
        this.email = new WeakReference<>(email);
        this.name = new WeakReference<>(name);
        this.password = new WeakReference<>(password);
    }

    ServerActivity(TextView email, TextView password){
        this.email = new WeakReference<>(email);
        this.password = new WeakReference<>(password);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
    }

}
