package com.example.quizapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.quizapp.Adapter.RankAdapter;
import com.example.quizapp.databinding.ActivityMainBinding;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private LinearLayout logoutButton, profile, savedAnswers;
    private TextView name, score, rank, initial, classCode;

    private Dialog progressDialog;
    private TextView dialogText;
    private RankAdapter rankAdapter;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

//        attributes initialization

//
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Account");

String s = " ";
s.toUpperCase();
        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("loading");

        logoutButton = view.findViewById(R.id.logoutView);
        profile = view.findViewById(R.id.profileView);
        savedAnswers = view.findViewById(R.id.bookmarkedView);

        name = view.findViewById(R.id.nameView);
        score = view.findViewById(R.id.totalscoreView);
        rank = view.findViewById(R.id.rankView);
        classCode = view.findViewById(R.id.classView);
        initial = view.findViewById(R.id.intialView);
//
        String userName = DataBase.profile.getName();
        initial.setText(userName.substring(0, 1));
        name.setText(userName);



        if (DataBase.usersList.size() == 0) {
            progressDialog.show();

            DataBase.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    if (DataBase.performance.getTotalScore() != 0) {
                        if (!DataBase.inTopList) {
                            myRankCal();
                        }
                        score.setText(""+DataBase.performance.getTotalScore());
                        rank.setText(""+DataBase.performance.getRank());
                        classCode.setText("Class Code:"+DataBase.profile.getClassCode());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure() {

                    Toast.makeText(getContext(), "error fetch data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });


        }
        else{

            progressDialog.dismiss();
            score.setText(""+DataBase.performance.getTotalScore());
            rank.setText(""+DataBase.performance.getRank());
            classCode.setText("Class Code:"+DataBase.profile.getClassCode());
        }


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignIn.getClient(
                        getContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();
                Intent i = new Intent(getContext(), LoginPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                getActivity().finish();


//                });
            }
        });

        savedAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), Profile.class);
                startActivity(i);
            }
        });


        return view;


    }

    private void myRankCal() {
        int lowestTopScore = DataBase.usersList.get(DataBase.usersList.size() - 1).getTotalScore();
//        check rank in remaining users / not in top 20
        int remaining = DataBase.usersTotal - 20;
//        calculate user location in the list
        int mySlot = (DataBase.performance.getTotalScore() * remaining) / lowestTopScore;
        int rank;
//        where not 21 rank ;get rank by subtracting user location from total
        if (lowestTopScore != DataBase.performance.getTotalScore()) {
            rank = DataBase.usersTotal - mySlot;
        } else {
            rank = 21;
        }
        DataBase.performance.setRank(rank);


    }
}