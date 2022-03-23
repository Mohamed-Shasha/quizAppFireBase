package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {


    private List<QuestionModel> questionModelList;

    public QuestionAdapter(List<QuestionModel> questionModelList) {
        this.questionModelList = questionModelList;
    }

    public List<QuestionModel> getQuestionModelList() {
        return questionModelList;
    }

    public void setQuestionModelList(List<QuestionModel> questionModelList) {
        this.questionModelList = questionModelList;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {

        holder.setDate(position);
    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView question;
        private Button optionA;
        private Button optionB;
        private Button optionC;
        private Button optionD;
        public Button prevSelected;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            question = itemView.findViewById(R.id.question_item);
            optionA = itemView.findViewById(R.id.question_item_optionA);
            optionB = itemView.findViewById(R.id.question_item_optionB);
            optionC = itemView.findViewById(R.id.question_item_optionC);
            optionD = itemView.findViewById(R.id.question_item_optionD);

            prevSelected = null;
        }

        private void setDate(int pos) {


            question.setText(questionModelList.get(pos).getQuestion());
            optionA.setText(questionModelList.get(pos).getOptionA());
            optionB.setText(questionModelList.get(pos).getOptionB());
            optionC.setText(questionModelList.get(pos).getOptionC());
            optionD.setText(questionModelList.get(pos).getOptionD());

            setOption(optionA,1,pos);
            setOption(optionB,2,pos);
            setOption(optionC,3,pos);
            setOption(optionD,4,pos);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//
                    selectedOption(optionA, 1, pos);
                }
            });
            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(optionB, 2, pos);
                }
            });
            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(optionC, 3, pos);
                }
            });
            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(optionD, 4, pos);
                }
            });

        }




        private void selectedOption(Button option, int i, int pos) {

//            no option selected yet!
            if (prevSelected == null) {
                option.setBackgroundResource(R.drawable.selected_button);
                DataBase.g_question_list.get(pos).getSelectedAnswer();
                prevSelected = option;
//                option selected
            } else {

//                same option selected then unselect it
                if (prevSelected.getId() == option.getId()) {
                    option.setBackgroundResource(R.drawable.unselected_button);
                    DataBase.g_question_list.get(pos).setSelectedAnswer(-1);
                    prevSelected = null;
//                 change selected to this option from option selected previously
                } else {
                    prevSelected.setBackgroundResource(R.drawable.unselected_button);
                    option.setBackgroundResource(R.drawable.selected_button);
                    DataBase.g_question_list.get(pos).setSelectedAnswer(i);
                    prevSelected = option;
                }

            }

        }
        private void setOption(Button option, int i, int pos) {
           if(DataBase.g_question_list.get(pos).getSelectedAnswer()==i){

               option.setBackgroundResource(R.drawable.selected_button);
           }
           else{
               option.setBackgroundResource(R.drawable.unselected_button);
           }
        }


    }


}
