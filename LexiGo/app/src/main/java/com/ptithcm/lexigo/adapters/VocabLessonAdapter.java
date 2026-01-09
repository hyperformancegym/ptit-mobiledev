package com.ptithcm.lexigo.adapters;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.VocabLesson;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class VocabLessonAdapter extends RecyclerView.Adapter<VocabLessonAdapter.ViewHolder> {
    
    private final Context context;
    private List<VocabLesson> lessons;
    private MediaPlayer mediaPlayer;

    public VocabLessonAdapter(Context context, List<VocabLesson> lessons) {
        this.context = context;
        this.lessons = lessons;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vocab_lesson, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VocabLesson lesson = lessons.get(position);
        
        holder.tvWord.setText(lesson.getWord());
        holder.tvPronunciation.setVisibility(View.GONE);
        holder.tvExample.setVisibility(View.GONE);

        if (!isNullOrEmpty(lesson.getPronunciation())) {
            holder.tvPronunciation.setText(lesson.getPronunciation());
            holder.tvPronunciation.setVisibility(View.VISIBLE);
        }

        holder.tvMeaning.setText(lesson.getMeaning());

        if (!isNullOrEmpty(lesson.getExample())) {
            holder.tvExample.setText(lesson.getExample());
            holder.tvExample.setVisibility(View.VISIBLE);
        }

        // TODO: Load image with Glide or Picasso
        // if (lesson.getImageUrl() != null) {
        //     Glide.with(context).load(lesson.getImageUrl()).into(holder.ivImage);
        // }
        
        holder.btnPlayAudio.setOnClickListener(v -> {
            String audioUrl = lesson.getAudioUrl();
            if (audioUrl == null || audioUrl.isEmpty()) {
                Toast.makeText(context, "Audio unavailable", Toast.LENGTH_SHORT).show();
                return;
            }
            playAudio(audioUrl);
        });
    }
    
    @Override
    public int getItemCount() {
        return lessons.size();
    }
    
    public void updateLessons(List<VocabLesson> newLessons) {
        this.lessons = newLessons != null ? newLessons : Collections.emptyList();
        notifyDataSetChanged();
    }
    
    private void playAudio(String audioUrl) {
        releaseMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build());
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> releaseMediaPlayer());
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(context, "Unable to play audio", Toast.LENGTH_SHORT).show();
                releaseMediaPlayer();
                return true;
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Toast.makeText(context, "Unable to play audio", Toast.LENGTH_SHORT).show();
            releaseMediaPlayer();
        }
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private boolean isNullOrEmpty(@Nullable String value) {
        return value == null || value.trim().isEmpty();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageButton btnPlayAudio;
        TextView tvWord, tvPronunciation, tvMeaning, tvExample;
        
        ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_word_image);
            btnPlayAudio = itemView.findViewById(R.id.btn_play_audio);
            tvWord = itemView.findViewById(R.id.tv_word);
            tvPronunciation = itemView.findViewById(R.id.tv_pronunciation);
            tvMeaning = itemView.findViewById(R.id.tv_meaning);
            tvExample = itemView.findViewById(R.id.tv_example);
        }
    }
}
