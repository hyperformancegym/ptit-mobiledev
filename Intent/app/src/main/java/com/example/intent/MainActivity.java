package com.example.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.intent.model.SinhVien;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int[] listButtonID = {R.id.btnShowWeb,
        R.id.btnCallSomeone, R.id.btnEditContact, R.id.btnViewContact, R.id.btnSendMessage, R.id.btnSendData};



    public void init(){
        for(int id:listButtonID){
            Button btnTemp = (Button) findViewById(id);
            btnTemp.setOnClickListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();

        if (id==R.id.btnShowWeb){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://vnexpress.net"));
        } else if (id == R.id.btnCallSomeone){
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0708643765"));
        } else if (id == R.id.btnSendMessage){
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("sms:0708643765"));
            intent.putExtra("sms_body", "Xin chào");
        } else if (id == R.id.btnViewContact){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("content://contacts/people/"));
        } else if (id == R.id.btnEditContact){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("content://contacts/people/1"));
        } else if (id == R.id.btnSendData){
            intent = new Intent(MainActivity.this, ShowData.class);

            String[] arrrayCountry = {"Hà Nội", "Đà Nẵng", "TP Hồ Chí Minh"};
            SinhVien sv = new SinhVien("Nguyễn Văn A", 1990, "180 Lê Văn Sỹ, P10, Quận Phú Nhuận");

            Bundle bundle = new Bundle();
            bundle.putString("chuoi", "Lop lap trinh thiet bi di dong");
            bundle.putInt("kieuso", 1234);
            bundle.putStringArray("kieumang", arrrayCountry);
            bundle.putSerializable("kieuObject", sv);

            intent.putExtra("noidung", bundle);

        }

        startActivity(intent);
    }
}