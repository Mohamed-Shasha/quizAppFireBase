package com.example.quizapp;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quizapp.Model.RankModel;
import com.example.quizapp.Model.CategoryModel;
import com.example.quizapp.Model.ProfileModel;
import com.example.quizapp.Model.QuestionModel;
import com.example.quizapp.Model.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataBase {
    // Access a Cloud Firestore instance from your Activity
    public static FirebaseFirestore db;
    public static List<CategoryModel> g_cat_List = new ArrayList<>();
    public static int cat_index = 0;
    public static int selectedTestIndex = 0;
    public static List<TestModel> g_test_List = new ArrayList<>();
    public static FirebaseAuth mAuth;

    public static List<QuestionModel> g_question_list = new ArrayList<>();
    public static List<QuestionModel> g_question_bookmarked = new ArrayList<>();
    public static int usersTotal = 0;
    public static boolean inTopList = false;
    public static List<RankModel> usersList = new ArrayList<>();
    public static List<String> g_bookmarkIdList = new ArrayList<>();
    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;
    public static ProfileModel profile = new ProfileModel("n", null, null, "n", 0);
    public static RankModel performance = new RankModel(0, -1, "n");

    static void createUser(String email, String name, MyCompleteListener completeListener) {
        // Create a new user with a first and last name , score , bookmarks number
        Map<String, Object> userData = new HashMap<>();
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);
        userData.put("BOOKMARKS", 0);

        // multiple writes in single atomic
        WriteBatch batch = db.batch();

        //get user id from firebase authentication table
        DocumentReference userDocReference = db.collection("USERS").document((FirebaseAuth.getInstance().getCurrentUser()).getUid());
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

    // load user-profile data
    public static void getProfile(MyCompleteListener myCompleteListener) {
//        get user by  firebase user ID
        db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        profile.setName(documentSnapshot.getString("NAME"));
                        profile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                        profile.setClassCode(documentSnapshot.getString("CLASS_CODE"));


                        if (documentSnapshot.getString("PHONE") != null) {
                            profile.setPhoneNumber(documentSnapshot.getString("PHONE"));

                        }
                        if (documentSnapshot.get("BOOKMARKS") != null) {
                            profile.setBookmarkCount(documentSnapshot.getLong("BOOKMARKS").intValue());

                        }
                        performance.setName(documentSnapshot.getString("NAME"));
                        performance.setTotalScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());

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

    //     update name and phone
    public static void updateProfileDate(String name, String phone, MyCompleteListener myCompleteListener) {

        Map<String, Object> profileData = new ArrayMap<>();

        profileData.put("NAME", name);
        profileData.put("PHONE", phone);


//      get current user from USERS collection
        db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
//                update user document
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        profile.setName(name);
                        profile.setPhoneNumber(phone);
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

    public static void updateClassCode(Context context,String classCode, MyCompleteListener myCompleteListener) {


        db.collection("CLASS").document("Code").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String classCodeDatabase = documentSnapshot.getString("CLASS_CODE");
                        if (classCode.equals(classCodeDatabase)) {
                            Map<String, Object> classData = new ArrayMap<>();
                            classData.put("CLASS_CODE", classCode);
                            db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
//                update user document

                                    .set(classData, SetOptions.merge())

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            profile.setClassCode(classCode);
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
                        else {
                            showToast(context,"Error class code");
                        }

                    }


                });

    }

    public static void showToast(Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
//      get current user from USERS collection


    //    load User data
    public static void loadUserDate(MyCompleteListener myCompleteListener) {
//        load categories
        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
//                load profile
                getProfile(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
//                        load number of users

                        getTotalUsers(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
//                              load bookmarked question IDs
                                loadBookmarkId(myCompleteListener);

                            }

                            @Override
                            public void onFailure() {
                                myCompleteListener.onFailure();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        myCompleteListener.onFailure();
                    }
                });

            }

            @Override
            public void onFailure() {
                myCompleteListener.onFailure();
            }
        });

    }

    //    load categories from fire_store Database
    public static void loadCategories(MyCompleteListener completeListener) {
//        clear category list
        g_cat_List.clear();
//        get collection quiz and loop though it
        db.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override

                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        store document lists in Map
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
//                        loop through map values
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                          add each document to the list
                            docList.put(doc.getId(), doc);
                        }
