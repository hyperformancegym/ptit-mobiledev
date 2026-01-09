package com.example.activitylifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Log.e("AAA","On Create Main Activity");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("AAA", "Start Main Activity");
//Toast.makeText(this,"onStart",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("AAA", "On Restart Main Activity");
//Toast.makeText(this,"onRestart",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("AAA", "onResume Main Activity");
//Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.e("AAA", "onPause Main Activity");
//Toast.makeText(this,"onPause",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("AAA", "onStop Main Activity");
//Toast.makeText(this,"onStop",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("AAA", "onDestroy Main Activity");
//Toast.makeText(this,"onDestroy",Toast.LENGTH_LONG).show();
    }
    public void ToActivity2(View view) {
        Intent intent = new Intent(MainActivity.this, Activity2.class);
        startActivity(intent);

    }

    public void CloseMainActivity(View view) { finish();
    }
}