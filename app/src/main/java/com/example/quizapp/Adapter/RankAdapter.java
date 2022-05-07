package com.example.quizapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Model.RankModel;
import com.example.quizapp.R;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<RankModel> userModelList;

    public RankAdapter(List<RankModel> usersList) {
        this.userModelList = usersList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        inflate layout from rank_item_layout layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout, parent, false);

        return new ViewHolder(view);
    }
    // binds the data to each rank item

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        String name = userModelList.get(position).getName();
        int rank = userModelList.get(position).getRank();
        int score = userModelList.get(position).getTotalScore();

        holder.setData(name,score,rank);
    }

    @Override
    public int getItemCount() {

        return userModelList.size();
    }
    // stores and recycles views as they are scrolled off screen

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName, textViewScore, textViewRank, textViewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewImage = itemView.findViewById(R.id.textImageRV);
            textViewName = itemView.findViewById(R.id.nameRV);
            textViewScore = itemView.findViewById(R.id.scoreRV);
            textViewRank = itemView.findViewById(R.id.rankRV);
        }

        private void setData(String name, int score, int rank) {
            textViewName.setText(name);
            textViewImage.setText(name.toUpperCase().substring(0, 1));
            textViewScore.setText("Score: " + score);
            textViewRank.setText("Rank: " + rank);
        }
    }
}
