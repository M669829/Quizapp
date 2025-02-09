package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_gallery).setOnClickListener {
            val intent = Intent(this, galleriaktivitet::class.java)
            startActivity(intent)
        }

        // Naviger til Quizaktivitet
        findViewById<Button>(R.id.button_quiz).setOnClickListener {
            val intent = Intent(this, quizaktivitet::class.java)
            startActivity(intent)
        }
    }
}