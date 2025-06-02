package com.example.quizappv3;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // Sett dark mode tidlig før UI lastes
        viewModel.getDarkModeEnabledLiveData().observe(this, enabled -> {
            AppCompatDelegate.setDefaultNightMode(
                    enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Gå til innstillinger når knapp trykkes
        findViewById(R.id.settings_button).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, setting_activity.class));
        });

        findViewById(R.id.gallery_button).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, GalleryActivity.class));
        });

        findViewById(R.id.quiz_button).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, QuizActivity.class));
        });

        // Juster padding for systembars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Observer musikkstatus og start/stop tjenesten
        viewModel.getMusicEnabledLiveData().observe(this, enabled -> {
            Intent musicIntent = new Intent(this, MusicService.class);
            if (enabled) {
                startService(musicIntent);
            } else {
                stopService(musicIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicService.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getMusicEnabledLiveData().observe(this, enabled -> {
            if (enabled) {
                MusicService.resumeMusic(this);
            }
        });
    }

    protected void onRestart() {
        super.onRestart();
        Boolean musicEnabled = viewModel.getMusicEnabledLiveData().getValue();
        if (Boolean.TRUE.equals(musicEnabled)) {
            MusicService.resumeMusic(this);
        }
    }
}
