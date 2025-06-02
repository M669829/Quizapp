package com.example.quizappv3;

import static com.example.quizappv3.MusicService.changeMusic;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel viewModel;
    private SettingsViewModel viewModelSetting;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        viewModelSetting = new ViewModelProvider(this).get(SettingsViewModel.class);

        // Observer musikkstatus og start/stop tjenesten
        viewModelSetting.getMusicEnabledLiveData().observe(this, enabled -> {
            Intent musicIntent = new Intent(this, MusicService.class);
            if (enabled) {
                changeMusic(this, R.raw.musikk_quiz);
            } else {
                stopService(musicIntent);
            }
        });
        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_image, new QuizImageFragment())
                    .replace(R.id.fragment_container_options, new QuizOptionsFragment())
                    .commit();
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicService.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean musicEnabled = viewModelSetting.getMusicEnabledLiveData().getValue();
        if (Boolean.TRUE.equals(musicEnabled)) {
            MusicService.resumeMusicQuiz(this);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        MusicService.changeMusic(this, R.raw.musikk_main);
    }
}
