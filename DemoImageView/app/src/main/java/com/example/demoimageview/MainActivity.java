package com.example.demoimageview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {
    TextView textView;
    ImageView imageView2;
    private int[] listButtonID = { R.id.button3, R.id.button4,R.id.button5,
            R.id.button6,R.id.button7,R.id.button8,R.id.button9};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                            systemBars.bottom);
                    return insets;
                });
        init();
    }
    public void init()
    {
        textView = findViewById(R.id.textView);
        imageView2 = findViewById(R.id.imageView2);
        imageView2.setImageResource(R.drawable.iphone17);
        for(int id:listButtonID) {
            Button btnTemp = (Button) findViewById(id);
            btnTemp.setOnClickListener(this);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button3:
                imageView2.setScaleType(ImageView.ScaleType.CENTER);
                textView.setText("Center Style");

                break;
            case R.id.button4:
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                textView.setText("Center Crop Style");
                break;
            case R.id.button5:
                imageView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                textView.setText("Center Inside Style");
                break;
            case R.id.button6:
                imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
                textView.setText("Center Center Style");
                break;
            case R.id.button7:
                imageView2.setScaleType(ImageView.ScaleType.FIT_END);
                textView.setText("Center FitEnd Style");
                break;
            case R.id.button8:
                imageView2.setScaleType(ImageView.ScaleType.FIT_START);
                textView.setText("Center FitStart Style");
                break;
            case R.id.button9:
                imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
                textView.setText("Center FitXY Style");
                break;
        }
    }
}