package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.imgRecipe.setImageResource(recipe.getImageRes());
        holder.txtRecipeName.setText(recipe.getName());
        holder.txtCalories.setText(String.format(Locale.getDefault(), "%d Calories", recipe.getCalories()));
        holder.txtNutritionalInfo.setText(recipe.getNutritionalInfo());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRecipe;
        TextView txtRecipeName;
        TextView txtCalories;
        TextView txtNutritionalInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
            txtRecipeName = itemView.findViewById(R.id.txtRecipeName);
            txtCalories = itemView.findViewById(R.id.txtCalories);
            txtNutritionalInfo = itemView.findViewById(R.id.txtNutritionalInfo);
        }
    }
}