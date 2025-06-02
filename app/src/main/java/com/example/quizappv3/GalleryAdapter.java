package com.example.quizappv3;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class GalleryAdapter extends ListAdapter<Quiz_Entry, GalleryAdapter.GalleryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Quiz_Entry entry); // brukes til sletting
    }

    private final OnItemClickListener clickListener;

    public GalleryAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.bind(getItem(position), clickListener);
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final ImageView imageView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.entry_name);
            imageView = itemView.findViewById(R.id.entry_image);
        }

        public void bind(Quiz_Entry entry, OnItemClickListener listener) {
            nameTextView.setText(entry.name);

            try {
                Context context = itemView.getContext();
                InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(entry.imageUri));
                imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }

            // Langt trykk → slett
            itemView.setOnLongClickListener(v -> {
                if (listener instanceof GalleryActivity) {
                    ((GalleryActivity) listener).showDeleteConfirmation(
                            v.getContext(),
                            entry,
                            () -> listener.onItemClick(entry)
                    );
                }
                return true; // Viktig for å hindre at også onClick trigges
            });

            // Vanlig trykk → endre navn
            itemView.setOnClickListener(v -> {
                if (listener instanceof GalleryActivity) {
                    ((GalleryActivity) listener).showRenameDialog(v.getContext(), entry);
                }
            });
        }
    }

    private static final DiffUtil.ItemCallback<Quiz_Entry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Quiz_Entry oldItem, @NonNull Quiz_Entry newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Quiz_Entry oldItem, @NonNull Quiz_Entry newItem) {
                    return oldItem.name.equals(newItem.name)
                            && oldItem.imageUri.equals(newItem.imageUri);
                }
            };
}

