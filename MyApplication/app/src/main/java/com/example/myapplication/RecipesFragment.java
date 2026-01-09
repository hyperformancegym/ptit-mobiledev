package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        RecyclerView rvRecipes = view.findViewById(R.id.rvRecipes);
        rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        List<Recipe> recipeItems = new ArrayList<>();
        // Sample Data - will replace R.drawable.celery later
        recipeItems.add(new Recipe("Celery Smashers", 471, "Protein: 10g, Carbs: 50g, Fat: 20g", R.drawable.celery));
        recipeItems.add(new Recipe("Spicy Noodles", 320, "Protein: 15g, Carbs: 40g, Fat: 10g", R.drawable.food1));
        recipeItems.add(new Recipe("Green Salad", 150, "Protein: 5g, Carbs: 20g, Fat: 5g", R.drawable.food2));
        recipeItems.add(new Recipe("Chicken Delight", 500, "Protein: 30g, Carbs: 30g, Fat: 25g", R.drawable.food3));

        RecipeAdapter adapter = new RecipeAdapter(recipeItems);
        rvRecipes.setAdapter(adapter);

        return view;
    }
}