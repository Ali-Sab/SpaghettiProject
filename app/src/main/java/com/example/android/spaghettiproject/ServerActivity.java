package com.example.android.spaghettiproject;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;

public class ServerActivity extends AsyncTask<JSONObject, Void, JSONObject> {

    private static final String ServerAPIKey = "TnqMS5BalKDYW6vE9gL80KrV1feGNhnq";
    public interface AsyncResponse {
        void processFinish(JSONObject response);
    }

    public AsyncResponse delegate = null;
    private String path;

    ServerActivity(AsyncResponse delegate, String path) {
        this.delegate = delegate;
        this.path = path;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected JSONObject doInBackground(JSONObject ...requestBodies) {
        JSONObject requestBody = null;

        if (requestBodies.length > 0) {
            requestBody = requestBodies[0];
            try {
                requestBody.put("API_KEY", ServerAPIKey);
            } catch (JSONException e) {
                Log.d("Error: ", e.toString());
                return null;
            }
        }

        return NetworkUtils.getInfo(path, requestBody);
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        delegate.processFinish(response);
    }
}
