package com.example.quizappv3;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class QuizViewModel extends AndroidViewModel {

    private final Repository repository;
    private final MutableLiveData<Quiz_Entry> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<List<String>> options = new MutableLiveData<>();
    private final MutableLiveData<Integer> correctAnswers = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalAnswers = new MutableLiveData<>(0);
    private List<Quiz_Entry> allEntries = new ArrayList<>();
    private final Random random = new Random();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public QuizViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        loadEntries(); // Laster asynkront
    }

    private void loadEntries() {
        disposables.add(
                repository.getAllEntriesAZ()
                        .subscribeOn(Schedulers.io())
                        .subscribe(entries -> {
                            allEntries = entries;
                            generateNewQuestion();
                        }, throwable -> {
                            Log.e("QuizViewModel", "Feil ved lasting av quiz-data", throwable);
                        })
        );
    }

    public void generateNewQuestion() {
        if (allEntries == null || allEntries.size() < 3) return;

        Quiz_Entry correct = allEntries.get(random.nextInt(allEntries.size()));
        List<String> wrongAnswers = new ArrayList<>();

        while (wrongAnswers.size() < 2) {
            Quiz_Entry wrong = allEntries.get(random.nextInt(allEntries.size()));
            if (!wrong.name.equals(correct.name) && !wrongAnswers.contains(wrong.name)) {
                wrongAnswers.add(wrong.name);
            }
        }

        List<String> optionsList = new ArrayList<>(wrongAnswers);
        optionsList.add(correct.name);
        Collections.shuffle(optionsList);

        currentQuestion.postValue(correct);
        options.postValue(optionsList);
    }

    public void submitAnswer(String selected) {
        totalAnswers.postValue(totalAnswers.getValue() + 1);
        if (selected.equals(currentQuestion.getValue().name)) {
            correctAnswers.postValue(correctAnswers.getValue() + 1);
        }
    }

    public LiveData<Quiz_Entry> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<List<String>> getOptions() {
        return options;
    }

    public LiveData<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public LiveData<Integer> getTotalAnswers() {
        return totalAnswers;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}


