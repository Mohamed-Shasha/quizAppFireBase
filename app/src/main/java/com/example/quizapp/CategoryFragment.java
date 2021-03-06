package com.example.quizapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.quizapp.Adapter.CategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {



    public CategoryFragment() {
        // Required empty public constructor
    }

    private GridView categoryView;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        categoryView = view.findViewById(R.id.cat_grid);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Categories");

        CategoryAdapter adapter = new CategoryAdapter(DataBase.g_cat_List);
        categoryView.setAdapter(adapter);
        return view;
    }



}