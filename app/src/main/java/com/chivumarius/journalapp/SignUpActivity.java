package com.chivumarius.journalapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class SignUpActivity extends AppCompatActivity {

    // ▼ "DECLARATION" OF "WIDGETS IDS" ▼
    EditText password_create;
    EditText username_create;
    EditText email_create;
    Button createBTN;



    // ▼ "FIREBASE AUTHENTICATION" ▼
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    // ▼ "FIREBASE CONNECTION" ▼
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");



    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON CREATE()" METHOD ▼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




        // ▼ "FIREBASE AUTHENTICATION"
        //      → WHICH REQUIRE "GOOGLE ACCOUNT"
        //      → ON THE "DEVICE"
        //      → TO "RETURN SUCCESSFULLY" ▼
        firebaseAuth = FirebaseAuth.getInstance();


        // ▼ "INITIALIZATION" OF "WIDGETS IDS" ▼
        createBTN = findViewById(R.id.acc_sign_up_button);
        password_create = findViewById(R.id.password_create);
        email_create = findViewById(R.id.email_create);
        username_create = findViewById(R.id.userName_create_ET);



        // ▼ "AUTHENTICATION" ▼
        authStateListener = new FirebaseAuth.AuthStateListener() {
            // ▼ "ON AUTH STATE CHANGED()" METHOD ▼
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // ▼ "INITIALIZATION" OF "CURRENT USER" ▼
                currentUser = firebaseAuth.getCurrentUser();

                // ▼ "CHECKING": IF "THERE IS" A "CURRENT USER" ▼
                if (currentUser != null){
                    // USER AUTHENTICATED

                }else{
                    // USER NOT AUTHENTICATED
                }
            }
        };


        // ▼ "SETTING CLICK LISTENERS" FOR "CREATE BUTTON" ▼
        createBTN.setOnClickListener(new View.OnClickListener() {

            // ▼ "ON CLICK()" METHOD ▼
            @Override
            public void onClick(View view) {

                // ▼ "CHECKING": IF "FIELDS ARE FILLED" ▼
                if (!TextUtils.isEmpty(email_create.getText().toString())
                        && !TextUtils.isEmpty(password_create.getText().toString())){

                    // GETTING DATA ENTERED BY USER ▼
                    String email = email_create.getText().toString().trim();
                    String password = password_create.getText().toString().trim();
                    String username = username_create.getText().toString().trim();


                    // CALLING "CREATE USER EMAIL ACCOUNT()" METHOD ▼
                    CreateUserEmailAccount(email, password, username);

                } else {
                    // ▼ "TOAST MESSAGE": "EMPTY FIELDS" ▼
                    Toast.makeText(SignUpActivity.this,
                            "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "CREATE USER EMAIL ACCOUNT()" METHOD ▼
    private void CreateUserEmailAccount(String email, String password, final String username) {

        // ▼ "CHECKING": IF "FIELDS ARE FILLED" ▼
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){

            // ▼ "AUTHENTICATION" ▼
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        // ▼ "ON COMPLETE()" METHOD ▼
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // ▼ WE "TAKE" THE "USER" TO THE "NEXT ACTIVITY": ("ADD JOURNAL ACTIVITY") ▼
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserId = currentUser.getUid();


                                // ▼ CREATE A "USER MAP",
                                //      → SO WE CAN "CREATE" A "USER"
                                //      → IN THE "USER COLLECTION" IN "FIRESTORE" ▼
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);


                                // ▼ "ADDING" THE "USER" TO THE "USER COLLECTION" IN "FIRESTORE" ▼
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                            // ▼ "ON SUCCESS()" METHOD ▼
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                // ▼ DOCUMENT REFERENCE: "SUCCESSFULLY" ADDED TO THE "USER COLLECTION" IN "FIRESTORE" ▼
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                                            // ▼ "ON COMPLETE()" METHOD ▼
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                // ▼ "CHECKING": IF "USER IS REGISTERED" ▼
                                                                if (Objects.requireNonNull(task.getResult()).exists()) {

                                                                    // ▼ GETTING: "USER NAME" ▼
                                                                    String name = task.getResult().getString("username");


                                                                    // ▼ IF THE "USER" IS "REGISTERED SUCCESSFULLY",
                                                                    //      → THEN MOVE TO "ADD JOURNAL ACTIVITY" SCREEN ▼
                                                                    Intent i = new Intent(SignUpActivity.this,
                                                                            AddJournalActivity.class);

                                                                    i.putExtra("username", name);
                                                                    i.putExtra("userId", currentUserId);
                                                                    startActivity(i);

                                                                } else {

                                                                }
                                                            }
                                                        })

                                                        .addOnFailureListener(new OnFailureListener() {

                                                            // ▼ "ON FAILURE()" METHOD ▼
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // ▼ "TOAST MESSAGE": "FAILED" ▼
                                                                Toast.makeText(
                                                                        SignUpActivity.this,
                                                                        "Something went wrong",
                                                                        Toast.LENGTH_SHORT
                                                                ).show();
                                                            }
                                                        });
                                            }
                                        });

                            }
                        }
                    });
        }
    }





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON START()" METHOD ▼
    @Override
    protected void onStart() {
        super.onStart();

        // ▼ "AUTHENTICATION" ▼
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
