package com.example.intent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intent.model.SinhVien;

public class ShowData extends AppCompatActivity {
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        txtInfo = findViewById(R.id.txtShowInfo);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("noidung");
        if(bundle!=null){
            String chuoi = bundle.getString("chuoi");
            int kieuso = bundle.getInt("kieuso", 123);
            String[] kieuMang = bundle.getStringArray("kieumang");
            SinhVien sv = (SinhVien) bundle.getSerializable("kieuObject");
            txtInfo.setText(chuoi+"\n"+kieuso+"\n"+kieuMang[2]+"\n"+sv.getHoTen());
        }
    }
}
