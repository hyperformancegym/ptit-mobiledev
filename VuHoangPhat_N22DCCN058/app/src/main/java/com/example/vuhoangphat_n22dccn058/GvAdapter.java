package com.example.vuhoangphat_n22dccn058;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GvAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<Food1> foods;

    public GvAdapter (Context context, int layout, List<Food1> foods) {
        this.context = context;
        this.layout = layout;
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int i) {
        return foods.get(i);
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

        ImageView img = view.findViewById(R.id.gvImageFood);
        TextView name = view.findViewById(R.id.gv_name);
        TextView description = view.findViewById(R.id.gv_description);

        Food1 f = foods.get(i);
        img.setImageResource(f.getImgResId());
        name.setText(f.getName());
        description.setText(f.getTime());

        return view;
    }
}