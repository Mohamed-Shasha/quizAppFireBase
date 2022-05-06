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

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {


    private List<QuestionModel> questionList;

    public BookmarksAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public BookmarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout, parent, false);
        return new BookmarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.ViewHolder holder, int position) {

        String question = questionList.get(position).getQuestion();
        String a = questionList.get(position).getOptionA();
        String b = questionList.get(position).getOptionB();
        String c = questionList.get(position).getOptionC();
        String d = questionList.get(position).getOptionD();

        int answer = questionList.get(position).getAnswer();
        holder.setDate(position, question, a, b, c, d, answer);
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

        private void setDate(int pos, String question, String a, String b, String c, String d, int correctAnswer) {

            questionNumber.setText("Question " + String.valueOf(pos + 1));
            questionView.setText(question);
            optionA.setText("A) " + a);
            optionB.setText("B) " + b);
            optionC.setText("C) " + c);
            optionD.setText("D) " + d);

            if (correctAnswer == 1) {
                result.setText("Answered: " + a);
            } else if (correctAnswer == 2) {
                result.setText("Answered: " + b);
            } else if (correctAnswer == 3) {
                result.setText("Answered: " + c);
            } else if (correctAnswer == 4) {
                result.setText("Answered: " + d);


            }

        }


    }


}
