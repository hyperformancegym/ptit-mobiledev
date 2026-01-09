package com.example.vuhoangphat_n22dccn058;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentTab2 extends Fragment {

    private ListView lvFood;
    private ArrayList<Food2> foodList;

    public FragmentTab2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        lvFood = view.findViewById(R.id.lv);

        foodList = new ArrayList<>();
        foodList.add(new Food2(R.drawable.pho, "Phở Bò", "Món Nước", 12.50));
        foodList.add(new Food2(R.drawable.goicuon, "Gỏi Cuốn Tôm Thịt", "Món Cuốn", 9.75));
        foodList.add(new Food2(R.drawable.tokbokki, "Tokbokki Cay", "Hàn Quốc", 25.00));
        foodList.add(new Food2(R.drawable.lau, "Lẩu Hải Sản", "Món Nước", 15.25));
        foodList.add(new Food2(R.drawable.pho, "Phở Gà", "Món Nước", 10.00));
        foodList.add(new Food2(R.drawable.goicuon, "Gỏi Cuốn Chay", "Món Cuốn", 8.99));

        LvAdapter adapter = new LvAdapter(getContext(), R.layout.list_view, foodList);
        lvFood.setAdapter(adapter);

        return view;
    }
}