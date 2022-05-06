package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Profile extends AppCompatActivity {

    private EditText editName, editEmail, editPhone;
    private LinearLayout editAction;
    private Button saveEdit, cancelEdit;

    private Dialog progressDialog;
    private TextView dialogText;

    private String nameInserted, phoneInserted, emailInserted;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setSupportActionBar(findViewById(R.id.toolbar_profile));
        (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        progressDialog = new Dialog(Profile.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.textViewDialog);
        dialogText.setText("Updating");


        editEmail = findViewById(R.id.profileEditEmail);
        editName = findViewById(R.id.profileEditName);
        editPhone = findViewById(R.id.profileEditPhone);

        editAction = findViewById(R.id.edit_profile_button);

        saveEdit = findViewById(R.id.save_profile_button);
        cancelEdit = findViewById(R.id.cancel_profile_button);

        editingDisabled();


        editAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editingEnabled();
            }
        });

        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidated()){
                    sendDate();
                }
            }
        });
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingDisabled();
            }
        });


    }

    private void sendDate() {

        progressDialog.show();
        DataBase.updateProfileDate(nameInserted,phoneInserted ,new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(Profile.this,"Updating Data Succeed",Toast.LENGTH_SHORT).show();
                editingDisabled();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {

                Toast.makeText(Profile.this,"Updating Data Failed",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private boolean isValidated() {

        nameInserted =editName.getText().toString();
        phoneInserted =editPhone.getText().toString();
        emailInserted =editEmail.getText().toString();
        if (nameInserted.isEmpty()){
            editName.setError("Name cannot be empty");
            return false;
        }
         if (phoneInserted.isEmpty() ){
            editPhone.setError("Phone number is not Valid");
            return false;
        }


            return true;

    }


    private void editingEnabled() {
        saveEdit.setVisibility(View.VISIBLE);
        cancelEdit.setVisibility(View.VISIBLE);
        editName.setEnabled(true);

        editPhone.setEnabled(true);

    }

    private void editingDisabled() {
        editName.setEnabled(false);
        editEmail.setEnabled(false);
        editPhone.setEnabled(false);
        saveEdit.setVisibility(View.INVISIBLE);
        cancelEdit.setVisibility(View.INVISIBLE);
        editName.setText(DataBase.profile.getName());
        editEmail.setText(DataBase.profile.getEmail());

        if (DataBase.profile.getPhoneNumber() != null) {
            editPhone.setText(DataBase.profile.getPhoneNumber());
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Profile.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}