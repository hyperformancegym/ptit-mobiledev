package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        RecyclerView rvMenu = view.findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Food> menuItems = new ArrayList<>();
        // Sample Data - will replace R.drawable.food1 later
        menuItems.add(new Food("Item 1", "Information of Item 1", 6, R.drawable.food1));
        menuItems.add(new Food("Item 2", "Information of Item 2", 8, R.drawable.food2));
        menuItems.add(new Food("Item 3", "Information of Item 3", 12, R.drawable.food3));

        FoodAdapter adapter = new FoodAdapter(menuItems);
        rvMenu.setAdapter(adapter);

        return view;
    }
}