package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    private TextInputEditText edtUsername;
    private FloatingActionButton fabEditPhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        edtUsername = view.findViewById(R.id.edtUsername);
        fabEditPhoto = view.findViewById(R.id.fabEditPhoto);

        // Setup toolbar menu
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.actionSave) {
                saveProfile();
                return true;
            }
            return false;
        });

        // Setup navigation icon (back button)
        toolbar.setNavigationOnClickListener(v -> {
            Toast.makeText(getContext(), "Back pressed", Toast.LENGTH_SHORT).show();
            // In a real app, you might navigate back or perform some action
        });

        fabEditPhoto.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Open Image Picker", Toast.LENGTH_SHORT).show();
            // Implement image picker logic here
        });

        // Set initial data (placeholder)
        edtUsername.setText("John Doe");

        return view;
    }

    private void saveProfile() {
        String username = edtUsername.getText().toString().trim();

        if (username.isEmpty()) {
            edtUsername.setError("Username cannot be empty");
            Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Profile saved: " + username, Toast.LENGTH_SHORT).show();
            // Save logic here (e.g., to SharedPreferences, database, etc.)
        }
    }
}