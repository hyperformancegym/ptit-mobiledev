package com.example.listviewnangcao;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.listviewnangcao.model.City;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvCity;
    ArrayList<City> cityArrayList = new ArrayList<>();
    CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        lvCity  = findViewById(R.id.lvCity);

        cityArrayList.add(new City("New York", R.drawable.newyork, "https://byvn.net/SQbO"));
        cityArrayList.add(new City("Paris", R.drawable.paris, "https://byvn.net/O3D5"));
        cityArrayList.add(new City("Rome", R.drawable.rome, "https://byvn.net/V4iv"));

        adapter = new CityAdapter(this, R.layout.dong_city, cityArrayList);

        lvCity.setAdapter(adapter);
    }
}