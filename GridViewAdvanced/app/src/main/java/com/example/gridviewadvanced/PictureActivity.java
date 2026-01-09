package com.example.gridviewadvanced;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gridviewadvanced.model.HinhAnh;

public class PictureActivity extends AppCompatActivity {
    ImageView imageView;
    Button btnBack;
    TextView txtView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imageView = findViewById(R.id.imageView);
        btnBack = findViewById(R.id.button);
        txtView = findViewById(R.id.txtView);

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("data");
        HinhAnh hinhAnh = (HinhAnh)bundle.getSerializable("ha");
        imageView.setImageResource(hinhAnh.getHinh());
        txtView.setText(hinhAnh.getTen());
    }

    public void BackActivity(View view){
        finish();
    }
}
