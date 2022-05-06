package com.example.quizapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Model.QuestionModel;
import com.example.quizapp.R;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {


    private List<QuestionModel> questionList;

    public AnswersAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout, parent, false);
        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {

        String question = questionList.get(position).getQuestion();
        String a = questionList.get(position).getOptionA();
        String b = questionList.get(position).getOptionB();
        String c = questionList.get(position).getOptionC();
        String d = questionList.get(position).getOptionD();
        int answerSelected = questionList.get(position).getSelectedAnswer();
        int answer = questionList.get(position).getAnswer();
        holder.setDate(position, question, a, b, c, d, answerSelected, answer);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView questionNumber, questionView, optionA, optionB, optionC, optionD, result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.questionNumber);
            questionView = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result = itemView.findViewById(R.id.result);

        }

        private void setDate(int pos, String question, String a, String b, String c, String d, int answerSelected, int correctAnswer) {

            questionNumber.setText("Question " + String.valueOf(pos+1));
            questionView.setText(question);
            optionA.setText("A) "+a);
            optionB.setText("B) "+b);
            optionC.setText("C) "+c);
            optionD.setText("D) "+d);
            if (answerSelected == -1) {
                result.setText("not answered");
                result.setTextColor(itemView.getContext().getResources().getColor(R.color.not_visited));
                setSelectedOptionColor(answerSelected,R.color.black);
            } else {
                if (answerSelected == correctAnswer) {
                    result.setText("correct");
                    result.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                    setSelectedOptionColor(answerSelected, android.R.color.holo_green_dark);
                } else {
                    result.setText("wrong");
                    result.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                    setSelectedOptionColor(answerSelected, android.R.color.holo_red_dark);
                }

            }
        }

        private void setSelectedOptionColor(int selected, int color) {
            if(selected==1){
                optionA.setTextColor(itemView.getContext().getResources().getColor(color));
            }

           else{
                optionA.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
            }
            if(selected==2){
                optionB.setTextColor(itemView.getContext().getResources().getColor(color));
            }
            else{
                optionB.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
            }
            if(selected==3){
                optionC.setTextColor(itemView.getContext().getResources().getColor(color));
            }
            else{
                optionC.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
            }

            if(selected==4){
                optionD.setTextColor(itemView.getContext().getResources().getColor(color));
            }
            else{
                optionD.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
            }




        }
    }


}
