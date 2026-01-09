package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.imgFood.setImageResource(food.getImageRes());
        holder.txtName.setText(food.getName());
        holder.txtDesc.setText(food.getDesc());
        holder.txtPrice.setText(String.format(Locale.getDefault(), "$%.2f", (double) food.getPrice()));

        // Set initial quantity
        holder.txtQuantity.setText(String.valueOf(0)); // Start with 0 quantity

        holder.btnPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.txtQuantity.getText().toString());
            currentQuantity++;
            holder.txtQuantity.setText(String.valueOf(currentQuantity));
            Toast.makeText(v.getContext(), "Added " + food.getName(), Toast.LENGTH_SHORT).show();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.txtQuantity.getText().toString());
            if (currentQuantity > 0) {
                currentQuantity--;
                holder.txtQuantity.setText(String.valueOf(currentQuantity));
                Toast.makeText(v.getContext(), "Removed " + food.getName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Cannot remove less than 0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView txtName;
        TextView txtDesc;
        TextView txtPrice;
        ImageButton btnPlus;
        ImageButton btnMinus;
        TextView txtQuantity; // Added quantity TextView

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            txtName = itemView.findViewById(R.id.txtName);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            txtQuantity = itemView.findViewById(R.id.txtQuantity); // Initialize quantity TextView
        }
    }
}