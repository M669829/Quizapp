package com.example.test

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class quizaktivitet : AppCompatActivity() {

    private lateinit var quizImage: ImageView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var scoreText: TextView
    private lateinit var viewModel: SharedViewModel
    private lateinit var imageEntries: List<imageEntry>
    private lateinit var knapper: List<Button>

    private var correctAnswer: String = ""
    private var score = 0
    private var attempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_aktivitet)

        // Initialiser ViewModel og data
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        imageEntries = viewModel.getAllEntries()


        // Sjekk om det finnes data
        if (imageEntries.isEmpty()) {
            findViewById<TextView>(R.id.quiz_score).text = "Ikke noe innhold enda, vennligst legg til data"
            return
        }

        // Observer score for å oppdatere UI
        viewModel.score.observe(this) { score ->
            scoreText.text = "Score: $score"
        }

        // Initialiser UI-elementer
        quizImage = findViewById(R.id.quiz_image)
        option1 = findViewById(R.id.option_1)
        option2 = findViewById(R.id.option_2)
        option3 = findViewById(R.id.option_3)
        scoreText = findViewById(R.id.quiz_score)
        //lager liste med knappene
        knapper = listOf(option1,option2,option3)

        // Start første spørsmål
        loadNewQuestion()
    }

    private fun loadNewQuestion() {
        // Velg et tilfeldig bilde fra listen
        val shuffledEntries = imageEntries.shuffled()
        val selectedEntry = shuffledEntries[0]
        correctAnswer = selectedEntry.name

        // Oppdater UI med bildet og svaralternativer
        val imageUri = Uri.parse(selectedEntry.imageUri)
        quizImage.setImageURI(imageUri)

        val options = (shuffledEntries.map { it.name } + List(3) { "Dummy" }).take(3).shuffled()
        option1.text = options[0]
        option2.text = options[1]
        option3.text = options[2]

        // Sett opp knapphandlinger
        option1.setOnClickListener { checkAnswer(option1.text.toString()) }
        option2.setOnClickListener { checkAnswer(option2.text.toString()) }
        option3.setOnClickListener { checkAnswer(option3.text.toString()) }
    }

    /* private fun checkAnswer(selectedAnswer: String) {
        attempts++
        if (selectedAnswer == correctAnswer) {
            score++
            scoreText.text = "Score: $score/$attempts"
        } else {
            scoreText.text = "Feil! Riktig svar: $correctAnswer\nScore: $score/$attempts"
        }

        // Last inn nytt spørsmål
        loadNewQuestion()
    }*/
    private fun checkAnswer(selectedAnswer: String) {
        attempts++

        // Finn knappen som brukeren trykket på
        val selectedButton = when (selectedAnswer) {
            option1.text.toString() -> option1
            option2.text.toString() -> option2
            option3.text.toString() -> option3
            else -> null
        }

        // Finn riktig svar-knapp
        val correctButton = when (correctAnswer) {
            option1.text.toString() -> option1
            option2.text.toString() -> option2
            option3.text.toString() -> option3
            else -> null
        }

        if (selectedAnswer == correctAnswer) {
            viewModel.increaseScore("poeng") // Oppdater score i ViewModel
            scoreText.text = "Score: $score/$attempts"

            // Sett grønn farge på riktig knapp
            correctButton?.setBackgroundColor(Color.GREEN)
        } else {
            viewModel.increaseScore("")
            scoreText.text = "Feil! Riktig svar: $correctAnswer\nScore: $score/$attempts"

            // Sett rød farge på feil knapp
            selectedButton?.setBackgroundColor(Color.RED)
            // Sett grønn farge på riktig svar
            correctButton?.setBackgroundColor(Color.GREEN)
        }
        //Deaktiverer knappene etter inigialieringen
        gjørmedknapper(knapper) { button -> button.isEnabled = false}
        // Vent 1 sekund før neste spørsmål lastes inn
        Handler(Looper.getMainLooper()).postDelayed({
            gjørmedknapper(knapper) { button ->
                button.setBackgroundColor(Color.parseColor("#6A1B9A")) // Lilla farge
            } // Tilbakestill knappene før nytt spørsmål
            gjørmedknapper(knapper) { button -> button.isEnabled = true} //Aktiverer knappene igjen
            loadNewQuestion()
        }, 1200) // 1000 ms = 1 sekund


    }

    private fun gjørmedknapper(buttons: List<Button>, action: (Button) -> Unit) {
        buttons.forEach {  button -> action(button) }
    }
}
