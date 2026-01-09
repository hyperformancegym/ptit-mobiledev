package com.ptithcm.lexigo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.GrammarExercise;

import java.util.List;

public class GrammarExerciseAdapter extends RecyclerView.Adapter<GrammarExerciseAdapter.ViewHolder> {
    
    private Context context;
    private List<GrammarExercise> exercises;
    
    public GrammarExerciseAdapter(Context context, List<GrammarExercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grammar_exercise, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrammarExercise exercise = exercises.get(position);
        
        holder.tvQuestion.setText((position + 1) + ". " + exercise.getQuestion());
        holder.tvExerciseType.setText("Type: " + exercise.getExerciseType());
        holder.tvDifficulty.setText("Difficulty: " + exercise.getDifficulty());
        
        if (exercise.getOptions() != null && !exercise.getOptions().isEmpty()) {
            StringBuilder optionsText = new StringBuilder();
            for (int i = 0; i < exercise.getOptions().size(); i++) {
                char optionLabel = (char) ('A' + i);
                optionsText.append(optionLabel).append(". ").append(exercise.getOptions().get(i)).append("\n");
            }
            holder.tvOptions.setText(optionsText.toString());
            holder.tvOptions.setVisibility(View.VISIBLE);
        } else {
            holder.tvOptions.setVisibility(View.GONE);
        }
        
        // Initially hide answer and explanation
        holder.tvCorrectAnswer.setVisibility(View.GONE);
        holder.tvExplanation.setVisibility(View.GONE);
        
        // Show answer when clicked
        holder.itemView.setOnClickListener(v -> {
            if (holder.tvCorrectAnswer.getVisibility() == View.GONE) {
                holder.tvCorrectAnswer.setText("Answer: " + exercise.getCorrectAnswer());
                holder.tvCorrectAnswer.setVisibility(View.VISIBLE);
                
                if (exercise.getExplanation() != null) {
                    holder.tvExplanation.setText("Explanation: " + exercise.getExplanation());
                    holder.tvExplanation.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tvCorrectAnswer.setVisibility(View.GONE);
                holder.tvExplanation.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return exercises.size();
    }
    
    public void updateExercises(List<GrammarExercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvExerciseType, tvDifficulty, tvOptions, tvCorrectAnswer, tvExplanation;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_exercise_question);
            tvExerciseType = itemView.findViewById(R.id.tv_exercise_type);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            tvOptions = itemView.findViewById(R.id.tv_options);
            tvCorrectAnswer = itemView.findViewById(R.id.tv_correct_answer);
            tvExplanation = itemView.findViewById(R.id.tv_explanation);
        }
    }
}
