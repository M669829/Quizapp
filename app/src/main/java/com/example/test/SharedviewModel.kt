package com.example.test

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    // Liste for bilder og navn
    private val _imageEntries = MutableLiveData<List<imageEntry>>()
    val imageEntries: LiveData<List<imageEntry>> get() = _imageEntries

    // Score
    private val _score = MutableLiveData(0) // Starter på 0
    val score: LiveData<Int> get() = _score

    // Score
    private val _attempt = MutableLiveData(0) // Starter på 0
    val attempt: LiveData<Int> get() = _attempt

    init {
        if (DataStore.imageEntries.isEmpty()) {
            // Legger til dummydata hvis DataStore er tom
            val dummyData = listOf(
                imageEntry("Bil", "android.resource://com.example.test/drawable/bil"),
                imageEntry("Eplepai", "android.resource://com.example.test/drawable/eplepai"),
                imageEntry("Helikopter", "android.resource://com.example.test/drawable/helikopter2"),
                imageEntry("Ring", "android.resource://com.example.test/drawable/test")
            )
            dummyData.forEach { DataStore.addEntry(it) }
        }
        // Initialiser LiveData med data fra DataStore
         _imageEntries.value = DataStore.imageEntries.toList()


    }

    // Legg til et nytt bilde/navn-par
    fun addEntry(name: String, imageUri: String) {
        val newEntry = imageEntry(name, imageUri)
        DataStore.addEntry(newEntry) // Legg til i DataStore
        _imageEntries.value = DataStore.imageEntries.toList() // Oppdater LiveData
    }


    // Sorter listen
    fun sortEntries(ascending: Boolean) {
        val sortedList = if (ascending) {
            DataStore.imageEntries.sortedBy { it.name }
        } else {
            DataStore.imageEntries.sortedByDescending { it.name }
        }

        // Oppdater både DataStore og LiveData
        DataStore.imageEntries.clear()
        DataStore.imageEntries.addAll(sortedList)
        _imageEntries.value = DataStore.imageEntries.toList()
    }

    fun getAllEntries(): List<imageEntry> {
        return DataStore.imageEntries.toList()
    }

    fun increaseScore(navn:String) {
        if (navn == "poeng") {
        _score.value = (_score.value ?: 0) + 1
        _attempt.value = (_attempt.value ?:0) +1}
        else {
            _attempt.value = (_attempt.value ?:0) +1
        }
    }

    fun resetScore() {
        _score.value = 0
    }
    fun getScore(): Int? {
        return _score.value
    }
}

