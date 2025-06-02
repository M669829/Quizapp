package com.example.quizappv3;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

public class QuizOptionsFragment extends Fragment {

    private QuizViewModel viewModel;
    private TextView feedbackText, scoreText;
    private Button option1, option2, option3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        feedbackText = view.findViewById(R.id.feedback_text);
        scoreText = view.findViewById(R.id.score_text);

        viewModel.getOptions().observe(getViewLifecycleOwner(), options -> {
            if (options != null && options.size() == 3) {
                resetButtons();
                option1.setText(options.get(0));
                option2.setText(options.get(1));
                option3.setText(options.get(2));
                feedbackText.setText("");
            }
        });

        View.OnClickListener listener = v -> {
            String selected = ((Button) v).getText().toString();
            viewModel.submitAnswer(selected);
            String correct = viewModel.getCurrentQuestion().getValue().name;

            if (selected.equals(correct)) {
                feedbackText.setText("Riktig!");
            } else {
                feedbackText.setText("Feil! Riktig svar: " + correct);
            }

            highlightAnswers(correct);
            new Handler().postDelayed(() -> viewModel.generateNewQuestion(), 3000);

            //viewModel.generateNewQuestion();
        };

        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);
        option3.setOnClickListener(listener);

        viewModel.getCorrectAnswers().observe(getViewLifecycleOwner(), correct ->
                updateScore(correct, viewModel.getTotalAnswers().getValue()));
        viewModel.getTotalAnswers().observe(getViewLifecycleOwner(), total ->
                updateScore(viewModel.getCorrectAnswers().getValue(), total));
    }

    private void updateScore(Integer correct, Integer total) {
        if (correct != null && total != null) {
            scoreText.setText("Score: " + correct + " / " + total);
        }
    }

    private void highlightAnswers(String correctAnswer) {
        List<Button> buttons = Arrays.asList(option1, option2, option3);

        for (Button button : buttons) {
            String text = button.getText().toString();
            if (text.equals(correctAnswer)) {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            } else {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
            button.setEnabled(false); // Deaktiver knappene etter svar
        }
    }

    private void resetButtons() {
        List<Button> buttons = Arrays.asList(option1, option2, option3);
        for (Button button : buttons) {
            button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            button.setEnabled(true);
        }
    }


}
