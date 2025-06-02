package com.example.quizappv3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class Gallery_Fragment_Add extends Fragment {

    private EditText inputName;
    private ImageView previewImage;
    private Uri imageUri;
    private GalleryViewModel viewModel;

    private final ActivityResultLauncher<String[]> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    Context context = requireContext();
                    context.getContentResolver().takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                    imageUri = uri;
                    previewImage.setImageURI(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_entry, container, false);

        inputName = view.findViewById(R.id.input_name);
        previewImage = view.findViewById(R.id.preview_image);
        Button buttonPickImage = view.findViewById(R.id.button_pick_image);
        Button buttonAdd = view.findViewById(R.id.button_add_entry);

        viewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

        buttonPickImage.setOnClickListener(v -> {
            imagePickerLauncher.launch(new String[]{"image/*"});
        });

        buttonAdd.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (name.isEmpty() || imageUri == null) {
                Toast.makeText(getContext(), "Fyll inn navn og velg bilde", Toast.LENGTH_SHORT).show();
                return;
            }

            Quiz_Entry entry = new Quiz_Entry(name, imageUri.toString());
            viewModel.addEntry(entry);

            Toast.makeText(getContext(), "Lagt til!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}

