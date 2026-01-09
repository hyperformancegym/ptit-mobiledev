package com.example.listviewnangcao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listviewnangcao.model.City;

import java.util.List;

public class CityAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<City> cityList;

    public CityAdapter(Context context, int layout, List<City> cityList) {
        this.context = context;
        this.layout = layout;
        this.cityList = cityList;
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

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView  = inflater.inflate(layout,null);

        TextView txtTen = convertView.findViewById(R.id.txtTen);
        TextView txtLink = convertView.findViewById(R.id.txtLink);
        ImageView imgHinh = convertView.findViewById(R.id.imgHinh);

        City city = cityList.get(position);
        txtTen.setText(city.getNameCity());
        txtLink.setText(city.getLinkWiki());
        imgHinh.setImageResource(city.getHinh());

        return  convertView;
    }
}
