package com.example.gridviewadvanced;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.gridviewadvanced.model.HinhAnh;

import java.util.List;
import java.util.Objects;

public class HinhAnhAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<HinhAnh> hinhAnhList;

    public HinhAnhAdapter(Context context, int layout, List<HinhAnh> hinhAnhList){
        this.context = context;
        this.layout = layout;
        this.hinhAnhList = hinhAnhList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public List<HinhAnh> getHinhAnhList() {
        return hinhAnhList;
    }

    public void setHinhAnhList(List<HinhAnh> hinhAnhList) {
        this.hinhAnhList = hinhAnhList;
    }

    @Override
    public int getCount(){
        return hinhAnhList.size();
    }

    @Override
    public Object getItem(int position){
        return hinhAnhList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        ImageView imageView = view.findViewById(R.id.imgHinhAnh);
        TextView imgText = view.findViewById(R.id.imgText);

        HinhAnh hinhAnh = hinhAnhList.get(i);
        imageView.setImageResource(hinhAnh.getHinh());
        imgText.setText(hinhAnh.getTen());

        return view;
    }
}
