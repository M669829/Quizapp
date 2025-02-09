package com.example.test

object DataStore {
    private val _imageEntries = mutableListOf<imageEntry>()
    val imageEntries: MutableList<imageEntry> get() = _imageEntries

    fun addEntry(entry: imageEntry) {
        _imageEntries.add(entry)
    }
}