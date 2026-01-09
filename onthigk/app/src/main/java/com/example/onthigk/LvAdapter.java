package com.example.onthigk;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LvAdapter extends BaseAdapter {
    private Context context;
    private List<Food1> listFood1;
    private int layout;

    public LvAdapter(Context context, List<Food1> listFood1, int layout) {
        this.context = context;
        this.listFood1 = listFood1;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return listFood1.size();
    }

    @Override
    public Object getItem(int position) {
        return listFood1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.lv_item, null);
        Log.d("dasda","dsasdas");
        ImageView imgView = convertView.findViewById(R.id.imageView);
        TextView txtView = convertView.findViewById((R.id.textView));
        TextView txtView2 = convertView.findViewById((R.id.textView2));
        TextView txtView3 = convertView.findViewById((R.id.textView3));

        Food1 food1 = listFood1.get(position);
        imgView.setImageResource(food1.getImgResId());
        txtView.setText(food1.getName());
        txtView2.setText(food1.getDescription());
        txtView3.setText(Double.toString(food1.getPrice()));

        return convertView;
    }
}
