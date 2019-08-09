package com.example.android.spaghettiproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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


    private String email = null;
    private String name = null;
    private String password = null;
    private Context context;
    private Boolean isMissingEmail = false;
    private Boolean isMissingPassword = false;
    private Boolean isMissingName = false;


    ServerActivity(@NonNull Context context, String email, String name, String password) {
        this.context = context;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    ServerActivity(Context context, String email, String password) {
        this.context = context;
        this.email = email;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlParams = "ERROR";
        try {
            if (email.isEmpty()) {
                isMissingEmail = true;
                return null;
            } else if (password.isEmpty()) {
                isMissingPassword = true;
                return null;
            } else if (context.getClass().getSimpleName() == "ProfileActivity" && name.isEmpty()) {
                isMissingName = true;
                return null;
            }

            if (context.getClass().getSimpleName() == "ProfileActivity")
                urlParams = "email=" + URLEncoder.encode(email.toString(), "UTF-8") + "&name=" + URLEncoder.encode(name.toString(), "UTF-8") + "&password=" + URLEncoder.encode(password.toString(), "UTF-8");
            else
                urlParams = "email=" + URLEncoder.encode(email.toString(), "UTF-8") + "&password=" + URLEncoder.encode(password.toString(), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.d("Error", e.toString());
            return null;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return NetworkUtils.getInfo(context.getClass().getSimpleName(), urlParams);
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (isMissingEmail == true)
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_LONG).show();
        else if (isMissingPassword == true)
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_LONG).show();
        else if (isMissingName == true)
            Toast.makeText(context, "Please enter your name", Toast.LENGTH_LONG).show();
//        try {
//            JSONObject jsonObject = new JSONObject(s);
//            JSONArray itemsArray = jsonObject.getJSONArray("items");
//
//            int i = 0;
//            String email = null;
//            String pass = null;
//
//            while (i < itemsArray.length() &&
//                    (pass == null && email == null)) {
//                // Get the current item information.
//                JSONObject account = itemsArray.getJSONObject(i);
//                JSONObject Info = account.getJSONObject("volumeInfo");
//
//                // Try to get the email and password from the current item,
//                // catch if either field is empty and move on.
//                try {
//                    email = Info.getString("email");
//                    pass = Info.getString("password");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // Move to the next item.
//                i++;
//            }
//
//            //If both found, then good but if not found, reset
//            if (email != null && password != null) {
//                //change to groups activity
////                user.get().setText("works");
////                password.get().setText("works");
//
//            }else{
////                user.get().setText("");
////                password.get().setText("");
////                Toast.makeText(this,"Incorrect email or password", Toast.LENGTH_LONG).show();
//            }
//
//        } catch (JSONException e) {//may need to change to exception
////            user.get().setText("");
////            password.get().setText("");
//            e.printStackTrace();
//        }
        if (response != null) {
            String responseCheck = response.substring(1, response.length() - 1);
            if (responseCheck.equals("Email does not exist")) {
                new AlertDialog.Builder(context)
                        .setTitle("Login Error")
                        .setMessage("Email does not exist.")
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else if (responseCheck.equals("Wrong password")) {
                new AlertDialog.Builder(context)
                        .setTitle("Login Error")
                        .setMessage("Password is incorrect.")
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else if (responseCheck.equals("Login Success")) {
                new AlertDialog.Builder(context)
                        .setTitle("Title")
                        .setMessage("Message")
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

}
