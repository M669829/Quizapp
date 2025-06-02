package com.example.quizappv3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class setting_activity extends AppCompatActivity {

    private Switch switchMusic, switchTheme;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchMusic = findViewById(R.id.switch_music);
        switchTheme = findViewById(R.id.switch_theme);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        viewModel.getMusicEnabledLiveData().observe(this, switchMusic::setChecked);
        viewModel.getDarkModeEnabledLiveData().observe(this, switchTheme::setChecked);

        switchMusic.setOnCheckedChangeListener((btn, isChecked) -> {
            viewModel.setMusicEnabled(isChecked);
            Log.d("SettingsActivity", "Musikk slått " + (isChecked ? "på" : "av"));
        });

        switchTheme.setOnCheckedChangeListener((btn, isChecked) -> {
            viewModel.setDarkModeEnabled(isChecked);
            Log.d("SettingsActivity", "Dark Mode satt til " + isChecked);
        });
    }

    protected void onStart() {
        super.onStart();
        viewModel.getMusicEnabledLiveData().observe(this, enabled -> {
            if (enabled) {
                MusicService.resumeMusic(this);
            }
        });
    }


}
