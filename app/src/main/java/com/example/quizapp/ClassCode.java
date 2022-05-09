package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ClassCode extends AppCompatActivity {

    EditText classCode;


    Button enter;
    ProgressBar progressBar;
    private Dialog progressDialog;
    private TextView dialogText;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        classCode = findViewById(R.id.editTextClassCode);

        enter = findViewById(R.id.buttonEnterClassCode);

//        progress dialog added
        progressDialog = new Dialog(ClassCode.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("Class Code Verification process");

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase.updateClassCode(ClassCode.this,classCode.getText().toString(), new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                        Intent i = new Intent(ClassCode.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();
                        Toast.makeText(ClassCode.this, "error fetch data", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }


}