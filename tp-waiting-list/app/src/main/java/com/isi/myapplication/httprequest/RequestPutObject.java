package com.isi.myapplication.httprequest;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
public class RequestPutObject extends AsyncTask<String, Void, String> {
    JSONObject postData;

    public RequestPutObject(Map<String, String> postData) {
        if (postData != null) {
            this.postData = new JSONObject(postData);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("PUT");

            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200) {

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                response = convertInputStreamToString(inputStream);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String convertInputStreamToString(InputStream inputStream)
        throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        inputStream.close();
        return result;

    }


}
