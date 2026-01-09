package com.ptithcm.lexigo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.VocabTopic;

import java.util.List;

public class VocabTopicAdapter extends RecyclerView.Adapter<VocabTopicAdapter.ViewHolder> {
    
    private Context context;
    private List<VocabTopic> topics;
    private OnTopicClickListener listener;
    private OnQuizClickListener quizListener;

    public interface OnTopicClickListener {
        void onTopicClick(VocabTopic topic);
    }
    
    public interface OnQuizClickListener {
        void onQuizClick(VocabTopic topic);
    }

    public VocabTopicAdapter(Context context, List<VocabTopic> topics, OnTopicClickListener listener) {
        this.context = context;
        this.topics = topics;
        this.listener = listener;
    }
    
    public void setOnQuizClickListener(OnQuizClickListener listener) {
        this.quizListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vocab_topic_grid, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VocabTopic topic = topics.get(position);
        
        holder.tvName.setText(topic.getName());
        holder.tvDescription.setText(topic.getDescription());
        holder.tvLevel.setText(topic.getLevel());

        // TODO: Load image with Glide or Picasso
        // Glide.with(context).load(topic.getImageUrl()).into(holder.ivIcon);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTopicClick(topic);
            }
        });

        holder.btnStartQuiz.setOnClickListener(v -> {
            if (quizListener != null) {
                quizListener.onQuizClick(topic);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return topics.size();
    }
    
    public void updateTopics(List<VocabTopic> newTopics) {
        this.topics = newTopics;
        notifyDataSetChanged();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvDescription, tvLevel, tvWordCount;
        Button btnStartQuiz;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_topic_icon);
            tvName = itemView.findViewById(R.id.tv_topic_name);
            tvDescription = itemView.findViewById(R.id.tv_topic_description);
            tvLevel = itemView.findViewById(R.id.tv_topic_level);
            btnStartQuiz = itemView.findViewById(R.id.btn_start_quiz);
        }
    }
}
