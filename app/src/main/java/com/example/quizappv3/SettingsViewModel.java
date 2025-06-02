package com.example.quizappv3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SettingsViewModel extends AndroidViewModel {

    private final SettingsDataStore dataStore;
    private final MutableLiveData<Boolean> musicEnabledLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> darkModeEnabledLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        dataStore = SettingsDataStore.getInstance(application.getApplicationContext());

        // ✅ Observer kontinuerlig
        disposables.add(dataStore.observeMusicEnabled()
                .subscribe(musicEnabledLiveData::postValue));

        disposables.add(dataStore.observeDarkMode()
                .subscribe(darkModeEnabledLiveData::postValue));
    }

    public LiveData<Boolean> getMusicEnabledLiveData() {
        return musicEnabledLiveData;
    }

    public LiveData<Boolean> getDarkModeEnabledLiveData() {
        return darkModeEnabledLiveData;
    }

    public void setMusicEnabled(boolean enabled) {
        musicEnabledLiveData.setValue(enabled); // for lokal oppdatering
        dataStore.setMusicEnabled(enabled);     // for lagring
    }

    public void setDarkModeEnabled(boolean enabled) {
        darkModeEnabledLiveData.setValue(enabled);
        dataStore.setDarkMode(enabled);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
