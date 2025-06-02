package com.example.quizappv3;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.io.InputStream;

public class QuizImageFragment extends Fragment {

    private ImageView imageView;
    private QuizViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.quiz_image);
        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        viewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), entry -> {
            if (entry != null) {
                try {
                    Uri uri = Uri.parse(entry.imageUri);
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                    imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                } catch (Exception e) {
                    imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            }
        });
    }
}
