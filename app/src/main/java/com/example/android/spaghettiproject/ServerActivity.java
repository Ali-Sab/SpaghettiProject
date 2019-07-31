package com.example.android.spaghettiproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class ServerActivity extends AsyncTask<String, Void, String> {


    private WeakReference<TextView> user;
    private WeakReference<TextView> name;
    private WeakReference<TextView> password;


    ServerActivity(TextView email, TextView name, TextView password) {
        this.user = new WeakReference<>(email);
        this.name = new WeakReference<>(name);
        this.password = new WeakReference<>(password);
    }

    ServerActivity(TextView email, TextView password) {
        this.user = new WeakReference<>(email);
        this.password = new WeakReference<>(password);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            int i = 0;
            String email = null;
            String pass = null;

            while (i < itemsArray.length() &&
                    (pass == null && email == null)) {
                // Get the current item information.
                JSONObject account = itemsArray.getJSONObject(i);
                JSONObject Info = account.getJSONObject("volumeInfo");

                // Try to get the email and password from the current item,
                // catch if either field is empty and move on.
                try {
                    email = Info.getString("email");
                    pass = Info.getString("password");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            //If both found, then good but if not found, reset
            if (email != null && password != null) {
                //change to groups activity
                user.get().setText("works");
                password.get().setText("works");

            }else{
                user.get().setText("");
                password.get().setText("");
                //Toast.makeText(this,"Incorrect email or password", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {//may need to change to exception
            user.get().setText("");
            password.get().setText("");
            e.printStackTrace();
        }
    }

}
