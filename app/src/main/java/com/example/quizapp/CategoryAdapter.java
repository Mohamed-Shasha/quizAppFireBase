package com.example.quizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

import java.util.Base64;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    public  List<CategoryModel> cat_List;

    public CategoryAdapter(List<CategoryModel> cat_List) {
        this.cat_List = cat_List;
    }

    @Override
    public int getCount() {
        return cat_List.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView;

        if (view == null) {
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout, viewGroup, false);
        } else {
            myView = view;
        }
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(view.getContext(),Test.class);
                intent.putExtra("CAT_INDEX",i);
                view.getContext().startActivities(new Intent[]{intent});
            }
        });
        TextView catName = myView.findViewById(R.id.catName);
        TextView numofTest = myView.findViewById(R.id.numOfTests);

        catName.setText(cat_List.get(i).getCategoryName());
        numofTest.setText(String.valueOf(cat_List.get(i).getNumOfTests()));
        return myView;
    }
}
