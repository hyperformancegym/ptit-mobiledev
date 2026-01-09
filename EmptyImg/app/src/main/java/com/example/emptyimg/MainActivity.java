package com.example.emptyimg;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView logoImage;
    private Button btnFacebook, btnTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logoImage   = findViewById(R.id.logoImage);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnTwitter  = findViewById(R.id.btnTwitter);

        btnFacebook.setOnClickListener(v ->
                logoImage.setImageResource(R.drawable.fb)
        );

        btnTwitter.setOnClickListener(v ->
                logoImage.setImageResource(R.drawable.x));
    }
}