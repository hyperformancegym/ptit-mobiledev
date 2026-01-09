package com.example.fragments;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity2 extends AppCompatActivity {

    FragmentB fragmentB;
    FragmentA fragmentA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        fragmentA = new FragmentA();
        fragmentB = new FragmentB();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentA, fragmentA);
        transaction.add(R.id.fragmentB, fragmentB);
        transaction.commit();

    }

    public void incrementValue(int count) {
        FragmentB fragmentB;
        fragmentB = (FragmentB) getSupportFragmentManager().findFragmentById(R.id.fragmentB);
        if(fragmentB != null) {
            fragmentB.setTheCount(count);
        }
    }
}