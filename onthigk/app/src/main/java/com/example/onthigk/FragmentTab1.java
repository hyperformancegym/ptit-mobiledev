package com.example.onthigk;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTab1 extends Fragment {
    List<Food1> food1List;
    LvAdapter lvAdapter;
    ListView lstView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTab1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTab1 newInstance(String param1, String param2) {
        FragmentTab1 fragment = new FragmentTab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab1, container, false);
        lstView = v.findViewById(R.id.lstView);
        food1List = new ArrayList<>();
        food1List.add(new Food1("a", "a", 1.1, R.drawable.ic_launcher_background));
        food1List.add(new Food1("a", "a", 1.1, R.drawable.ic_launcher_background));
        food1List.add(new Food1("a", "a", 1.1, R.drawable.ic_launcher_background));
        food1List.add(new Food1("a", "a", 1.1, R.drawable.ic_launcher_background));
        food1List.add(new Food1("a", "a", 1.1, R.drawable.ic_launcher_background));

        lvAdapter = new LvAdapter(this.getContext(), food1List, R.layout.lv_item);
        lstView.setAdapter(lvAdapter);
        lvAdapter.notifyDataSetChanged();

        return v;
    }
}