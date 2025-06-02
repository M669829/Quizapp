package com.example.quizappv3;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class SettingsDataStore {

    private static final String STORE_NAME = "settings";
    private static final Preferences.Key<Boolean> DARK_MODE = PreferencesKeys.booleanKey("dark_mode");
    private static final Preferences.Key<Boolean> MUSIC_ENABLED = PreferencesKeys.booleanKey("music_enabled");

    private static SettingsDataStore INSTANCE;
    private final RxDataStore<Preferences> dataStore;

    private SettingsDataStore(Context context) {
        dataStore = new RxPreferenceDataStoreBuilder(context.getApplicationContext(), STORE_NAME).build();
    }

    public static synchronized SettingsDataStore getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SettingsDataStore(context);
        }
        return INSTANCE;
    }

    // ✅ Kontinuerlig observasjon
    public Flowable<Boolean> observeDarkMode() {
        return dataStore.data()
                .map(prefs -> prefs.get(DARK_MODE) != null && prefs.get(DARK_MODE));
    }

    public Flowable<Boolean> observeMusicEnabled() {
        return dataStore.data()
                .map(prefs -> prefs.get(MUSIC_ENABLED) == null || prefs.get(MUSIC_ENABLED));
    }

    // ✅ Sette verdier
    public void setDarkMode(boolean enabled) {
        dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePrefs = prefsIn.toMutablePreferences();
            mutablePrefs.set(DARK_MODE, enabled);
            return Single.just(mutablePrefs);
        });
    }

    public void setMusicEnabled(boolean enabled) {
        dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePrefs = prefsIn.toMutablePreferences();
            mutablePrefs.set(MUSIC_ENABLED, enabled);
            return Single.just(mutablePrefs);
        });
    }
}
