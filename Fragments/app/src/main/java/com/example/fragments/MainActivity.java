package com.example.fragments;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements FragmentB.Counter {
    FragmentA fragmentA;
    FragmentB fragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        fragmentA = new FragmentA();
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.main, fragmentA);
//        transaction.commit();
//
//        fragmentB = new FragmentB();
//        getSupportFragmentManager().beginTransaction().add(R.id.main, fragmentB, "MyFragment").commit();
    }

    @Override
    public void incrementValue(int count) {
        FragmentB fragmentB;
        fragmentB = (FragmentB) getSupportFragmentManager().findFragmentById(R.id.fragmentB);
        if(fragmentB != null) {
            fragmentB.setTheCount(count);
        }
    }


}