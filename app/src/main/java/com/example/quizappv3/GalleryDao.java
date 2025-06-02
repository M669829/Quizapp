package com.example.quizappv3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GalleryDao {

    @Query("SELECT * FROM quiz_entries ORDER BY name ASC")
    List<Quiz_Entry> getAllEntriesAZ();

    @Query("SELECT * FROM quiz_entries ORDER BY name DESC")
    List<Quiz_Entry> getAllEntriesZA();

    @Insert
    void insertEntry(Quiz_Entry entry);

    @Delete
    void deleteEntry(Quiz_Entry entry);

    @Update
    void updateEntry(Quiz_Entry entry);
}

