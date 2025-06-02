package com.example.quizappv3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.OnItemClickListener {

    private GalleryViewModel viewModel;
    private GalleryAdapter adapter;
    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // 🎵 Musikkoppsett
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.getMusicEnabledLiveData().observe(this, enabled -> {
            Intent musicIntent = new Intent(this, MusicService.class);
            if (enabled) {
                MusicService.resumeMusic(this);
            } else {
                stopService(musicIntent);
            }
        });

        // 🔁 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_gallery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GalleryAdapter(this);
        recyclerView.setAdapter(adapter);

        // 📦 ViewModel for galleri
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        viewModel.getGalleryEntries().observe(this, adapter::submitList);

        // 🔘 Sorteringsknapper og legg til
        Button buttonAZ = findViewById(R.id.btn_sort_az);
        Button buttonZA = findViewById(R.id.btn_sort_za);
        FloatingActionButton buttonAdd = findViewById(R.id.btn_add);

        buttonAZ.setOnClickListener(v -> viewModel.loadEntriesAZ());
        buttonZA.setOnClickListener(v -> viewModel.loadEntriesZA());

        buttonAdd.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.add_fragment_container, new Gallery_Fragment_Add())
                    .addToBackStack(null)
                    .commit();
            findViewById(R.id.add_fragment_container).setVisibility(View.VISIBLE);
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            FrameLayout fragmentContainer = findViewById(R.id.add_fragment_container);
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                fragmentContainer.setVisibility(View.GONE);
            }
        });
    }

    // 🔁 Slett oppføring
    @Override
    public void onItemClick(Quiz_Entry entry) {
        viewModel.deleteEntry(entry);
    }

    // 🗑️ Bekreft sletting
    public void showDeleteConfirmation(Context context, Quiz_Entry entry, Runnable onDelete) {
        new AlertDialog.Builder(context)
                .setTitle("Slett oppføring")
                .setMessage("Er du sikker på at du vil slette \"" + entry.name + "\"?")
                .setPositiveButton("Ja", (dialog, which) -> onDelete.run())
                .setNegativeButton("Nei", null)
                .show();
    }

    // ✏️ Endre navn
    public void showRenameDialog(Context context, Quiz_Entry entry) {
        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(entry.name);

        new AlertDialog.Builder(context)
                .setTitle("Endre navn")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        Quiz_Entry updated = new Quiz_Entry(newName, entry.imageUri);
                        updated.id = entry.id;
                        viewModel.updateEntry(updated);
                    }
                })
                .setNegativeButton("Avbryt", null)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicService.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean musicEnabled = settingsViewModel.getMusicEnabledLiveData().getValue();
        if (Boolean.TRUE.equals(musicEnabled)) {
            MusicService.resumeMusic(this);
        }
    }
}
