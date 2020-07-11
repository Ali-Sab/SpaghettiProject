package com.example.android.spaghettiproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

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

    private static String requestMethod = "";

    static String getInfo(String activityName, String urlParameters){
        if (urlParameters == null)
            return "Params are empty";
        String requestURL = URL;
        if (activityName.equals("RegisterActivity")) {
            requestURL += "register";
            requestMethod = "POST";
        }else if (activityName.equals("LoginActivity")) {
            requestURL += "login";
            requestMethod = "POST";
        }else if (activityName.equals("GroupsActivity")) {
            if(urlParameters.contains("&groupName=")){
                requestURL += "groups/add";
                requestMethod = "POST";
            }else{
                requestURL += "groups?" + urlParameters;
                requestMethod = "GET";
            }

        }else if (activityName.equals("ListsActivity")) {
            requestURL += "lists?" + urlParameters;
            requestMethod = "GET";
        }

        HttpsURLConnection conn = null;
        BufferedReader reader = null;
        String response = null;

        try{
            //byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            //int postDataLength = postData.length;
            URL url = new URL(requestURL);

            conn = (HttpsURLConnection) url.openConnection();

            if (requestMethod == "GET") {
                conn.setDoOutput(false);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
            } else {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(urlParameters.length()));
                conn.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParameters);


                wr.flush();
                wr.close();
            }




            Log.d(LOG_TAG, Integer.toString(conn.getResponseCode()));
            Log.d(LOG_TAG, conn.getResponseMessage());
//            Log.d(LOG_TAG, "getContent()=" + conn.getContent().toString());
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = "";
            String lineRead = input.readLine();
            while (lineRead != null) {
                response += (lineRead);
                lineRead = input.readLine();
            }

            input.close();

            Log.d(LOG_TAG, "response:\n" + response);

//            //build request URI
//            Uri builtURI = Uri.parse(URL).buildUpon().build();
//
//            //Convert Uri to URL
//            URL requestURL = new URL(builtURI.toString());
//
//            //Open URL connection
//            urlConnection = (HttpsURLConnection) requestURL.openConnection();
//
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//            urlConnection.connect();
//
//            //Get the inputstream
//            InputStream inputStream = urlConnection.getInputStream();
//
//            //Creat a buffered reader from that input stream.
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            //Use a StringBuilder to hold the incomin response.
//            StringBuilder builder = new StringBuilder();
//
//            String line;
//            while((line = reader.readLine()) != null){
//                builder.append(line);
//                //
//                builder.append("\n");
//
//                //May be in wrong spot
//                if(builder.length() == 0){
//                    //Stream was empty. No point in parsing
//                    return null;
//                }
//
//
//            }
//
//            //May be in wrong spot
//            JSONString = builder.toString();
//            JSONString = "hi";
//

        }catch(IOException e){
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response;
    }


}
