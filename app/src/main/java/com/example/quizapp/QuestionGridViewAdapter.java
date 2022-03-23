package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionGridViewAdapter extends BaseAdapter {

    private int numOfQuestions;

    public QuestionGridViewAdapter(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    @Override
    public int getCount() {
        return numOfQuestions;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View myView ;
        if(view==null){
         myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.qustion_grid_view_item,viewGroup,false) ;
        }
        else{
            myView = view;
        }
        TextView quesTV = myView.findViewById(R.id.ques_number);
        quesTV.setText(String.valueOf(i+1));

        switch (DataBase.g_question_list.get(i).getStatus()){

        }
        return myView;
    }
}
