package com.example.activityforresult;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int[] listButtonID = { R.id.btnChangeBottom, R.id.btnChangeTop};
    TextView tvTop, tvBottom;
    public static final int REQUEST_CODE_TOP = 999;
    public static final int REQUEST_CODE_BOTTOM=998;
    public static final int RESULT_CODE_UPPER = 100;
    public static final int RESULT_CODE_LOWER= 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
    }
    public void init(){
        tvTop = findViewById(R.id.txtTittleTop);
        tvBottom= findViewById(R.id.txtTittleBottom);

        for(int id:listButtonID) {
            Button btnTemp = (Button) findViewById(id);
            btnTemp.setOnClickListener(this);
        }
    }
    public void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnChangeBottom:
                Intent intentChangeBottom = new Intent(MainActivity.this, ResultActivity.class);
                startActivityForResult(intentChangeBottom,REQUEST_CODE_BOTTOM);
                break;
            case R.id.btnChangeTop:
                Intent intentChangeTop = new Intent(MainActivity.this, ResultActivity.class);
                startActivityForResult(intentChangeTop, REQUEST_CODE_TOP);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = data.getStringExtra("yourTitle");
        if(requestCode == REQUEST_CODE_TOP) {
            if(resultCode == RESULT_CODE_UPPER) tvTop.setText(title.toUpperCase());
            if(resultCode == RESULT_CODE_LOWER) tvTop.setText(title.toLowerCase());
//showToast("TOP");
        }
        if(requestCode == REQUEST_CODE_BOTTOM) {
//showToast("BOTTOM");
            if(resultCode == RESULT_CODE_UPPER) tvBottom.setText(title.toUpperCase());
            if(resultCode == RESULT_CODE_LOWER) tvBottom.setText(title.toLowerCase());
        }
    }
}