package com.apptest.homeassignment;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class SearchableActivity extends AsyncTask<String, Integer, JSONObject> {
    List<Exception> exceptions = new ArrayList<Exception>();
    // Runs in UI before background thread is called
    static JSONObject error = new JSONObject();
    @Override
    protected void onPreExecute() {
        try {
            error.put("error", 0);
        }
        catch (JSONException e) {
            return;
        }
        super.onPreExecute();
    }

    // This is run in a background thread
    @Override
    protected JSONObject doInBackground(String... urls){
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(150000); //milliseconds
            conn.setConnectTimeout(15000); // milliseconds
            conn.setRequestMethod("GET");
            conn.connect();
            try {
                if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    String jsonText = readAll(rd);
                    return new JSONObject(jsonText);
                } else {
                    return error;
                }
            } catch (JSONException e) {
                return error;
            }
            finally {
                conn.disconnect();
            }
        } catch (IOException e) {
            return error;
        }
    }
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(JSONObject result) {
        for (Exception e : exceptions) {
            Log.d("MYAPP", "exception", e);
        }
        super.onPostExecute(result);
    }
}

