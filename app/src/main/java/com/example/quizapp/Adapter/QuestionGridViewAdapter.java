package com.example.quizapp.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static com.example.quizapp.DataBase.*;

import androidx.core.content.ContextCompat;

import com.example.quizapp.Questions;
import com.example.quizapp.R;

public class QuestionGridViewAdapter extends BaseAdapter {

    private int numOfQuestions;
    private  Context context;

    public QuestionGridViewAdapter(Context context, int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
        this.context = context;
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

        View myView;
        if (view == null) {
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.qustion_grid_view_item, viewGroup, false);
        } else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(context instanceof Questions){
                    ((Questions) context).sendToQuestion(i);
                }
            }
        });
        TextView quesTV = myView.findViewById(R.id.ques_number);
        quesTV.setText(String.valueOf(i + 1));

        switch (g_question_list.get(i).getStatus()) {
            case NOT_VISITED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.not_visited)));
                break;
            case ANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.answered)));
                break;
            case UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.not_answered)));
                break;
            case REVIEW:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.review)));



        }


        return myView;
    }
}
