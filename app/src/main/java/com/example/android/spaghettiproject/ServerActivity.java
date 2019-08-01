package com.example.android.spaghettiproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;

public class ServerActivity extends AsyncTask<String, Void, String> {


    private String user;
    private String name;
    private String password;


    ServerActivity(String email, String name, String password) {
        this.user = email;
        this.name = name;
        this.password = password;
    }

    ServerActivity(String email, String password) {
        this.user = email;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlParams = "ERROR";
        try {
            urlParams = "email=" + URLEncoder.encode(user.toString(), "UTF-8") + "&name=" + URLEncoder.encode(name.toString(), "UTF-8") + "&password=" + URLEncoder.encode(password.toString(), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.d("error exception occurred", e.toString());
        }
        return NetworkUtils.getInfo(urlParams);
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
//                user.get().setText("works");
//                password.get().setText("works");

            }else{
//                user.get().setText("");
//                password.get().setText("");
//                Toast.makeText(this,"Incorrect email or password", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {//may need to change to exception
//            user.get().setText("");
//            password.get().setText("");
            e.printStackTrace();
        }
    }

}
