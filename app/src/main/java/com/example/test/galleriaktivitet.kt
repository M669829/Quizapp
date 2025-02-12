package com.example.test

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class galleriaktivitet : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private var selectedImageResId: Int = R.drawable.leggtil // Placeholder for valgt bilde
    private val STORAGE_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.galleri_aktivitet)

        checkStoragePermission()

        // Initialiser ViewModel
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Initialiser RecyclerView og adapter
        recyclerView = findViewById(R.id.gallery_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = GalleryAdapter(emptyList())
        recyclerView.adapter = adapter



        // Observer ViewModel for oppdateringer
        viewModel.imageEntries.observe(this) { entries ->
            adapter.updateData(entries)
        }

        // Legg til funksjonalitet for "Add Entry"-knappen
        findViewById<Button>(R.id.button_add_entry).setOnClickListener {
            showAddEntryDialog()
        }

        // Funksjonalitet for "Sort A-Z"-knappen
        findViewById<Button>(R.id.button_sort_ascending).setOnClickListener {
            viewModel.sortEntries(ascending = true) // Sorter i stigende rekkefølge
        }

        // Funksjonalitet for "Sort Z-A"-knappen
        findViewById<Button>(R.id.button_sort_descending).setOnClickListener {
            viewModel.sortEntries(ascending = false) // Sorter i synkende rekkefølge
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES), STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Tillatelse gitt!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Tillatelse avvist. Kan ikke hente bilder.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showAddEntryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_leggtil, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.edit_text_name)
        val buttonSelectImage = dialogView.findViewById<Button>(R.id.button_select_image)
        val bildepreview = dialogView.findViewById<ImageView>(R.id.preview_image_view)

        // Åpne bildevelger når brukeren trykker på "Select Image"
        buttonSelectImage.setOnClickListener {
            openImagePicker()
        }

        // Vis dialogen
        AlertDialog.Builder(this)
            .setTitle("Add New Entry")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = editTextName.text.toString()
                if (name.isNotEmpty() && bilde != null ) {
                    viewModel.addEntry(name, bilde!!.toString())
                } else{
                    Toast.makeText(this, "Du må velge et bilde", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private var bilde: Uri? = null

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            bilde = result.data!!.data
            // Du kan lagre denne URI-en eller konvertere den til en ressurs-ID hvis nødvendig
            // For enkelhetens skyld, setter vi en placeholder
           // selectedImageResId = R.drawable.test1 // Oppdater med riktig ressurs-ID
        }
    }
    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val entryNames = ArrayList<String>()
        val entryUris = ArrayList<String>()

        viewModel.getAllEntries().forEach {
            entryNames.add(it.name)
            entryUris.add(it.imageUri)
        }

        outState.putStringArrayList("entryNames", entryNames)
        outState.putStringArrayList("entryUris", entryUris)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val entryNames = savedInstanceState.getStringArrayList("entryNames") ?: arrayListOf()
        val entryUris = savedInstanceState.getStringArrayList("entryUris") ?: arrayListOf()

        if (entryNames.isNotEmpty() && entryUris.isNotEmpty()) {
            for (i in entryNames.indices) {
                viewModel.addEntry(entryNames[i], entryUris[i])
            }
        }
    } */

}


