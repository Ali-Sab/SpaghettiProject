package com.example.android.spaghettiproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //URL
    private static final String URL = "https://spaghetti-project.herokuapp.com/";

    static JSONObject getInfo(String path, JSONObject requestBody){
        JSONObject responseJSON = null;
        HttpsURLConnection conn = null;

        try {
            URL targetURL = new URL(URL + path);
            String response = null;

            conn = (HttpsURLConnection) targetURL.openConnection();

            if (requestBody != null) {
                String request = requestBody.toString();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(request.length()));

                Log.d("Request: ", request);                                                         /// TEMPORARY!!! PLEASE REMOVE

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(request);
                wr.flush();
                wr.close();
            }

            Log.d(LOG_TAG, Integer.toString(conn.getResponseCode()));
            Log.d(LOG_TAG, conn.getResponseMessage());

            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = "";
            String lineRead = input.readLine();
            while (lineRead != null) {
                response += (lineRead);
                lineRead = input.readLine();
            }
            input.close();

            Log.d(LOG_TAG, "response:\n" + response);
            responseJSON = new JSONObject(response);

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return responseJSON;
    }
}
