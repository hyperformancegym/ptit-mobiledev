package com.example.vuhoangphat_n22dccn058;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class FragmentTab1 extends Fragment {

    private GridView gvFood;
    private ArrayList<Food1> foodList;

    public FragmentTab1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        gvFood = view.findViewById(R.id.gvFood);

        foodList = new ArrayList<>();
        foodList.add(new Food1(R.drawable.goicuon, "Gỏi Cuốn", "30 mins"));
        foodList.add(new Food1(R.drawable.lau, "Lẩu", "25 mins"));
        foodList.add(new Food1(R.drawable.pho, "Phở", "40 mins"));
        foodList.add(new Food1(R.drawable.tokbokki, "Tokbokki", "45 mins"));
        foodList.add(new Food1(R.drawable.goicuon, "Gỏi Cuốn 2", "35 mins"));
        foodList.add(new Food1(R.drawable.lau, "Lẩu Thái", "20 mins"));

        GvAdapter adapter = new GvAdapter(getContext(), R.layout.grid_view, foodList);
        gvFood.setAdapter(adapter);

        return view;
    }
}