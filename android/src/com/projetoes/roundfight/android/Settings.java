package com.projetoes.roundfight.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Gervasio on 4/6/2015.
 */
public class Settings {

    protected static final String RF_PREFERENCES = "rf_preferences_v3";
    protected static final String RF_PREFERENCES_USER = "rf_user";
    protected static final String RF_PREFERENCES_SOUND = "rf_sound";
    protected static final String RF_PREFERENCES_VIBRATE = "rf_vibrate";
    protected static final String RF_PREFERENCES_HIGHSCORE = "rf_highscore";
    protected static final Preferences prefs = Gdx.app.getPreferences(RF_PREFERENCES);
    protected static Sound clickSound, wooshSound, ballHit;

    public static void load() {
        if (!prefs.contains(RF_PREFERENCES_SOUND))
            prefs.putBoolean(RF_PREFERENCES_SOUND, true).flush();

        if (!prefs.contains(RF_PREFERENCES_VIBRATE))
            prefs.putBoolean(RF_PREFERENCES_VIBRATE, true).flush();

        if (!prefs.contains(RF_PREFERENCES_USER))
            prefs.putString(RF_PREFERENCES_USER, "Anonymous").flush();

        if (!prefs.contains(RF_PREFERENCES_HIGHSCORE))
            prefs.putFloat(RF_PREFERENCES_HIGHSCORE, 0f).flush();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/billiards.mp3"));
        ballHit = Gdx.audio.newSound(Gdx.files.internal("data/ballHit.mp3"));
        wooshSound = Gdx.audio.newSound(Gdx.files.internal("data/woosh.mp3"));
    }

    public static void vibrateAndBeepIfAvailable() {
        if (prefs.getBoolean(RF_PREFERENCES_VIBRATE))
            Gdx.input.vibrate(100);
        if (prefs.getBoolean(RF_PREFERENCES_SOUND))
            clickSound.play();
    }

    public static void wooshSound() {
        if (prefs.getBoolean(RF_PREFERENCES_SOUND))
            wooshSound.play();
    }

    public static void ballHit() {
        if (prefs.getBoolean(RF_PREFERENCES_SOUND))
            ballHit.play();
    }

}
