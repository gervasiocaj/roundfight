package com.projetoes.roundfight.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by gervasio on 26/10/2015.
 */
public class WebClient {

    private static final String SERVER_URL = "http://roundfight-server-v2.herokuapp.com";
    private OkHttpClient client = new OkHttpClient();
    private ProgressDialog pDialog;

    protected boolean checkConnection() {
        try {
            return new AccessWeb().execute(SERVER_URL).get() == null;
        } catch (Exception e) {
        }
        return false;
    }

    protected JSONArray getLeaderboard() {
        try {
            String leaderboard = new AccessWeb().execute(SERVER_URL + "/leaderboard").get();
            return new JSONArray(leaderboard);
        } catch (Exception e) {
        }
        return new JSONArray();
    }

    protected JSONArray getLeaderboard(String user) {
        try {
            String leaderboard = new AccessWeb().execute(SERVER_URL + "/leaderboard/" + user).get();
            return new JSONArray(leaderboard);
        } catch (Exception e) {
        }
        return new JSONArray();
    }

    protected JSONObject getMultiplier() {
        if (!AndroidLauncher.gps.canGetLocation())
            return null;
        try {
            String url = SERVER_URL + "/multiplier/" + AndroidLauncher.gps.getLatitude() + "/" + AndroidLauncher.gps.getLongitude();
            String result = new AccessWeb().execute(url).get();
            return new JSONObject(result);
        } catch (Exception e) {
        }
        return null;
    }

    protected boolean postScore(String user, String points) {
        try {
            return new AccessWeb().execute(SERVER_URL + "/" + user + "/" + points).get() != null;
        } catch (Exception e){
        }
        return false;
    }

    private String getServer(String url) {
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
        }
        return result;
    }

    private class AccessWeb extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(AndroidLauncher.context, "Loading...", "Conneccting, please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String result = getServer(url);
            pDialog.dismiss();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO
        }
    }

}
