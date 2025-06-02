package com.example.quizappv3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import io.reactivex.rxjava3.annotations.NonNull;

@Database(entities = {Quiz_Entry.class}, version = 1, exportSchema = false)
public abstract class GalleryDatabase extends RoomDatabase {

    private static volatile GalleryDatabase INSTANCE;

    public abstract GalleryDao galleryDao();

    public static GalleryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GalleryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            GalleryDatabase.class,
                            "gallery_database"
                    ).addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                GalleryDao dao = getInstance(context).galleryDao();

                                dao.insertEntry(new Quiz_Entry("Bil", "android.resource://com.example.quizappv3/drawable/bil"));
                                dao.insertEntry(new Quiz_Entry("Eplepai", "android.resource://com.example.quizappv3/drawable/eplepai"));
                                dao.insertEntry(new Quiz_Entry("Helikopter", "android.resource://com.example.quizappv3/drawable/helikopter2"));
                            });
                        }
                    }).build();
                }
            }
        }
        return INSTANCE;
    }
}
