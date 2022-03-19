package com.example.quizapp;


import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    // Access a Cloud Firestore instance from your Activity
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static FirebaseFirestore db;

    static void createUser(String email, String name, MyCompleteListener completeListener) {
        // Create a new user with a first and last name
        Map<String, Object> userData = new HashMap<>();
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);

        // multiple writes in single atomic
        WriteBatch batch = db.batch();

        //get user id from firebase authentication table
        DocumentReference userDocReference = db.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        batch.set(userDocReference, userData);

        DocumentReference countDocReference = db.collection("USERS").document("TOTAL_USERS");
        batch.update(countDocReference, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();
                    }
                });
    }
//    load categories from fire_store Database

    public static void loadCategories() {
        categoryModelList.clear();
        db.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            docList.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot categoryListDoc = docList.get("Categories");

                        long catCount = categoryListDoc.getLong("COUNT");
                        for(int i = 1; i<= catCount; i++){
                            String catID = categoryListDoc.getString("CAT"+String.valueOf(i)+"_ID");
                            QueryDocumentSnapshot catDoc = docList.get
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
