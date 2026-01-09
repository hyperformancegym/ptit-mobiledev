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
import com.ptithcm.lexigo.models.LearningCategory;

import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách các mục học tập
 */
public class LearningCategoryAdapter extends RecyclerView.Adapter<LearningCategoryAdapter.ViewHolder> {

    private Context context;
    private List<LearningCategory> categories;
    private OnItemClickListener listener;

    // Interface cho sự kiện click
    public interface OnItemClickListener {
        void onItemClick(LearningCategory category);
    }

    private static final int TYPE_GRID = 0;
    private static final int TYPE_LIST = 1;

    public LearningCategoryAdapter(Context context, List<LearningCategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        // First 4 items are grid, others are list
        return position < 4 ? TYPE_GRID : TYPE_LIST;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == TYPE_GRID) ? R.layout.item_learning_category_grid : R.layout.item_learning_category;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LearningCategory category = categories.get(position);

        // Gán dữ liệu vào views
        holder.tvTitle.setText(category.getTitle());
        holder.tvDescription.setText(category.getDescription());
        holder.imgIcon.setImageResource(category.getIconResId());

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * ViewHolder cho item trong RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTitle;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgCategoryIcon);
            tvTitle = itemView.findViewById(R.id.tvCategoryTitle);
            tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
        }
    }
}

