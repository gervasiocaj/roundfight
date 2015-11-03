package com.projetoes.roundfight.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	public static GPSManager gps;
    private static ProgressDialog pDialog;
    public static Context context;
    public static Activity ui;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        ui = this;
        context = this;
        gps = new GPSManager();

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Loading");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(), config);
	}

	public static void startLoadingAnimation(final String message) {
		ui.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("rf2", "started loading animation");
                pDialog.setMessage(message);
                pDialog.show();
            }
        });
	}

	public static void stopLoadingAnimation() {
        ui.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("rf2", "stopped loading animation");
                pDialog.dismiss();
            }
        });
    }
}