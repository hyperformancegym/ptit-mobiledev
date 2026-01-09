package com.ptithcm.lexigo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.lexigo.R;
import com.ptithcm.lexigo.api.models.GrammarLesson;

import java.util.List;

public class GrammarLessonAdapter extends RecyclerView.Adapter<GrammarLessonAdapter.ViewHolder> {
    
    private Context context;
    private List<GrammarLesson> lessons;
    private OnLessonClickListener listener;
    
    public interface OnLessonClickListener {
        void onLessonClick(GrammarLesson lesson);
    }
    
    public GrammarLessonAdapter(Context context, List<GrammarLesson> lessons, OnLessonClickListener listener) {
        this.context = context;
        this.lessons = lessons;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grammar_lesson, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrammarLesson lesson = lessons.get(position);

        holder.tvTitle.setText(lesson.getTitle());

        // Use explanation if description is not available
        String description = lesson.getDescription();
        if (description == null || description.isEmpty()) {
            description = lesson.getExplanation();
        }
        holder.tvDescription.setText(description);

        holder.tvLevel.setText(lesson.getLevel());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLessonClick(lesson);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return lessons.size();
    }
    
    public void updateLessons(List<GrammarLesson> newLessons) {
        this.lessons = newLessons;
        notifyDataSetChanged();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvLevel;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_lesson_title);
            tvDescription = itemView.findViewById(R.id.tv_lesson_description);
            tvLevel = itemView.findViewById(R.id.tv_lesson_level);
        }
    }
}
