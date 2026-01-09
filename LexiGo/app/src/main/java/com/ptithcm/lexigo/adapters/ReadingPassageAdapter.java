package com.ptithcm.lexigo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.ReadingPassage;

import java.util.List;

public class ReadingPassageAdapter extends RecyclerView.Adapter<ReadingPassageAdapter.ViewHolder> {

    private Context context;
    private List<ReadingPassage> passages;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ReadingPassage passage);
    }

    public ReadingPassageAdapter(Context context, List<ReadingPassage> passages) {
        this.context = context;
        this.passages = passages;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reading_passage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReadingPassage passage = passages.get(position);

        holder.tvTitle.setText(passage.getTitle());
        holder.tvLevel.setText(passage.getLevel());
        
        // Display tags if available
        if (passage.getTags() != null && !passage.getTags().isEmpty()) {
            holder.tvTags.setText(String.join(", ", passage.getTags()));
            holder.tvTags.setVisibility(View.VISIBLE);
        } else {
            holder.tvTags.setVisibility(View.GONE);
        }

        // TODO: Load image using Glide or Picasso if imageUrl is available

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(passage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvLevel;
        TextView tvTags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivPassageThumbnail);
            tvTitle = itemView.findViewById(R.id.tvPassageTitle);
            tvLevel = itemView.findViewById(R.id.tvPassageLevel);
            tvTags = itemView.findViewById(R.id.tvPassageTags);
        }
    }
}
