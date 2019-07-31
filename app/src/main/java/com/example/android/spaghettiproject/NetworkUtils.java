package com.example.android.spaghettiproject;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //URL
    private static final String URL = "https://spaghetti-project.herokuapp.com/register";


    static String getInfo(String queryString){

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONString = null; //Not sure about this one

        try{
            //build request URI
            Uri builtURI = Uri.parse(URL).buildUpon().build();

            //Convert Uri to URL
            URL requestURL = new URL(builtURI.toString());

            //Open URL connection
            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Get the inputstream
            InputStream inputStream = urlConnection.getInputStream();

            //Creat a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Use a StringBuilder to hold the incomin response.
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
                //
                builder.append("\n");

                //May be in wrong spot
                if(builder.length() == 0){
                    //Stream was empty. No point in parsing
                    return null;
                }


            }

            //May be in wrong spot
            JSONString = builder.toString();

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            //
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader !=null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        Log.d(LOG_TAG, JSONString);
        return JSONString;
    }


}
