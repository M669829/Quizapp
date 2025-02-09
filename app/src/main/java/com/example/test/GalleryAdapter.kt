package com.example.test

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GalleryAdapter(private var imageEntries: List<imageEntry>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val textView: TextView = view.findViewById(R.id.text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.galleri_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = imageEntries[position]
        holder.textView.text = entry.name

        val imageUri = Uri.parse(entry.imageUri)
        holder.imageView.setImageURI(imageUri)
    }

    override fun getItemCount(): Int {
        return imageEntries.size
    }

    fun updateData(newEntries: List<imageEntry>) {
        imageEntries = newEntries.toList() // Sikrer at en ny liste brukes
        notifyDataSetChanged()
    }
}
