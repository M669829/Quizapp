package com.example.test

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    // Liste for bilder og navn
    private val _imageEntries = MutableLiveData<List<imageEntry>>()
    val imageEntries: LiveData<List<imageEntry>> get() = _imageEntries

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
        val currentList = _imageEntries.value?.toMutableList() ?: mutableListOf()
        if (ascending) {
            currentList.sortBy { it.name }
        } else {
            currentList.sortByDescending { it.name }
        }
        _imageEntries.value = currentList
    }

    fun getAllEntries(): List<imageEntry> {
        return DataStore.imageEntries.toList()
    }
}
