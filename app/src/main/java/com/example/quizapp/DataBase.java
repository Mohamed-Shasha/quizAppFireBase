package com.example.quizapp;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public static List<CategoryModel> g_cat_List = new ArrayList<>();
    public static int cat_index = 0;
    public static int selectedTestIndex = 0;
    public static List<TestModel> g_test_List = new ArrayList<>();

    public static List<QuestionModel> g_question_list = new ArrayList<>();
    public static ProfileModel profile = new ProfileModel("n", null);

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

    public static void getProfile(MyCompleteListener myCompleteListener) {
//        get user by  firebase user ID
        db.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        profile.setName(documentSnapshot.getString("NAME"));
                        profile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();

                    }
                });
    }

    public static void loadUserDate(MyCompleteListener myCompleteListener) {
        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getProfile(myCompleteListener);

            }

            @Override
            public void onFailure() {

            }
        });

    }


    public static void loadCategories(MyCompleteListener completeListener) {
        g_cat_List.clear();
        db.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            docList.put(doc.getId(), doc);
                        }
                        QueryDocumentSnapshot categoryListDoc = docList.get("Categories");

                        long catCount = categoryListDoc.getLong("COUNT");

                        for (int i = 1; i <= catCount; i++) {

                            String catID = categoryListDoc.getString("CAT" + String.valueOf(i) + "_ID");
                            QueryDocumentSnapshot catDoc = docList.get(catID);
                            int NumOfTest = catDoc.getLong("NUM_OF_TESTS").intValue();
                            String catName = catDoc.getString("NAME");
                            g_cat_List.add(new CategoryModel(catID, catName, NumOfTest));
                        }
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


    public static void loadTestData(MyCompleteListener myCompleteListener) {
        g_test_List.clear();
//        get document quiz from user selected index
        db.collection("QUIZ").document(g_cat_List.get(cat_index).getDocumentID())
                .collection("TEST_LIST").document("TEST_INFO").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int Num_Of_Test = g_cat_List.get(cat_index).getNumOfTests();


                        for (int i = 1; i <= Num_Of_Test; i++) {

                            String test_ID = documentSnapshot.getString("TEST" + String.valueOf(i)+"_ID");
                            int test_time = documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue();
                            g_test_List.add(new TestModel(test_ID, 0, test_time));


                        }
                        myCompleteListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }

    public static void loadQuestions(MyCompleteListener myCompleteListener) {
        g_question_list.clear();
        db.collection("Questions")
               .whereEqualTo("CATEGORY", g_cat_List.get(cat_index).getDocumentID())
              .whereEqualTo("TEST", g_test_List.get(selectedTestIndex).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            String question = documentSnapshot.getString("QUESTION");
                            String a = documentSnapshot.getString("A");
                            String b = documentSnapshot.getString("B");
                            String c = documentSnapshot.getString("C");
                            String d = documentSnapshot.getString("D");
                            int answer = documentSnapshot.getLong("ANSWER").intValue();
                            g_question_list.add(new QuestionModel(question,a,b,c,d,answer));


                        }
                        Log.d("test", String.valueOf(g_question_list.size()));
                        Log.d("question", String.valueOf(g_question_list));
                        Log.d("test", String.valueOf(g_test_List).toString());
                        Log.d("selectedTestIndex", String.valueOf(selectedTestIndex));
                        Log.d("cat_index", String.valueOf(cat_index));

                        myCompleteListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });

    }


}