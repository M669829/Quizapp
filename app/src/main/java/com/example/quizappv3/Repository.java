package com.example.quizappv3;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class Repository {

    private final GalleryDao galleryDao;

    public Repository(Context context) {
        GalleryDatabase db = GalleryDatabase.getInstance(context);
        galleryDao = db.galleryDao();
    }

    public Single<List<Quiz_Entry>> getAllEntriesAZ() {
        return Single.fromCallable(() -> galleryDao.getAllEntriesAZ());
    }

    public List<Quiz_Entry> getAllEntriesSync() {
        return galleryDao.getAllEntriesAZ(); // DAO-kall er blokkende og synkrone
    }

    public Single<List<Quiz_Entry>> getAllEntriesZA() {
        return Single.fromCallable(() -> galleryDao.getAllEntriesZA());
    }

    public Completable insertEntry(Quiz_Entry entry) {
        return Completable.fromAction(() -> galleryDao.insertEntry(entry));
    }

    public Completable updateEntry(Quiz_Entry entry) {
        return Completable.fromAction(() -> galleryDao.updateEntry(entry));
    }

    public Completable deleteEntry(Quiz_Entry entry) {
        return Completable.fromAction(() -> galleryDao.deleteEntry(entry));
    }
}
