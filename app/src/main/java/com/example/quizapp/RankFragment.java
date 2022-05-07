package com.example.quizapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.quizapp.Adapter.RankAdapter;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {

    private TextView totalUsers, imageTextInitial, imageTextName, score, rank;
    private RecyclerView usersView;
    private Dialog progressDialog;
    private TextView dialogText;
    private RankAdapter rankAdapter;

    public RankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_stats, container, false);


        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Rank");
//        initialize fields
        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("loading");
        progressDialog.show();

        totalUsers = view.findViewById(R.id.totalUsers);
        imageTextInitial = view.findViewById(R.id.statsInitialImageText);
        imageTextName = view.findViewById(R.id.statsImageText);
        score = view.findViewById(R.id.statsScoreImageText);
        rank = view.findViewById(R.id.statsRankImageText);
        usersView = view.findViewById(R.id.userView);

//       get and  set the layout manager of the layout from the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

//        pass the list to the rank adapter
        rankAdapter = new RankAdapter(DataBase.usersList);

//        set class view to adapter
        usersView.setAdapter(rankAdapter);
        progressDialog.show();

//        get top 20 users form Database
        DataBase.getTopUsers(new MyCompleteListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess() {
//                listen to any changes
                rankAdapter.notifyDataSetChanged();

                if (DataBase.performance.getTotalScore() != 0) {
//                    user not in top 20
                    if (!DataBase.inTopList) {
//                        calculate rank in remaining
                        myRankCal();
                    }
                    score.setText("Score" + DataBase.performance.getTotalScore());
                    rank.setText("Rank" + DataBase.performance.getRank());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
//show error
                Toast.makeText(getContext(), "error fetch data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
//        set total users and initial letter
        totalUsers.setText("Total Users :" + DataBase.usersTotal);
        imageTextInitial.setText(DataBase.performance.getName().toUpperCase().substring(0, 1));


        return view;
    }
// calculating rank
    private void myRankCal() {
        int lowestTopScore = DataBase.usersList.get(DataBase.usersList.size() - 1).getTotalScore();
//        check rank in remaining users / not in top 20
        int remaining = DataBase.usersTotal - 20;
//     calculate user slot by multiplying score by number od users out of top 20 and divide it by the lowest score of top 20 users
        int mySlot = (DataBase.performance.getTotalScore() * remaining) / lowestTopScore;
        int rank;
//        users score is not the lowest in top 20
        if (lowestTopScore != DataBase.performance.getTotalScore()) {
            rank = DataBase.usersTotal - mySlot;
        } else {
            rank = 21;
        }
//        set user rank in the List
        DataBase.performance.setRank(rank);


    }
}