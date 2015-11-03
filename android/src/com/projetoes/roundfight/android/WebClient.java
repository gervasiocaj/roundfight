package com.projetoes.roundfight.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
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
    private static final OkHttpClient client = new OkHttpClient();

    protected static boolean checkConnection2() {
        Log.i("roundfight", "checkConnection2");
        AndroidLauncher.startLoadingAnimation("Verifying connection...");
        final String[] c = new String[1];

        Net.HttpRequest a = new Net.HttpRequest();
        a.setMethod(Net.HttpMethods.GET);
        a.setUrl(SERVER_URL);
        Net.HttpResponseListener b = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                c[0] = httpResponse.getResultAsString();
                Log.i("rf2", c[0]);
                AndroidLauncher.stopLoadingAnimation();
            }

            @Override
            public void failed(Throwable t) {
                Log.i("rf2", "fecking failed ");
                t.printStackTrace();
                AndroidLauncher.stopLoadingAnimation();
            }

            @Override
            public void cancelled() {
                Log.i("rf2", "fecking canceled");
                AndroidLauncher.stopLoadingAnimation();
            }
        };
        Gdx.net.sendHttpRequest(a, b);
        return c[0] != null;
    }

    protected static boolean checkConnection() {
        Log.i("roundfight", "checkConnection");
        AndroidLauncher.startLoadingAnimation("Verifying connection...");
        try {
            Object res = new AccessWeb().execute(SERVER_URL).get();
            AndroidLauncher.stopLoadingAnimation();
            return res != null;
        } catch (Exception e) {
        }
        AndroidLauncher.stopLoadingAnimation();
        return false;
    }

    protected static JSONArray getLeaderboard() {
        AndroidLauncher.startLoadingAnimation("Loading leaderboards...");
        try {
            String leaderboard = new AccessWeb().execute(SERVER_URL + "/leaderboard").get();
            AndroidLauncher.stopLoadingAnimation();
            return new JSONArray(leaderboard);
        } catch (Exception e) {
        }
        AndroidLauncher.stopLoadingAnimation();
        return new JSONArray();
    }

    protected static JSONArray getLeaderboard(String user) {
        AndroidLauncher.startLoadingAnimation("Loading your position on the leaderboards...");
        try {
            String leaderboard = new AccessWeb().execute(SERVER_URL + "/leaderboard/" + user).get();
            AndroidLauncher.stopLoadingAnimation();
            return new JSONArray(leaderboard);
        } catch (Exception e) {
        }
        AndroidLauncher.stopLoadingAnimation();
        return new JSONArray();
    }

    protected static JSONObject getMultiplier() {
        AndroidLauncher.startLoadingAnimation("Obtaining your multiplier...");
        if (!AndroidLauncher.gps.canGetLocation())
            return null;
        try {
            String url = SERVER_URL + "/multiplier/" + AndroidLauncher.gps.getLatitude() + "/" + AndroidLauncher.gps.getLongitude();
            String result = new AccessWeb().execute(url).get();
            AndroidLauncher.stopLoadingAnimation();
            return new JSONObject(result);
        } catch (Exception e) {
        }
        AndroidLauncher.stopLoadingAnimation();
        return null;
    }

    protected static boolean postScore(String user, float points) {
        AndroidLauncher.startLoadingAnimation("Updating your highscore on the leaderboards...");
        try {
            String a = new AccessWeb().execute(SERVER_URL + "/leaderboard/" + user + "/" + Float.toString(points)).get();
            AndroidLauncher.stopLoadingAnimation();
            return a != null;
        } catch (Exception e){
        }
        AndroidLauncher.stopLoadingAnimation();
        return false;
    }

    private static String getServer(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
        }
        return null;
    }

    private static class AccessWeb extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("rf2", "preexecute");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("rf2", "do in bg");
            String url = params[0];
            String result = getServer(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO
        }
    }
}
