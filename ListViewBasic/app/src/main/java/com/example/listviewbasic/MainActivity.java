package com.example.listviewbasic;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> arrayMonhoc;
    ArrayAdapter adapter;
    EditText editText;
    Button btnThem, btnCapNhat;
    ListView lsview;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        pos=-1;
        arrayMonhoc = new ArrayList<>();
        arrayMonhoc.add("Android");
        arrayMonhoc.add("Java");
        arrayMonhoc.add("PHP");
        arrayMonhoc.add("Hadoop");
        arrayMonhoc.add("Sap");
        arrayMonhoc.add("Python");
        arrayMonhoc.add("Ajax");
        arrayMonhoc.add("C++");
        arrayMonhoc.add("Ruby");
        arrayMonhoc.add("Rails");

        lsview = findViewById(R.id.lsView);
        editText = findViewById(R.id.editText);
        btnThem = findViewById(R.id.btnThem);
        btnCapNhat = findViewById(R.id.btnCapNhat);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayMonhoc);
        lsview.setAdapter(adapter);

        lsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = arrayMonhoc.get(position);
                editText.setText(s);
                pos=position;
            }
        });

        lsview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Xóa môn học: "+arrayMonhoc.get(position), Toast.LENGTH_SHORT).show();


                String s = arrayMonhoc.get(position);
                Toast.makeText(MainActivity.this, "Xóa môn học: "+s, Toast.LENGTH_SHORT).show();
                arrayMonhoc.remove(position);
                adapter.notifyDataSetChanged();
                pos=-1;
                return false;
            }
        });
    }

    public void updateItem(View view) {

        if(pos!=-1){
            String s = editText.getText().toString();
            arrayMonhoc.set(pos, s);
            adapter.notifyDataSetChanged();
            editText.setText("");
        }
        pos=-1;
        editText.setText("");

    }

    public void addItem(View view) {
        String s = editText.getText().toString();
        arrayMonhoc.add(s);
        adapter.notifyDataSetChanged();
        editText.setText("");
    }
}