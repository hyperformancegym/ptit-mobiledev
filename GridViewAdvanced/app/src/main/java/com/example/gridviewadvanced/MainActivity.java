package com.example.gridviewadvanced;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gridviewadvanced.model.HinhAnh;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<HinhAnh> arrayHinhAnh;
    GridView gridView;
    HinhAnhAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gvHinhAnh);

        arrayHinhAnh = new ArrayList<>();
        arrayHinhAnh.add(new HinhAnh(R.drawable.android1, "Hình số 1"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android2, "Hình số 2"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android3, "Hình số 3"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android4, "Hình số 4"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android5, "Hình số 5"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android6, "Hình số 6"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android7, "Hình số 7"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android8, "Hình số 8"));
        arrayHinhAnh.add(new HinhAnh(R.drawable.android9, "Hình số 9"));

        adapter = new HinhAnhAdapter(this, R.layout.dong_hinh_anh, arrayHinhAnh);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HinhAnh hinhAnh = arrayHinhAnh.get(position);

                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ha", hinhAnh);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }
}