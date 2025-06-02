package com.example.quizappv3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GalleryViewModel extends AndroidViewModel {

    private final Repository repository;
    private final MutableLiveData<List<Quiz_Entry>> galleryEntries = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());
        loadEntriesAZ(); // Default sortering
    }

    public LiveData<List<Quiz_Entry>> getGalleryEntries() {
        return galleryEntries;
    }

    public void loadEntriesAZ() {
        disposables.add(
                repository.getAllEntriesAZ()
                        .subscribeOn(Schedulers.io())
                        .subscribe(galleryEntries::postValue)
        );
    }

    public void loadEntriesZA() {
        disposables.add(
                repository.getAllEntriesZA()
                        .subscribeOn(Schedulers.io())
                        .subscribe(galleryEntries::postValue)
        );
    }

    public void addEntry(Quiz_Entry entry) {
        disposables.add(
                repository.insertEntry(entry)
                        .subscribeOn(Schedulers.io())
                        .andThen(repository.getAllEntriesAZ())
                        .subscribe(galleryEntries::postValue)
        );
    }

    public void deleteEntry(Quiz_Entry entry) {
        disposables.add(
                repository.deleteEntry(entry)
                        .subscribeOn(Schedulers.io())
                        .andThen(repository.getAllEntriesAZ())
                        .subscribe(galleryEntries::postValue)
        );
    }

    public void updateEntry(Quiz_Entry entry) {
        disposables.add(
                repository.updateEntry(entry)
                        .subscribeOn(Schedulers.io())
                        .andThen(repository.getAllEntriesAZ())
                        .subscribe(galleryEntries::postValue)
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
