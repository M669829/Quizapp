package com.example.quizappv3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "quiz_entries")
public class Quiz_Entry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String imageUri; // Lagres som String (f.eks. content://...)

    public Quiz_Entry(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }
}


