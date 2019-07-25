package com.example.android.spaghettiproject.Server;

import android.os.AsyncTask;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
    }

}
