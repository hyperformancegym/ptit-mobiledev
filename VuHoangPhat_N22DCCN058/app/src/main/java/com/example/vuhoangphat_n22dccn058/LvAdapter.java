package com.example.vuhoangphat_n22dccn058;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;

import java.util.List;

public class LvAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Food2> foodList;

    public LvAdapter (Context context, int layout, List<Food2> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        TextView txtName = view.findViewById(R.id.food_name);
        TextView txtDescription = view.findViewById(R.id.food_type);
        TextView txtPrice = view.findViewById(R.id.food_price);
        ImageView imgFood = view.findViewById(R.id.imageView);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        Food2 foodItem = foodList.get(i);
        txtName.setText(foodItem.getName());
        txtDescription.setText(foodItem.getType());
        txtPrice.setText("$" + String.format("%.2f", foodItem.getPrice()));
        imgFood.setImageResource(foodItem.getImgResId());

        return view;
    }
}