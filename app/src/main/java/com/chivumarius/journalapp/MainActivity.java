package com.chivumarius.journalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import util.JournalUser;


public class MainActivity extends AppCompatActivity {

    // ▼ "DECLARATION" OF "WIDGETS IDS" ▼
    Button loginBTN;
    Button createAccBTN;
    private EditText emailET;
    private EditText passET;


    // ▼ "FIREBASE AUTHENTICATION" ▼
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    // ▼ "FIREBASE CONNECTION" ▼
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");







    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON CREATE()" METHODS ▼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // ▼ "INITIALIZATION" OF "WIDGETS IDS" ▼
        loginBTN = findViewById(R.id.email_sign_in_button);
        createAccBTN = findViewById(R.id.create_acct_BTN);
        emailET = findViewById(R.id.email);
        passET  = findViewById(R.id.password);


        // ▼ "INITIALIZATION" OF "AUTHENTICATION REFERENCE" ▼
        firebaseAuth = FirebaseAuth.getInstance();



        // ▼ "CREATE FUNCTIONALITY" ▼
        // ▼ "SETTING CLICK LISTENERS" FOR "CREATE ACCOUNT BUTTON" ▼
        createAccBTN.setOnClickListener(new View.OnClickListener() {

            // ▼ "ON CLICK()" METHOD ▼
            @Override
            public void onClick(View view) {
                // ▼ "STARTING" ANOTHER "SCREEN" ▼
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });



        // ▼ "LOGIN FUNCTIONALITY" ▼
        // ▼ "SETTING CLICK LISTENERS" FOR "LOGIN BUTTON" ▼
        loginBTN.setOnClickListener(new View.OnClickListener() {

            // ▼ "ON CLICK()" METHOD ▼
            @Override
            public void onClick(View view) {

                // ▼ "CALLING" THE "METHOD" ▼
                LoginEmailPasswordUser(
                        emailET.getText().toString().trim(),
                        passET.getText().toString().trim()
                );
            }
        });
    }





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "LOGIN EMAIL PASSWORD USER()" METHODS
    //       → WITH "LOGIN FUNCTIONALITY" ▼
    private void LoginEmailPasswordUser(String email, String pwd) {

        // ▼ "CHECKING": FOR "EMPTY TEXTS" ▼
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)){

            // ▼ "AUTHENTICATING" THE "USER" ▼
            firebaseAuth.signInWithEmailAndPassword(email,pwd)

                    // ▼ "ADD ON COMPLETE LISTENER()" METHOD ▼
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        // ▼ "ON COMPLETE()" METHOD ▼
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // ▼ GETTING THE "CURRENT USER" ▼
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            // "SETTING" THE "USER" AS "NOT NULL" ▼
                            assert user != null;

                            // ▼ "GETTING" THE "USER ID" ▼
                            final String currentUserId = user.getUid();


                            // ▼ "CREATING" A "COLLECTION REFERENCE" ▼
                            collectionReference.
                                    whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                                        // ▼ "ON EVENT()" METHOD ▼
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            // ▼ IF "THERE IS" AN "ERROR" ▼
                                            if (error != null){

                                            }

                                            // ▼ IF THERE IS "NO ERROR" ▼
                                            assert value != null;


                                            // ▼ IF "THERE IS" A "VALUE" ▼
                                            if (!value.isEmpty()){

                                                // ▼ "LOOPING": TO "GET" ALL "QUERY DOCUMENT SNAPSHOTS" ▼
                                                for (QueryDocumentSnapshot snapshot : value){

                                                    // ▼ GETTING THE "JOURNAL USER INSTANCE" ▼
                                                    JournalUser journalUser = JournalUser.getInstance();

                                                    // ▼ SETTING THE "JOURNAL USERNAME" ▼
                                                    journalUser.setUsername(snapshot.getString("username"));

                                                    // ▼ SETTING THE "JOURNAL USER ID" ▼
                                                    journalUser.setUserId(snapshot.getString("userId"));



                                                    // ▼ "STARTING" THE "LIST ACTIVITY" SCREEN
                                                    //      → AFTER "LOGIN SUCCESSFUL" ▼

                                                    // ▼ "DISPLAYING" THE "LIST" OF "JOURNALS" AFTER "LOGIN" ▼
                                                    //  startActivity(new Intent(MainActivity.this, AddJournalActivity.class));
                                                    startActivity(new Intent(MainActivity.this, JournalListActivity.class));
                                                }
                                            }
                                        }
                                    });
                        }
                    })


                    // ▼ "ADD ON FAILURE LISTENER()" METHOD ▼
                    .addOnFailureListener(new OnFailureListener() {

                        // ▼ "ON FAILURE()" METHOD ▼
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // ▼ DISPLAYING AN "TOAST ERROR MESSAGE" ▼
                            Toast.makeText(
                                    MainActivity.this,
                                    "Something went wront " + e,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

        } else {

            // ▼ DISPLAYING AN "TOAST ERROR MESSAGE" ▼
            Toast.makeText(
                    MainActivity.this,
                    "Please Enter email & password",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }
}