//                        get categories document
                        QueryDocumentSnapshot categoryListDoc = docList.get("Categories");


//                        get count field of categories
                        long catCount = categoryListDoc.getLong("COUNT");
//                        loop until count is 0
                        for (int i = 1; i <= catCount; i++) {
//                            get Category ID string
                            String catID = categoryListDoc.getString("CAT" + String.valueOf(i) + "_ID");
//                            here we get the category by document ID read
                            QueryDocumentSnapshot catDoc = docList.get(catID);
//                            get category name and number of tests
                            int NumOfTest = catDoc.getLong("NUM_OF_TESTS").intValue();
                            String catName = catDoc.getString("NAME");
//                            add our  data to static local categoryList of CategoryModel class
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

//        get document quiz test data
        db.collection("QUIZ").document(g_cat_List.get(cat_index).getDocumentID())
                .collection("TEST_LIST").document("TEST_INFO").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

//                      get number of test from user selected test index
                        int Num_Of_Test = g_cat_List.get(cat_index).getNumOfTests();


                        for (int i = 1; i <= Num_Of_Test; i++) {
//                            get test id and test time
                            String test_ID = documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID");
                            int test_time = documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue();
//                            add to static list the data retrieved from database
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
//        clear question list
        g_question_list.clear();

//        get question by searching the values that connects with user selected test and selected category
        db.collection("Questions")
                .whereEqualTo("CATEGORY", g_cat_List.get(cat_index).getDocumentID())
                .whereEqualTo("TEST", g_test_List.get(selectedTestIndex).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
//                    loop through that document
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

//                       set bookmark to true if  bookmark list has question ID
                            boolean isBookmarked = g_bookmarkIdList.contains(documentSnapshot.getId());

                            String question = documentSnapshot.getString("QUESTION");
                            String a = documentSnapshot.getString("A");
                            String b = documentSnapshot.getString("B");
                            String c = documentSnapshot.getString("C");
                            String d = documentSnapshot.getString("D");
                            int answer = documentSnapshot.getLong("ANSWER").intValue();

//                            set the question static list data from data retrieved
                            g_question_list.add(new QuestionModel(documentSnapshot.getId(),
                                    question, a, b, c, d, answer, -1, NOT_VISITED, isBookmarked));


                        }
                        Log.d("test", String.valueOf(g_question_list.size()));
                        Log.d("question", String.valueOf(g_question_list));
                        Log.d("test", String.valueOf(g_test_List));
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

    //    send grade to fire_store DB
    public static void sendResult(int score, MyCompleteListener myCompleteListener) {

//        allow multiple writes at the same time
        WriteBatch writeBatch = db.batch();


//       set bookmarks data in a Map
        Map<String, Object> bookmarkData = new ArrayMap<>();

//        loop through questions bookmarked from previous list
        for (int i = 0; i < g_bookmarkIdList.size(); i++) {
            bookmarkData.put("BM" + String.valueOf(i) + "_ID", g_bookmarkIdList.get(i));
        }

//        get document BOOKMARKS
        DocumentReference bookmarkDocument = db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("USER_DATA").document("BOOKMARKS");

//      write to this document the bookmark id with question ID
        writeBatch.set(bookmarkDocument, bookmarkData);

        DocumentReference userDocument = db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
//       update user's number of bookmarked question
        writeBatch.update(userDocument, "BOOKMARKS", g_bookmarkIdList.size());

//        if score is more that what is in the database do update score
        if (score > g_test_List.get(selectedTestIndex).getTopScore()) {

            DocumentReference scoreDocument = userDocument.collection("USER_DATA").document("MY_SCORE");

            Map<String, Object> testData = new ArrayMap<>();
//            get score user for that particular selected test
            testData.put(g_test_List.get(selectedTestIndex).getTestID(), score);
//            if document do not exist will be created and  set
            writeBatch.set(scoreDocument, testData, SetOptions.merge());
        }
//      commit changes to fire_store DB
        writeBatch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("top", "onSuccess: " + score);
                        g_test_List.get(selectedTestIndex).setTopScore(score);


                        myCompleteListener.onSuccess();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
//      update total score
        updateTotalScore();
    }

    public static void loadScore(MyCompleteListener myCompleteListener) {
        db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("USER_DATA").document("MY_SCORE")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

//                      loop through the size of tests
                        for (int i = 0; i < g_test_List.size(); i++) {
                            if (documentSnapshot.get(g_test_List.get(i).getTestID()) != null) {
//                                get score of this document
                                int top = documentSnapshot.getLong(g_test_List.get(i).getTestID()).intValue();
                                g_test_List.get(i).setTopScore(top);

                            }

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


    public static void updateTotalScore() {

        final int[] sum = {0};
        db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("USER_DATA").document("MY_SCORE")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        List<Integer> numbers = new ArrayList<>();
//                        get my_score Data and store it in a map
                        Map<String, Object> map = documentSnapshot.getData();
                        assert map != null;

//                        iterate through the map
                        for (Map.Entry<String, Object> entry : map.entrySet()) {

//                            add numbers to the numbers list
                            numbers.add(Integer.parseInt(entry.getValue().toString()));
                        }

                        for (int i = 0; i < numbers.size(); i++) {
//                            total score summed
                            sum[0] += numbers.get(i);

                        }
                        DocumentReference userDocument = db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                        userDocument.update("TOTAL_SCORE", sum[0]);
                        performance.setTotalScore(sum[0]);
                        numbers.clear();


                    }
                });

    }


    public static void getTopUsers(MyCompleteListener myCompleteListener) {
        usersList.clear();
        String uID = FirebaseAuth.getInstance().getUid();
// limit users by 20
        db.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()

                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int rank = 1;
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            usersList.add(
                                    new RankModel(
                                            queryDocumentSnapshot.getLong("TOTAL_SCORE").intValue(),
                                            rank,
                                            queryDocumentSnapshot.getString("NAME")
                                    )
                            );
                            if (uID.compareTo(queryDocumentSnapshot.getId()) == 0) {
                                inTopList = true;
                                performance.setRank(rank);
                            }
                            rank++;
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

    //    get the number of app users
    public static void getTotalUsers(MyCompleteListener myCompleteListener) {

        db.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        usersTotal = documentSnapshot.getLong("COUNT").intValue();


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


    public static void loadBookmarkId(MyCompleteListener myCompleteListener) {

        g_bookmarkIdList.clear();
        //                      count number of booked marked questions
        db.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int count = documentSnapshot.getLong("BOOKMARKS").intValue();
                Log.e("count", String.valueOf(count));
                //        user's bookmarks
                db.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .collection("USER_DATA").document("BOOKMARKS")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

//                                fetch question ID
                                for (int i = 0; i < count; i++) {
                                    Log.e("count", String.valueOf(count));
                                    String boomMarkId = documentSnapshot.getString("BM" + String.valueOf(i) + "_ID");
                                    g_bookmarkIdList.add(boomMarkId);
                                    Log.e("TAG", String.valueOf(g_bookmarkIdList));
                                    Log.e("here", String.valueOf(1));
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
        });


    }

    public static void loadBookmarkedQues(MyCompleteListener myCompleteListener) {

        g_question_bookmarked.clear();
        final int[] temp = {0};

        if (g_bookmarkIdList.size() == 0) {
            myCompleteListener.onSuccess();

        }

        for (int i = 0; i < g_bookmarkIdList.size(); i++) {

//                fetch booked marked questions IDs from static g_bookmarkIdList
            String documentID = String.valueOf(g_bookmarkIdList.get(i));

//                start fetching documents one by one
            db.collection("Questions").document(documentID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
//                                  get data from the Question and  store the question booked marked  in QuestionModel list
                                g_question_bookmarked.add(new QuestionModel(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("QUESTION"),
                                        documentSnapshot.getString("A"),
                                        documentSnapshot.getString("B"),
                                        documentSnapshot.getString("C"),
                                        documentSnapshot.getString("D"),
                                        documentSnapshot.getLong("ANSWER").intValue(),
                                        0,
                                        -1,
                                        false
                                ));
                                temp[0]++;
                            }

//                                once we reached the end list we call its end

                            if (temp[0] == g_bookmarkIdList.size()) {
                                myCompleteListener.onSuccess();
                            }
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

}
