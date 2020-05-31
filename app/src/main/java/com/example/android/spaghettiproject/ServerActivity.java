package com.example.android.spaghettiproject;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;

public class ServerActivity extends AsyncTask<String, Void, String> {

    private static final String ServerAPIKey = "TnqMS5BalKDYW6vE9gL80KrV1feGNhnq";
    private String email;
    private String name = null;
    private String password;
    private WeakReference<Context> context;
    private Boolean isMissingEmail = false;
    private Boolean isMissingPassword = false;
    private Boolean isMissingName = false;
    private WeakReference<ProgressBar> progressBar;
    
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    ServerActivity(AsyncResponse delegate, String email, String name, String password, ProgressBar progressBar) {
        this.delegate = delegate;
        //this.context = new WeakReference<>(context);
        this.email = email;
        this.name = name;
        this.password = password;
        this.progressBar = new WeakReference<> (progressBar);
    }

    //Do we need a serveractivity given no name?
    ServerActivity(AsyncResponse delegate, String email, String password, ProgressBar progressBar) {
        this.delegate = delegate;
        //this.context = new WeakReference<>(context);
        this.email = email;
        this.password = password;
        this.progressBar = new WeakReference<> (progressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.get().setVisibility(View.VISIBLE);
    }


    @Override
    protected String doInBackground(String... strings) {
        String urlParams = null;
		
        try {
            if (email.isEmpty()) {
                isMissingEmail = true;
                return null;
            } else if (password.isEmpty()) {
                isMissingPassword = true;
                return null;
            } else if (delegate.getClass().getSimpleName().equals("RegisterActivity") && name.isEmpty()) {
                isMissingName = true;
                return null;
            }

            if (delegate.getClass().getSimpleName().equals("RegisterActivity"))
                urlParams = "API_KEY=" + URLEncoder.encode(ServerAPIKey, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&name=" + URLEncoder.encode(name, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
            else if (delegate.getClass().getSimpleName().equals("LoginActivity"))
                urlParams = "API_KEY=" + URLEncoder.encode(ServerAPIKey, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
            else if (delegate.getClass().getSimpleName().equals("GroupsActivity"))
                urlParams = "API_KEY=" + URLEncoder.encode(ServerAPIKey, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.d("Error", e.toString());
            return null;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return NetworkUtils.getInfo(delegate.getClass().getSimpleName(), urlParams);
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progressBar.get().setVisibility(View.GONE);
        if (isMissingEmail)
            delegate.processFinish("missing email");
        else if (isMissingPassword)
            delegate.processFinish("missing password");
        else if (isMissingName)
            delegate.processFinish("missing name");
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
            if (delegate.getClass().getSimpleName().equals("LoginActivity")) {
                String responseCheck = response.substring(1, response.length() - 1);
                if (responseCheck.equals("Email does not exist")) {
                    delegate.processFinish("wrong email");
                } else if (responseCheck.equals("Wrong password")) {
                    delegate.processFinish("wrong password");
                } else if (responseCheck.equals("Login Success")) {
                    delegate.processFinish("success");
                }
            } else if (delegate.getClass().getSimpleName().equals("RegisterActivity")) {
                String responseCheck = response.substring(1, response.length() - 1);
                if (responseCheck.equals("Email already exists")) {
                    delegate.processFinish("email exists");
                } else if (responseCheck.equals("Registration successful")) {
                    delegate.processFinish("success");
                }
            }
        }
    }

}
